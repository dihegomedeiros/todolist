package com.diego.todolist.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private IUserRepository userRepository;

    UserController(IUserRepository repository) {
        this.userRepository = repository;
    }

    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody UserModel userModel) {

        var existingUser = this.userRepository.findByEmail(userModel.getEmail());

        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already taken");
        }

        final String name = userModel.getName();
        final String email = userModel.getEmail();
        final String password = userModel.getPassword();

        final boolean USER_HAS_ALL_FIELDS = name != null && email != null && password != null;

        if (!USER_HAS_ALL_FIELDS) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The user must have a name, email, and password.");
        }

        // Checks if the name has the wrong length
        if (name.length() <= 0 || name.length() > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The username must be between 3 and 50 characters long.");
        }

        // Checks if the email has the wrong length
        if (email.length() < 3 || email.length() > 320) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The email must be between 3 and 320 characters long.");
        }

        // Checks if the password has the wrong length
        if (password.length() < 8 || password.length() > 128) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The password must be between 8 and 128 characters long.");
        }

        var userCreated = this.userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
