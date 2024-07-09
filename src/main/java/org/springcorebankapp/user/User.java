package org.springcorebankapp.user;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springcorebankapp.account.Account;

import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "login", nullable = false, unique = true)
    private String login;
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Account> accountList;

    public User(String login, List<Account> accountList) {
        this.login = login;
        this.accountList = accountList;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accountList=" + accountList +
                '}';
    }
}
