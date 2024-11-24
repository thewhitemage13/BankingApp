
package org.springcorebankapp.user;

import org.springcorebankapp.account.AccountService;
import org.springcorebankapp.exception.LoginIsBusyException;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AccountService accountService;

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
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id = %s not found".formatted(id)));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
