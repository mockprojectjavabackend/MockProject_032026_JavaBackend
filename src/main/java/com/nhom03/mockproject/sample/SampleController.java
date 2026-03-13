package com.nhom03.mockproject.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample")
public class SampleController {
    @GetMapping
    public ResponseEntity<String> getSampleString() {
        return ResponseEntity.ok("Project name: MockProject_032026_JavaBackend");
    }
}
