package com.diego.todolist.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping ("/users")
public class UserController {

    private IUserRepository userRepository;

    UserController(IUserRepository repository) {
        this.userRepository = repository;
    }

    @PostMapping ("/")
    public ResponseEntity<Object> create(@RequestBody UserModel userModel) {

        var existingUser = this.userRepository.findByEmail(userModel.getEmail());

        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already taken");
        }

        var userCreated = this.userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
