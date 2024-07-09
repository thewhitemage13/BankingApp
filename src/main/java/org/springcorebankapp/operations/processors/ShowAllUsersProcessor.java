package org.springcorebankapp.operations.processors;

import org.springcorebankapp.operations.ConsoleOperationType;
import org.springcorebankapp.operations.OperationCommandProcessor;
import org.springcorebankapp.user.User;
import org.springcorebankapp.user.UserRepository;
import org.springcorebankapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Component
@Transactional
public class ShowAllUsersProcessor implements OperationCommandProcessor {
    @Autowired
    private UserRepository userRepository;
    private final UserService userService;

    public ShowAllUsersProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        List<User> users = userService.getAllUsers();
        System.out.println("List of all users: ");
        List<User> all = userRepository.findAll();
        for (User user : all) {
            System.out.println(user);
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
