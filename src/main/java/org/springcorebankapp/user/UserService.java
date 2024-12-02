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

/**
 * Service class that provides business logic for user management.
 * <p>
 * This service contains methods for creating users, retrieving user information, and managing
 * user accounts. It interacts with the {@link UserRepository} for user data persistence and
 * with the {@link AccountService} for account creation and management.
 * </p>
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountService accountService;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Creates a new user with the specified login.
     * <p>
     * This method checks if the login is already taken. If not, it creates a new {@link User}
     * with the provided login and adds a default account for the user. If the login is taken,
     * a {@link LoginIsBusyException} is thrown.
     * </p>
     *
     * @param login the login for the new user
     * @throws LoginIsBusyException if the login is already in use by another user
     */
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

    /**
     * Finds a user by their unique ID.
     * <p>
     * This method retrieves the user with the given ID from the database. If the user is not found,
     * a {@link UserNotFoundException} is thrown.
     * </p>
     *
     * @param id the ID of the user to find
     * @return the {@link User} with the specified ID
     * @throws UserNotFoundException if no user with the given ID is found
     */
    //@Cacheable(value = "users", key = "#id")
    public User findUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id = %s not found".formatted(id)));
    }

    /**
     * Retrieves all users from the database.
     * <p>
     * This method fetches all the users present in the database and logs the operation.
     * </p>
     *
     * @return a list of all {@link User}s
     */
    //@Cacheable(value = "users", key = "'allUsers'")
    public List<User> getAllUsers() {
        logger.info("Fetching all users from DB");
        return userRepository.findAll();
    }
}
