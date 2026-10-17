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

    @Column (unique = true, length = 50)
    private String title;
    private String description;
    private String priority;
    private boolean completed;

    @CreationTimestamp 
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;

    @Column (nullable = false)
    private UUID userId;
}
