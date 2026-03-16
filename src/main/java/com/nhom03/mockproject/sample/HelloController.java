package com.nhom03.mockproject.sample;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/*
Date        Author  Desc
3-16-2026   AXL24   Ceate
 */
@RestController
@RequestMapping("/api/test")
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        //current date time
        Date date = new Date();
        String dateStr = date.toString();
        String response = "Hello, World!\nCurrent timestamp: " +dateStr;
        return ResponseEntity.ok(response);
    }
}
