package org.springcorebankapp.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springcorebankapp.exception.LoginIsBusyException;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Controller", description = "Operations related to user management")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create a new user",
            description = "Adds a new user to the database. The user's login must be unique.",
            tags = {"User Controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "User with the specified login already exists"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser (
            @Parameter(description = "Unique login for the user", required = true)
            @RequestParam("login") String login) {
        try {
            userService.createUser(login);
            return ResponseEntity.ok("User with login = %s created".formatted(login));
        } catch (LoginIsBusyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with login = %s already exist".formatted(login));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Find a user by ID",
            description = "Retrieves a user's details based on their unique ID.",
            tags = {"User Controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @GetMapping("/find-user-by-id/{userId}")
    public ResponseEntity<User> findUserById(
            @Parameter(description = "Unique ID of the user", required = true)
            @PathVariable("userId") int userId) {
        try {
            return ResponseEntity.ok(userService.findUserById(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Retrieve all users",
            description = "Fetches a list of all users from the database.",
            tags = {"User Controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
