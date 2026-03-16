package com.nhom03.mockproject.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TamController {

    @GetMapping("/get-tam")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("Pham Tam ");
    }
}
