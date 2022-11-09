package com.badfox.osstest.controller;

import com.badfox.osstest.advice.NotControllerResponseAdvice;
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
