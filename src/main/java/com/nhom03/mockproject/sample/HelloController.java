package com.nhom03.mockproject.sample;


import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nhom03.mockproject.sample.dto.HelloRequestDTO;


/**
 * HelloController
 *
 * Version 1.0
 *
 * Date: 16-03-2026
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 16-03-2026         AXL24             Create
 * 17-03-2026         Agent             Add more HTTP methods
 */
@RestController
@RequestMapping("/api/test")
public class HelloController {

    /**
     * get hello message
     * @return
     */
    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {
        //get current date time
        Date date = new Date();
        String dateStr = date.toString();
        String response = "Hello, World!\nCurrent timestamp: " + dateStr;

        return ResponseEntity.ok(response);
    }

    /**
     * insert sample data
     * @param body
     * @return
     */
    @PostMapping("/hello")
    public ResponseEntity<String> insertHello(@RequestBody HelloRequestDTO body) {
        //return success message
        String response = "Inserted: " + body.toString();

        return ResponseEntity.ok(response);
    }

    /**
     * update sample data
     * @param id
     * @param body
     * @return
     */
    @PutMapping("/hello/{id}")
    public ResponseEntity<String> updateHello(@PathVariable String id, @RequestBody HelloRequestDTO body) {
        //return success message
        String response = "Updated id: " + id + " with: " + body.toString();

        return ResponseEntity.ok(response);
    }

    /**
     * delete sample data
     * @param id
     * @return
     */
    @DeleteMapping("/hello/{id}")
    public ResponseEntity<String> deleteHello(@PathVariable String id) {
        //return success message
        String response = "Deleted: " + id;

        return ResponseEntity.ok(response);
    }
}
