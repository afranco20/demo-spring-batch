package io.example.demospringbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class DemoSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringBatchApplication.class, args);
    }

}
