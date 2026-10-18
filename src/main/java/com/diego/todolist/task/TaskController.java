package com.diego.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final ITaskRepository taskRepository;

    TaskController(ITaskRepository repository) {
        this.taskRepository = repository;
    }

    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

        final String title = taskModel.getTitle();
        final UUID userId = (UUID) request.getAttribute("userId");
        
        taskModel.setUserId(userId);
        
        if (taskModel.getUserId() == null) {
            return ResponseEntity.badRequest().body("The task must have a corresponding user ID.");
        }

        if (title == null) {
            return ResponseEntity.badRequest().body("The task must have a title.");
        }
        
        // Checks if the title has the wrong length
        if (title.length() < 1 || title.length() > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The task's title must be between 1 and 50 characters long.");
        }

        // Checks if a task with the same title already exists.
        if (this.taskRepository.findByTitle(taskModel.getTitle()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A task with the same title already exists.");
        }

        // Checks whether the due date is later than the current date
        if (LocalDateTime.now().isAfter(taskModel.getDueDate())) {
            return ResponseEntity.badRequest().body("The due date must be later than the current date.");
        }

        var taskCreated = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }
}
