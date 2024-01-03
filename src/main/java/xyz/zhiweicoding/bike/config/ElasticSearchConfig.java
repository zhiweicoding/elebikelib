package xyz.zhiweicoding.bike.config;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.function.BiConsumer;

/**
 * @author zhiweicoding.xyz
 * @date 1/3/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
@Configuration
public class ElasticSearchConfig {
    public static final int BUFFER_LIMIT = 5 * 100 * 1024 * 1024;

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
    @Value("${elasticsearch.auth.enable}")
    private boolean isAuth;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        HttpHost[] hosts = joinHosts();
        if (hosts == null) {
            throw new IllegalStateException("No Elasticsearch hosts configured");
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
                            .setMaxConnTotal(100)
                            .setMaxConnPerRoute(100)
                            .setKeepAliveStrategy((response, context) -> Duration.ofMinutes(10).toMillis())
                            .setDefaultCredentialsProvider(credentialsProvider));
        }
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        builder.setDefaultHeaders(headers);

        return new RestHighLevelClient(builder);
    }

    private HttpHost[] joinHosts() {
        if (StrUtil.isBlank(addr)) {
            return null;
        }
        String[] splits = addr.split(",");
        HttpHost[] hosts = new HttpHost[splits.length];
        for (int i = 0; i < hosts.length; i++) {
            String[] hostPorts = splits[i].split(COLON);
            hosts[i] = new HttpHost(hostPorts[0], Integer.parseInt(hostPorts[1]));
            log.info("build elasticsearch client=====>host={}, port={}", hostPorts[0], hostPorts[1]);
        }
        return hosts;
    }

    @Bean
    public BulkProcessor bulkProcessor(RestHighLevelClient restHighLevelClient) {
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
        return BulkProcessor.builder(consumer, listener)
                .setBulkActions(500).setBulkSize(new ByteSizeValue(10L, ByteSizeUnit.MB))
                .setConcurrentRequests(0).setFlushInterval(TimeValue.timeValueSeconds(1L))
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3)).build();
    }


}
