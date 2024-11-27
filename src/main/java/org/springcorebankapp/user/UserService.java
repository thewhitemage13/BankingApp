package org.springcorebankapp.user;

import org.springcorebankapp.account.AccountService;
import org.springcorebankapp.exception.LoginIsBusyException;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountService accountService;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    //@CacheEvict(value = "users", key = "'allUsers'")
    public void createUser(String login) {
        if (userRepository.existsUserByLogin(login)) {
            throw new LoginIsBusyException("User already exists with login = %s".formatted(login));
        }

        var newUser = new User(login, new ArrayList<>());
        userRepository.save(newUser);
        var newAccount = accountService.createAccount(newUser.getLogin());

        newUser.getAccountList().add(newAccount);
    }

    //@Cacheable(value = "users", key = "#id")
    public User findUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id = %s not found".formatted(id)));

        user.getAccountList().size();

        return user;
    }

    //@Cacheable(value = "users", key = "'allUsers'")
    public List<User> getAllUsers() {
        logger.info("Fetching all users from DB");
        return userRepository.findAll();
    }

}
