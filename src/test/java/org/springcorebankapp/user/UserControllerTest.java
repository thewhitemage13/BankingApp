package org.springcorebankapp.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springcorebankapp.exception.LoginIsBusyException;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;

    @Test
    @DisplayName("GET /get-all-users - Non-empty User List")
    void handleGetAllUsers_ReturnsValidResponseEntity() {
        // User Test 1
        User user1 = new User();
        user1.setId(1);
        user1.setLogin("user1");

        // User Test 2
        User user2 = new User();
        user2.setId(2);
        user2.setLogin("user2");

        // given
        List<User> mockUsers = Arrays.asList
                (
                        user1,
                        user1
                );

        Mockito.doReturn(mockUsers).when(this.userService).getAllUsers();

        // when
        var response = this.userController.getAllUsers();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
    }

    @Test
    @DisplayName("GET /get-all-users - Empty List")
    void handleGetAllUsers_EmptyList() {
        // given
        Mockito.doReturn(List.of()).when(this.userService).getAllUsers();

        // when
        var response = this.userController.getAllUsers();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /get-all-users - Internal Server Error")
    void handleGetAllUsers_InternalServerError() {
        // given
        Mockito.doThrow(new RuntimeException("Unexpected error")).when(this.userService).getAllUsers();

        // when
        var response = this.userController.getAllUsers();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void handleCreateUser_ReturnsValidResponseEntity() {
        // given
        String login = "user3";

        //when
        var responseEntity = this.userController.createUser(login);

        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User with login = user3 created", responseEntity.getBody());
        verify(this.userService).createUser(login);
    }

    @Test
    @DisplayName("POST /create-user - Login Is Busy")
    void handleCreateUser_LoginIsBusy() {
        // given
        String login = "existingUser";

        Mockito.doThrow(new LoginIsBusyException("User already exists")).when(this.userService).createUser(login);

        // when
        var response = this.userController.createUser(login);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User with login = existingUser already exist", response.getBody());
    }

    @Test
    @DisplayName("POST /create-user - Internal Server Error")
    void handleCreateUser_InternalServerError() {
        // given
        String login = "newUser";

        Mockito.doThrow(new RuntimeException("Unexpected error")).when(this.userService).createUser(login);

        // when
        var response = this.userController.createUser(login);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody());
    }

    @Test
    @DisplayName("GET /find-user-by-id/{userId} - Success")
    void handleFindUserById_ReturnsValidResponseEntity() {
        // given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setLogin("user1");

        Mockito.doReturn(user).when(this.userService).findUserById(userId);

        // when
        var response = this.userController.findUserById(userId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user, response.getBody());
    }

    @Test
    @DisplayName("GET /find-user-by-id/{userId} - User Not Found")
    void handleFindUserById_UserNotFound() {
        // given
        int userId = 1;

        Mockito.doThrow(new UserNotFoundException("User not found")).when(this.userService).findUserById(userId);

        // when
        var response = this.userController.findUserById(userId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("GET /find-user-by-id/{userId} - Internal Server Error")
    void handleFindUserById_InternalServerError() {
        // given
        int userId = 1;

        Mockito.doThrow(new RuntimeException("Unexpected error")).when(this.userService).findUserById(userId);

        // when
        var response = this.userController.findUserById(userId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}