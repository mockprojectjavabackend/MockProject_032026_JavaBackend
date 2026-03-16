package com.nhom03.mockproject.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @GetMapping
    public ResponseEntity<String> getTask(){
        return ResponseEntity.ok("This is a task");
    }
}
