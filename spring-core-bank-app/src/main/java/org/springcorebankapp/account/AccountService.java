package org.springcorebankapp.account;

import org.springcorebankapp.exception.UserNotFoundException;
import org.springcorebankapp.user.User;
import org.springcorebankapp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    private final AccountProperties accountProperties;
    @Autowired
    private UserRepository userRepository;

    public AccountService(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
    }

    public Account createAccount(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with username = %s not found".formatted(login)));

        Account account = new Account(user.getId(), accountProperties.getDefaultAccountAmount());
        accountRepository.save(account);
        return account;
    }

    public Account findAccountById(int id) throws AccountNotFoundException {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with id = %s not found".formatted(id)));
    }

    public List<Account> getAllUserAccounts(int userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = %s not found".formatted(userId)));
    }

    public void depositAccount(int accountId, int moneyToDeposit) throws AccountNotFoundException {
        var account = findAccountById(accountId);
        if(moneyToDeposit <= 0) {
            throw new IllegalArgumentException("Cannot deposit not positive money: amount = %s".formatted(moneyToDeposit));
        }

        account.setMoneyAmount(account.getMoneyAmount() + moneyToDeposit);
        accountRepository.save(account);
    }

    public void withdrawFromAccount(int accountId, int amountToWithdraw) throws AccountNotFoundException {
        var account = findAccountById(accountId);

        if(amountToWithdraw <= 0) {
            throw new IllegalArgumentException("Cannot withdraw not positive money: amount = %s".formatted(amountToWithdraw));
        }
        if(account.getMoneyAmount() < amountToWithdraw) {
            throw new IllegalArgumentException("Cannot withdraw from account: id = %s, moneyAmount = %s, attemptedWithdraw=%s"
                    .formatted(accountId, account.getMoneyAmount(), amountToWithdraw));
        }

        account.setMoneyAmount(account.getMoneyAmount() - amountToWithdraw);

        accountRepository.save(account);
    }

    public void closeAccount(int accountId) throws AccountNotFoundException {
        var accountToRemove = findAccountById(accountId);

        List<Account> accountList = getAllUserAccounts(accountToRemove.getUserId());

        if(accountList.size() == 1) {
            throw new IllegalArgumentException("Cannot close the only one account");
        }
        Account accountToDeposit = accountList.stream()
                .filter(it->it.getId() != accountId)
                .findFirst()
                .orElseThrow();
        accountToDeposit.setMoneyAmount(accountToDeposit.getMoneyAmount() + accountToRemove.getMoneyAmount());
        accountRepository.delete(accountToRemove);
    }

    public void transfer(int fromAccountId, int toAccountId, int amountToTransfer) throws AccountNotFoundException {
        var accountFrom = findAccountById(fromAccountId);
        var accountTo = findAccountById(toAccountId);

        if(amountToTransfer <= 0) {
            throw new IllegalArgumentException("Cannot transfer not positive money: amount = %s"
                    .formatted(amountToTransfer));
        }
        if(accountFrom.getMoneyAmount() < amountToTransfer) {
            throw new IllegalArgumentException("Cannot transfer from account: id = %s, moneyAmount= %s, attemptedTransfer = %s"
                    .formatted(accountFrom, accountFrom.getMoneyAmount(), amountToTransfer));
        }

        int totalAmountToDeposit = accountTo.getUserId() != accountFrom.getUserId()
                ? (int) (amountToTransfer * (1 - accountProperties.getTransferCommission()))
                : amountToTransfer;
        accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amountToTransfer);
        accountTo.setMoneyAmount(accountTo.getMoneyAmount() + totalAmountToDeposit);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
    }
}
