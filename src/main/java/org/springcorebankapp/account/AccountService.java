package org.springcorebankapp.account;

import org.springcorebankapp.exception.UserNotFoundException;
import org.springcorebankapp.user.User;
import org.springcorebankapp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

/**
 * Service class for managing bank accounts.
 * <p>
 * This class provides business logic for creating, retrieving, updating, and deleting
 * accounts, as well as performing operations such as deposits, withdrawals, and transfers.
 * It interacts with the {@link AccountRepository}, {@link UserRepository}, and {@link AccountProperties}
 * to handle persistence, validation, and application-specific configurations.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Account creation with default balances.</li>
 *     <li>Retrieval of accounts by ID or user ID, with caching support.</li>
 *     <li>Deposit, withdrawal, and transfer functionality with validation and caching.</li>
 *     <li>Account closure logic, ensuring rules are followed before deletion.</li>
 * </ul>
 *
 * <p>
 * Methods use annotations such as {@link Cacheable} and {@link CacheEvict} for
 * caching account-related data, improving application performance.
 * </p>
 *
 * @see Account
 * @see AccountRepository
 * @see AccountProperties
 * @see UserRepository
 * @see org.springframework.cache.annotation.Cacheable
 * @see org.springframework.cache.annotation.CacheEvict
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
@Service
@Transactional
public class AccountService {

    /**
     * Repository for managing {@link Account} entities.
     * <p>
     * Provides CRUD operations and query methods for accessing account data.
     * </p>
     */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Configuration properties for account-related settings.
     * <p>
     * Includes default account balance and transfer commission rate.
     * </p>
     */
    @Autowired
    private AccountProperties accountProperties;

    /**
     * Repository for managing {@link User} entities.
     * <p>
     * Used to validate user existence and retrieve user-related data.
     * </p>
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new account for a user with a default balance.
     *
     * @param login the username of the user for whom the account is to be created
     * @return the created {@link Account} entity
     * @throws UserNotFoundException if no user is found with the provided login
     */
    @CacheEvict(value = "accounts", key = "#login")
    public Account createAccount(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new UserNotFoundException("User with username = %s not found".formatted(login)));

        Account account = new Account(user.getId(), accountProperties.getDefaultAccountAmount());
        accountRepository.save(account);
        return account;
    }

    /**
     * Finds an account by its ID.
     *
     * @param id the ID of the account to retrieve
     * @return the found {@link Account} entity
     * @throws AccountNotFoundException if no account is found with the provided ID
     */
    @Cacheable(value = "accounts", key = "#id")
    public Account findAccountById(int id) throws AccountNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id = %s not found".formatted(id)));
    }

    /**
     * Retrieves all accounts associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of {@link Account} entities owned by the user
     * @throws UserNotFoundException if no accounts are found for the user
     */
    @Cacheable(value = "userAccounts", key = "#userId")
    public List<Account> getAllUserAccounts(int userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = %s not found".formatted(userId)));
    }

    /**
     * Deposits money into a specific account.
     *
     * @param accountId      the ID of the account to deposit money into
     * @param moneyToDeposit the amount of money to deposit
     * @throws AccountNotFoundException if the account is not found
     * @throws IllegalArgumentException if the deposit amount is not positive
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public void depositAccount(int accountId, int moneyToDeposit) throws AccountNotFoundException {
        var account = findAccountById(accountId);
        if(moneyToDeposit <= 0) {
            throw new IllegalArgumentException("Cannot deposit not positive money: amount = %s"
                    .formatted(moneyToDeposit));
        }

        account.setMoneyAmount(account.getMoneyAmount() + moneyToDeposit);
        accountRepository.save(account);
    }

    /**
     * Withdraws money from a specific account.
     *
     * @param accountId        the ID of the account to withdraw money from
     * @param amountToWithdraw the amount of money to withdraw
     * @throws AccountNotFoundException if the account is not found
     * @throws IllegalArgumentException if the withdrawal amount is not positive
     *                                  or exceeds the account's balance
     */
    @CacheEvict(value = "accounts", key = "#accountId")
    public void withdrawFromAccount(int accountId, int amountToWithdraw) throws AccountNotFoundException {
        var account = findAccountById(accountId);

        if(amountToWithdraw <= 0) {
            throw new IllegalArgumentException("Cannot withdraw not positive money: amount = %s"
                    .formatted(amountToWithdraw));
        }
        if(account.getMoneyAmount() < amountToWithdraw) {
            throw new IllegalArgumentException("Cannot withdraw from account: id = %s, moneyAmount = %s, attemptedWithdraw=%s"
                    .formatted(accountId, account.getMoneyAmount(), amountToWithdraw));
        }

        account.setMoneyAmount(account.getMoneyAmount() - amountToWithdraw);

        accountRepository.save(account);
    }

    /**
     * Closes a specific account, transferring its balance to another account if necessary.
     * <p>
     * This method ensures that a user cannot close their only account. If the account
     * has a balance, the funds are transferred to another account owned by the same user.
     * The account is then removed from the system.
     * </p>
     *
     * @param accountId the ID of the account to be closed
     * @throws AccountNotFoundException if the account is not found
     * @throws IllegalArgumentException if the user only has one account, preventing closure
     */
    @CacheEvict(value = {"accounts", "userAccounts"}, allEntries = true)
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

    /**
     * Transfers money from one account to another.
     * <p>
     * This method allows transferring funds between two accounts, either within the same user
     * or between different users. If the transfer is between accounts of different users, a
     * commission is applied to the amount being transferred. Both accounts' balances are updated
     * accordingly, and changes are saved to the database.
     * </p>
     *
     * @param fromAccountId    the ID of the account to transfer money from
     * @param toAccountId      the ID of the account to transfer money to
     * @param amountToTransfer the amount of money to transfer
     * @throws AccountNotFoundException if either of the accounts is not found
     * @throws IllegalArgumentException if the transfer amount is not positive
     *                                  or if the source account's balance is insufficient
     */
    @CacheEvict(value = "accounts", allEntries = true)
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
