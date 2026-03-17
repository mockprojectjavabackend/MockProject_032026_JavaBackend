package com.nhom03.mockproject.sample.UserTest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhom03.mockproject.dto.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class UserController {

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<String>> getUser() {
        String response = "hello from userController";
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody UserRequestDto userRequestDto) {
        String response = userRequestDto.toString();
        return ResponseEntity.ok(ApiResponse.success("Created user", response));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<ApiResponse<String>> updateUser(
            @PathVariable int id,
            @RequestBody UserRequestDto userRequestDto) {

        String response = "Updated user id = " + id + ", data = " + userRequestDto.toString();
        return ResponseEntity.ok(ApiResponse.success("Updated user", response));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable int id) {

        String response = "Deleted user id = " + id;
        return ResponseEntity.ok(ApiResponse.success("Deleted user", response));
    }
}