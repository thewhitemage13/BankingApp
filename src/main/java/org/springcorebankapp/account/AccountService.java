package org.springcorebankapp.account;

import org.springcorebankapp.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service // над ней повешена аннотация Component
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    private final Map<Integer, Account> accountMap;
    private final AccountProperties accountProperties;

    public AccountService(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
        this.accountMap = new HashMap<>();
    }

    public Account createAccount(User user) {
        Account account = new Account(user.getId(), accountProperties.getDefaultAccountAmount());
        accountMap.put(account.getId(), account);
        return account;
    }

    // Optional - обертка над каким-то обьектом, которая может
    // Либо содержать обьект, либо его не содержать
    // Удобный вариант вместо того, чтоб возвращать null из метода
    public Optional<Account> findAccountById(int id) {
        return accountRepository.findById(id);
    }

    public List<Account> getAllUserAccounts(int userId) {
        return accountRepository.findByUserId(userId);
    }

    public void depositAccount(int accountId, int moneyToDeposit) {
        var account = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));
        if(moneyToDeposit <= 0) {
            throw new IllegalArgumentException("Cannot deposit not positive money: amount=%s".formatted(moneyToDeposit));
        }

        account.setMoneyAmount(account.getMoneyAmount() + moneyToDeposit);
        accountRepository.save(account);
    }

    public void withdrawFromAccount(int accountId, int amountToWithdraw) {
        var account = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));

        if(amountToWithdraw <= 0) {
            throw new IllegalArgumentException("Cannot withdraw not positive money: amount=%s".formatted(amountToWithdraw));
        }
        if(account.getMoneyAmount() < amountToWithdraw) {
            throw new IllegalArgumentException("Cannot withdraw from account: id=%s, moneyAmount=%s, attemptedWithdraw=%s"
                    .formatted(accountId, account.getMoneyAmount(), amountToWithdraw));
        }

        account.setMoneyAmount(account.getMoneyAmount() - amountToWithdraw);

        accountRepository.save(account);
    }

    public Account closeAccount(int accountId) {
        var accountToRemove = findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));

        List<Account> accountList = getAllUserAccounts(accountToRemove.getUserId());

        if(accountList.size() == 1) {
            throw new IllegalArgumentException("Cannot close the only one account");
        }
        Account accountToDeposit = accountList.stream()
                .filter(it->it.getId() != accountId)
                .findFirst()
                .orElseThrow();
        accountToDeposit.setMoneyAmount(accountToDeposit.getMoneyAmount() + accountToRemove.getMoneyAmount());
        accountMap.remove(accountId);
        accountRepository.delete(accountToRemove);
        return accountToRemove;
    }

    public void transfer(int fromAccountId, int toAccountId, int amountToTransfer) {
        var accountFrom = findAccountById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(fromAccountId)));
        var accountTo = findAccountById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(toAccountId)));

        if(amountToTransfer <= 0) {
            throw new IllegalArgumentException("Cannot transfer not positive money: amount=%s"
                    .formatted(amountToTransfer));
        }
        if(accountFrom.getMoneyAmount() < amountToTransfer) {
            throw new IllegalArgumentException("Cannot transfer from account: id=%s, moneyAmount=%s, attemptedTransfer=%s"
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
