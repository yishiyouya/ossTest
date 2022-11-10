package com.badfox.basicpro.controller;

import com.badfox.basicpro.advice.NotControllerResponseAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 15210
 */
@RestController
public class HealthController {
    @NotControllerResponseAdvice
    @GetMapping("/health")
    public String health() {
        return "success";
    }
}
