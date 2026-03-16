package com.nhom03.mockproject.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/testApi")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("This is a test API endpoint.");
    }

    @GetMapping("/vhaiTestApi")
    public ResponseEntity<String> vhaiTestApi() {
        return ResponseEntity.ok("vhaiTestApi");
    }
}
