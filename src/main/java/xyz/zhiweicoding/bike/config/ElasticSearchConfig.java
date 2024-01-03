package xyz.zhiweicoding.bike.config;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.protocol.HTTP;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.rest.RestHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author zhiweicoding.xyz
 * @date 1/3/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
@Component
public class ElasticSearchConfig {
    public static final int BUFFER_LIMIT = 5 * 100 * 1024 * 1024;

    private static final String SPLITER = ",";

    private static final String COLON = "[:]";

    @Value("${elasticsearch.urls}")
    private String addr;

    @Value("${elasticsearch.auth.username}")
    private String username;

    @Value("${elasticsearch.auth.password}")
    private String password;
    /**
     * 是否开启安全验证，默认是
     */
    @Value("${elasticsearch.auth.enable:true}")
    private boolean isAuth;

    @Getter
    private RestClient restClient;
    @Getter
    private RestHighLevelClient restHighLevelClient;
    @Getter
    private BulkProcessor bulkProcessor;

    @PostConstruct
    public void preMethod() {
        //构建rest client
        buildClient();
        //构建bulk processor
        buildBulkProcessor();
        //设置es查询buffer大小
        setBufferLimit();
    }

    @PreDestroy
    public void afterMethod() {
        if (restClient != null) {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("Closing elasticsearch client error.{}", e.getMessage(), e);
            }
        }
        if (restHighLevelClient != null) {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                log.error("Closing elasticsearch restHighLevelClient error.{}", e.getMessage(), e);
            }
        }
        if (bulkProcessor != null) {
            try {
                bulkProcessor.awaitClose(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("Closing bulk processor error.{}", e.getMessage(), e);
            }
        }
        log.info("elasticsearch client closed.");
    }

    private HttpHost[] joinHosts() {
        if (StrUtil.isBlank(addr)) {
            return null;
        }
        String[] splits = addr.split(SPLITER);
        HttpHost[] hosts = new HttpHost[splits.length];
        for (int i = 0; i < hosts.length; i++) {
            String[] hostPorts = splits[i].split(COLON);
            hosts[i] = new HttpHost(hostPorts[0], Integer.parseInt(hostPorts[1]));
            log.info("build elasticsearch client=====>host={}, port={}", hostPorts[0], hostPorts[1]);
        }
        return hosts;
    }

    /**
     * 构建ElasticsearchClient
     */
    private void buildClient() {
        HttpHost[] hosts = joinHosts();
        if (hosts == null) {
            log.error("Init elasticsearch rest high level client error.", new Throwable("No hosts and ports."));
            System.exit(1);
        }
        RestClientBuilder builder = RestClient.builder(hosts).setRequestConfigCallback(
                requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(0)
                        .setSocketTimeout(0)
                        .setConnectionRequestTimeout(0));

        if (isAuth) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            builder.setHttpClientConfigCallback(
                    httpClientBuilder -> httpClientBuilder
                            .setMaxConnTotal(200)
                            .setMaxConnPerRoute(200)
                            .setKeepAliveStrategy((response, context) -> Duration.ofMinutes(10).toMillis())
                            .setDefaultCredentialsProvider(credentialsProvider));
        }
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        builder.setDefaultHeaders(headers);
        this.restClient = builder.build();
        this.restHighLevelClient = new RestHighLevelClient(builder);
    }

    /**
     * 构建BulkProcessor
     */
    private void buildBulkProcessor() {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                int numberOfActions = request.numberOfActions();
                if (log.isDebugEnabled()) {
                    log.debug("Executing bulk [{}] with {} requests", executionId, numberOfActions);
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                log.error("Failed to execute bulk.", failure);
                failure.printStackTrace();
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                if (response.hasFailures()) {
                    for (BulkItemResponse itemResponse : response.getItems()) {
                        if (itemResponse.isFailed()) {
                            // 进一步获取操作的索引、类型、ID等信息
                            log.error("Failed operation: [{}], failure message: [{}], Index: [{}], Type: [{}], ID: [{}]",
                                    itemResponse.getOpType(),
                                    itemResponse.getFailureMessage(),
                                    itemResponse.getIndex(),
                                    itemResponse.getType(),
                                    itemResponse.getId());
                        }
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Bulk [{}] completed in {} milliseconds.", executionId, response.getTook().getMillis());
                    }
                }
            }
        };
        BiConsumer<BulkRequest, ActionListener<BulkResponse>> consumer = (request, bulkListener) -> restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT,
                bulkListener);
        this.bulkProcessor = BulkProcessor.builder(consumer, listener, "bulk_processor")
                .setBulkActions(500).setBulkSize(new ByteSizeValue(10L, ByteSizeUnit.MB))
                .setConcurrentRequests(0).setFlushInterval(TimeValue.timeValueSeconds(1L))
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3)).build();
    }

    /**
     * 设置es查询buffer大小
     */
    private void setBufferLimit() {
        try {
            //设置es查询buffer大小
            RequestOptions requestOptions = RequestOptions.DEFAULT;
            Class<? extends RequestOptions> aClass = requestOptions.getClass();
            Field aDefault = aClass.getDeclaredField("httpAsyncResponseConsumerFactory");
            // aDefault.setAccessible(true);
            ReflectionUtils.makeAccessible(aDefault);
            //设置默认的工厂
            aDefault.set(requestOptions, (HttpAsyncResponseConsumerFactory) () -> new HeapBufferedAsyncResponseConsumer(BUFFER_LIMIT));
            log.debug("set RequestOptions.DEFAULT bufferLimit to {}", BUFFER_LIMIT);
        } catch (Exception e) {
            log.error("set RequestOptions.DEFAULT bufferLimit error.", e);
        }
    }

}
