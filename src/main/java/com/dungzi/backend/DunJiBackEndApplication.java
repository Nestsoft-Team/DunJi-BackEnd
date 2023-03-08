package com.dungzi.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ServletComponentScan
@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.dungzi.backend.domain.*.dao")
@EnableMongoRepositories(basePackages = "com.dungzi.backend.domain.chat.mongo")
public class DunJiBackEndApplication {

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DunJiBackEndApplication.class, args);
    }

}
