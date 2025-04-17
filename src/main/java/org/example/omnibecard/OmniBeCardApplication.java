package org.example.omnibecard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.omnibecard.client")
public class OmniBeCardApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmniBeCardApplication.class, args);
    }

}
