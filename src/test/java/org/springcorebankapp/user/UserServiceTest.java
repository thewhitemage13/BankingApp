package org.springcorebankapp.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springcorebankapp.account.AccountService;

import java.util.Arrays;
import java.util.List;

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

        // Мокируем метод findAll() репозитория
        Mockito.doReturn(mockUsers).when(this.userRepository).findAll();

        // Выполняем метод getAllUsers
        var response = this.userService.getAllUsers();

        // Проверяем результат
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("user1", response.get(0).getLogin());
        assertEquals("user2", response.get(1).getLogin());
    }
}
