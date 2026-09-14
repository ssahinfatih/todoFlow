package com.todoflow.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Authenticated");
    }
    @GetMapping("/admin")
    public ResponseEntity<String> adminTest() {
        return ResponseEntity.ok("Admin endpoint");
    }
    @GetMapping("/error")
    public ResponseEntity<String> errorTest() {

        throw new RuntimeException("Test hatası");

    }
}
