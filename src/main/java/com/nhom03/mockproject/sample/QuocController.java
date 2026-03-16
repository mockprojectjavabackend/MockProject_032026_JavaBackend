package com.nhom03.mockproject.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class QuocController {

    @GetMapping("/get-profilio")
    public ResponseEntity<String> getProfilioQuoc() {
        return ResponseEntity.ok("Huynh Dang Quoc - Truong Dai Hoc Duy Tan - dang thuc tap");
    }
}
