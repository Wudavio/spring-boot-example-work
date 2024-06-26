package com.springboot.examplework.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Example API", description = "Example APIs")
public class ExampleController {
    
    @GetMapping("/")
    public String root() {
        return "Hello World!";
    }
}
