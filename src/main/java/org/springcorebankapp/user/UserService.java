
package org.springcorebankapp.user;

import org.springcorebankapp.account.AccountRepository;
import org.springcorebankapp.account.AccountService;
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

    public User createUser(String login) {

        if (takenLogins.contains(login)) {
            throw new IllegalArgumentException("User already exists with login=%s".formatted(login));
        }

        takenLogins.add(login);

        var newUser = new User(login, new ArrayList<>());
        userRepository.save(newUser);
        var newAccount = accountService.createAccount(newUser);

        newUser.getAccountList().add(newAccount);

        userMap.put(newUser.getId(), newUser);

        return newUser;
    }

    public Optional<User> findUserById(int id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
