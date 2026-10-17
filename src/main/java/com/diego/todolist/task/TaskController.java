package com.diego.todolist.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping ("/tasks")
public class TaskController {

    private final ITaskRepository taskRepository;

    TaskController(ITaskRepository repository) {
        this.taskRepository = repository;
    }
    
    @PostMapping ("/")
    public ResponseEntity<Object> create(@RequestBody TaskModel taskModel) {

        if (taskModel.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The userId is required.");
        }

        var existingTask = this.taskRepository.findByTitle(taskModel.getTitle());

        if (existingTask != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A task with the same title already exists.");
        }
        
        if (taskModel.getTitle().length() == 0 || taskModel.getTitle().length() > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The task title must be between 1 and 50 characters long.");
        }

        var taskCreated = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }
}
