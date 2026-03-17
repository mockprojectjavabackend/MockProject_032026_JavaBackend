package com.nhom03.mockproject.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskController
 *
 * Version 1.0
 *
 * Date: 17-03-2026
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 17-03-2026         ThoHa             Get all task
 * 17-03-2026         ThoHa             create a task
 * 17-03-2026         ThoHa             get task by id
 * 17-03-2026         ThoHa             delete task
 */

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>(List.of(
            new Task(1,"Learn Spring Boot"),
            new Task(2,"Learn REST API")
    ));

    /**
     * get Task
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Task>> getTask(){
        return ResponseEntity.ok(tasks);
    }

    /**
     * create a Task
     * @param task
     * @return
     */
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task){
        tasks.add(task);
        return ResponseEntity.ok(task);
    }

    /**
     * get task by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id){

        for(Task task : tasks){
            if(task.getId() == id){
                return ResponseEntity.ok(task);
            }
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * delete task
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id){

        tasks.removeIf(task -> task.getId() == id);

        return ResponseEntity.ok("Delete task successfully");
    }
}

class Task{

    private int id;
    private String name;

    public Task(){}

    public Task(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}