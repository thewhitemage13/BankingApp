package org.springcorebankapp.user;

import org.springcorebankapp.exception.LoginIsBusyException;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestParam("login") String login) {
        try {
            userService.createUser(login);
            return ResponseEntity.ok("User with login = %s crated".formatted(login));
        } catch (LoginIsBusyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with login = %s already exist".formatted(login));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/find-user-by-id/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable("userId") int userId) {
        try {
            return ResponseEntity.ok(userService.findUserById(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
