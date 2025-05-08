package com.aktog.yusuf.ratelimiter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a")
public class Example {

    @GetMapping
    public String hello() {
        return "Hello World";
    }
}
