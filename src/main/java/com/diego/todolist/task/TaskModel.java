package com.diego.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data 
@Entity (name = "tb_tasks")
public class TaskModel {
    @Id 
    @GeneratedValue 
    private UUID id;

    @Column (unique = true, length = 50, nullable = false)
    private String title;
    private String description;

    @Column (nullable = false, length = 30)
    private String priority;
    private boolean completed = false;

    @CreationTimestamp 
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;

    @Column (nullable = false)
    private UUID userId;

    public void setTitle(String title) throws Exception {
        var taskTitle = title.trim();

        if (taskTitle.length() > 50) {
            throw new Exception("The title must have a maximum of 50 characters.");
        }
        
        if (taskTitle.length() == 0) {
            throw new Exception("The title cannot be empty.");
        }
        this.title = title;
    }
}
