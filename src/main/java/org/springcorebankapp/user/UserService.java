
package org.springcorebankapp.user;

import org.springcorebankapp.account.AccountService;
import org.springcorebankapp.exception.LoginIsBusyException;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springcorebankapp.redis.repository.UserRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final Map<Integer, User> userMap;
    private final Set<String> takenLogins;

    @Autowired
    private final AccountService accountService;
    @Autowired
    private UserRedisRepository userRedisRepository;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
        this.userMap = new HashMap<>();
        this.takenLogins = new HashSet<>();
    }

    public void createUser(String login) {
        if (takenLogins.contains(login)) {
            throw new LoginIsBusyException("User already exists with login = %s".formatted(login));
        }

        takenLogins.add(login);

        var newUser = new User(login, new ArrayList<>());
        userRepository.save(newUser);
        var newAccount = accountService.createAccount(newUser.getLogin());

        newUser.getAccountList().add(newAccount);

        userMap.put(newUser.getId(), newUser);
        userRedisRepository.save(newUser);
    }

    @Cacheable(value = "users", key = "#id")
    public User findUserById(int id) {
        return userRedisRepository.findById(id)
                .orElseGet(() -> userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User with id = %s not found".formatted(id))));
    }

    @Cacheable(value = "users", key = "'allUsers'")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
