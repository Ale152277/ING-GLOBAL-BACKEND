package com.ingenieraglobal.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IngenieraGlobalECommerceApplication {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("sun.net.inetaddr.ttl", "0"); // fuerza re-resolución DNS
        SpringApplication.run(IngenieraGlobalECommerceApplication.class, args);
    }

}
