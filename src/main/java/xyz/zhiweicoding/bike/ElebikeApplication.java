package xyz.zhiweicoding.bike;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableAsync
@EnableCaching
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("xyz.zhiweicoding.bike.dao.mysql")
@EnableConfigurationProperties
@EnableAspectJAutoProxy
public class ElebikeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElebikeApplication.class, args);
        System.out.println("启动成功");
    }

}
