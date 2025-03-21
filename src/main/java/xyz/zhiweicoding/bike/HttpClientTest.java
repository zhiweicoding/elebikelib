package xyz.zhiweicoding.bike;

import com.alibaba.fastjson2.JSON;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhiweicoding.xyz
 * @date 3/15/24
 * @email diaozhiwei2k@gmail.com
 */
public class HttpServerTest {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/message", new MessageHandler());
        server.setExecutor(null);
        System.out.println("服务开启");
        server.start();
    }

    static class MessageHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("收到数据获取请求");

            String code = "2";
            String message = "未传入Token";
            String[] data = {};
            String callbackDate = "";
            int count = 0;

            System.out.println("未传入Token");
            Map<String, Object> bsonDocument = new HashMap<>();
            bsonDocument.put("code", code);
            bsonDocument.put("message", message);
            bsonDocument.put("data", data);
            bsonDocument.put("date", callbackDate);
            bsonDocument.put("count", count);
            String response = JSON.toJSONString(bsonDocument);
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
