package com.ingenieraglobal.ecommerce.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ApihealthController {
    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }
    
}
