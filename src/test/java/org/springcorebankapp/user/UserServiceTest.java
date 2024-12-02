package org.springcorebankapp.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springcorebankapp.account.AccountService;
import org.springcorebankapp.exception.LoginIsBusyException;
import org.springcorebankapp.exception.UserNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private UserService userService;

    // create user

    @Test
    void handleGetAllUsers_ReturnsAllUsers() {
        // User Test 1
        User user1 = new User();
        user1.setId(1);
        user1.setLogin("user1");

        // User Test 2
        User user2 = new User();
        user2.setId(2);
        user2.setLogin("user2");

        List<User> mockUsers = Arrays.asList(user1, user2);

        Mockito.doReturn(mockUsers).when(this.userRepository).findAll();

        var response = this.userService.getAllUsers();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("user1", response.get(0).getLogin());
        assertEquals("user2", response.get(1).getLogin());
    }

    @Test
    void handleCreateUser_LoginIsBusy() {
        // given
        String login = "existingUser";
        Mockito.when(userRepository.existsUserByLogin(login)).thenReturn(true);

        // when
        // then
        Assertions.assertThrows(LoginIsBusyException.class, () -> userService.createUser(login));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
        Mockito.verify(accountService, Mockito.never()).createAccount(Mockito.anyString());
    }

    // find user by id

    @Test
    void handleFindUserById_ReturnsUser() {
        // given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setLogin("user1");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        var response = this.userService.findUserById(userId);

        // then
        assertNotNull(response);
        assertEquals("user1", response.getLogin());
        assertEquals(userId, response.getId());
    }

    @Test
    void handleFindUserById_UserNotFound() {
        // given
        int userId = 999;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when / then
        Assertions.assertThrows(UserNotFoundException.class, () -> this.userService.findUserById(userId));
    }

    // get all users

    @Test
    void handleGetAllUsers_ReturnsUsers() {
        // given
        User user1 = new User();
        user1.setId(1);
        user1.setLogin("user1");

        User user2 = new User();
        user2.setId(2);
        user2.setLogin("user2");

        List<User> mockUsers = Arrays.asList(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(mockUsers);

        // when
        List<User> users = this.userService.getAllUsers();

        // then
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getLogin());
        assertEquals("user2", users.get(1).getLogin());
        Mockito.verify(userRepository).findAll();
    }
}
