package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import zipkin2.server.internal.EnableZipkinServer;
import zipkin2.storage.mysql.v1.MySQLStorage;


import javax.sql.DataSource;

@SpringBootApplication
@EnableZipkinServer
public class LySleuthApplication {
    @Bean
    public MySQLStorage mySQLStorage(DataSource datasource) {
        return MySQLStorage.newBuilder().datasource(datasource).executor(Runnable::run).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(LySleuthApplication.class, args);
    }
}
