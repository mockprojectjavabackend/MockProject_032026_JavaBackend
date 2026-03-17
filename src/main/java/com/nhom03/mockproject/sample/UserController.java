package com.nhom03.mockproject.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/*
Date        Author      Desc
3-17-2026   Phamtam23   Test coding convention API
 */
@RestController
@RequestMapping("/api/test")
public class UserController {

    // Fake database for testing CRUD operations
    private List<String> users = new ArrayList<>(List.of("Tam", "An", "Huy"));

    /**
     * get test message for api testing
     * return test message
     */
    @GetMapping("/get-tam")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("Pham Tam ");
    }

    /**
     * get list of users for testing API
     * @return list of mock users
     */
    @GetMapping("/users")
    public ResponseEntity<List<String>> getAllUsers() {
        return ResponseEntity.ok(users);
    }

    /**
     * add new user (mock)
     * @param name user
     * @return updated user list
     */
    @PostMapping("/users")
    public ResponseEntity<List<String>> addUser(@RequestBody String name) {
        try {

            if (name == null || name.trim().isEmpty()) {
                throw new Exception("User name cannot be empty");
            }

            users.add(name);
            return ResponseEntity.ok(users);

        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * update user by index (mock)
     * @param index user
     * @param name new
     * @return updated user list
     */
    @PutMapping("/users")
    public ResponseEntity<List<String>> updateUser(@RequestParam int index, @RequestBody String name) {
        try {

            if (index < 0 || index >= users.size()) {
                throw new Exception("Invalid user index");
            }

            users.set(index, name);
            return ResponseEntity.ok(users);

        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * delete user by index (mock)
     * @param index user index
     * @return updated user list
     */
    @DeleteMapping("/users")
    public ResponseEntity<List<String>> deleteUser(@RequestParam int index) {
        try {

            if (index < 0 || index >= users.size()) {
                throw new Exception("User index does not exist");
            }

            users.remove(index);
            return ResponseEntity.ok(users);

        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }
    }




}
