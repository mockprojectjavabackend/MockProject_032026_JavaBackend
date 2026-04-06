package com.mockproject.notary_admin_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.mockproject.notary_common.entity")
public class NotaryAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotaryAdminServerApplication.class, args);
    }
}
