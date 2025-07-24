package com.musti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String home() {
        return "welcome to trading home r";
    }

    @GetMapping("/api")
    public String secure () {
        return "welcome to trading home secure ";
    }

}
