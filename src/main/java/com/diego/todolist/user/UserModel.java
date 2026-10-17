package com.diego.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data 
@Entity (name = "tb_users")
public class UserModel {
    @Id 
    @GeneratedValue (generator = "UUID")
    private UUID id;
    
    @Column (length = 50, nullable = false)
    private String name;

    @Column (unique = true, length = 320, nullable = false)
    private String email;

    @Column (length = 128, nullable = false)
    private String password;

    @CreationTimestamp 
    private LocalDateTime createdAt;
}
