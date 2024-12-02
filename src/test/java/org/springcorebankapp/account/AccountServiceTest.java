package org.springcorebankapp.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springcorebankapp.user.User;
import org.springcorebankapp.user.UserRepository;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountProperties accountProperties;
    @InjectMocks
    private AccountService accountService;

    // create account

    @Test
    void handleCreateAccount_ReturnsAccount() {
        // given
        String login = "test 1";
        User user = new User();
        user.setLogin(login);
        user.setId(1);

        Account account = new Account(user.getId(),0);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // when

        Account resul = accountService.createAccount(login);

        // then

        assertNotNull(resul);
        assertEquals(account.getId(), resul.getId());
        verify(accountRepository).save(account);

    }

    @Test
    void handleCreateAccount_UserNotFound() {
        // given

        String login = "test 1";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        // when / then
        assertThrows(UserNotFoundException.class, () -> accountService.createAccount(login));
        verify(accountRepository, never()).save(any(Account.class));
    }

    // find account by id

    @Test
    void handleFindAccountById_ReturnsAccount() throws Exception {
        // given
        int accountId = 1;
        Account account = new Account(accountId,0);
        account.setMoneyAmount(500);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // when
        Account result = accountService.findAccountById(accountId);

        // then
        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        assertEquals(500, result.getMoneyAmount());
    }

    @Test
    void handleFindAccountById_AccountNotFound() {
        // given
        int accountId = 1;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(AccountNotFoundException.class, () -> accountService.findAccountById(accountId));
    }

    // get all users

    @Test
    void handleGetAllUserAccounts_ReturnsAllUserAccounts() {
        // given
        int userId = 1;
        Account account = new Account(userId,100);
        Account account2 = new Account(userId,200);

        List<Account> accounts = List.of(account,account2);

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(accounts));

        // when
        List<Account> result = accountService.getAllUserAccounts(userId);

        // then
        assertNotNull(result);
        assertEquals(accounts.size(), result.size());
    }

    @Test
    void handleGetAllUserAccounts_UserNotFound() {
        // given
        int userId = 1;
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(UserNotFoundException.class, () -> accountService.getAllUserAccounts(userId));
    }

    // deposit account

    @Test
    void depositAccount_Success() throws Exception {
        // given
        int accountId = 1;
        int depositAmount = 100;
        Account account = new Account();

        account.setId(accountId);
        account.setMoneyAmount(1000);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // when
        accountService.depositAccount(accountId, depositAmount);

        // then
        assertEquals(1100, account.getMoneyAmount());
        verify(accountRepository).save(account);
    }

    //

    @Test
    void depositAccount_InvalidAmount() {
        // given
        int accountId = 1;
        int moneyToDeposit = -100;
        Account account = new Account();
        account.setId(accountId);
        account.setUserId(1);
        account.setMoneyAmount(1000);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // when / then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.depositAccount(accountId, moneyToDeposit)
        );

        assertEquals("Cannot deposit not positive money: amount = -100", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

//    @Test
//    void closeAccount_Success() throws Exception {
//        // given
//        int accountIdToClose = 1;
//        Account accountToClose = new Account(1, 500);  // Аккаунт, который будем закрывать
//        accountToClose.setId(accountIdToClose);
//        Account accountToDeposit = new Account(1, 1000); // Аккаунт, на который будем переводить средства
//        accountToDeposit.setId(2);
//
//        // Мокаем поведение findAccountById и getAllUserAccounts
//        when(accountRepository.findById(accountIdToClose)).thenReturn(Optional.of(accountToClose));
//        when(accountRepository.findById(2)).thenReturn(Optional.of(accountToDeposit));
//        when(accountRepository.findByUserId(1)).thenReturn(Optional.of(List.of(accountToClose, accountToDeposit)));
//
//        // when
//        accountService.closeAccount(accountIdToClose);
//
//        // then
//        // Проверяем, что средства были переведены на другой аккаунт (1000 + 500 = 1500)
//        assertEquals(1500, accountToDeposit.getMoneyAmount());
//        // Проверяем, что аккаунт был удален
//        verify(accountRepository).delete(accountToClose);
//        verify(accountRepository).save(accountToDeposit); // Проверяем, что обновление суммы на целевом аккаунте произошло
//    }

    @Test
    void closeAccount_OnlyOneAccount() {
        // given
        int accountIdToClose = 1;
        Account accountToClose = new Account(1, 500);
        accountToClose.setId(accountIdToClose);

        when(accountRepository.findById(accountIdToClose)).thenReturn(Optional.of(accountToClose));
        when(accountRepository.findByUserId(1)).thenReturn(Optional.of(List.of(accountToClose)));

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.closeAccount(accountIdToClose));

        assertEquals("Cannot close the only one account", exception.getMessage());
        verify(accountRepository, never()).delete(accountToClose);
    }

    // withdraw

    @Test
    void withdrawFromAccount_Success() throws AccountNotFoundException {
        // given
        int accountId = 1;
        int amountToWithdraw = 500;
        Account account = new Account(1, 1000);
        account.setId(accountId);

        // Мокаем поведение findAccountById
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // when
        accountService.withdrawFromAccount(accountId, amountToWithdraw);

        // then
        assertEquals(500, account.getMoneyAmount());
        verify(accountRepository).save(account);
    }

    @Test
    void withdrawFromAccount_InsufficientFunds() {
        // given
        int accountId = 1;
        int amountToWithdraw = 1500;
        Account account = new Account(1, 1000);
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.withdrawFromAccount(accountId, amountToWithdraw));

        assertEquals("Cannot withdraw from account: id = 1, moneyAmount = 1000, attemptedWithdraw=1500", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void withdrawFromAccount_NonPositiveAmount() {
        // given
        int accountId = 1;
        int amountToWithdraw = -500;
        Account account = new Account(1, 1000);
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.withdrawFromAccount(accountId, amountToWithdraw));

        assertEquals("Cannot withdraw not positive money: amount = -500", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    // transfer

    @Test
    void transfer_Success() throws AccountNotFoundException {
        // given
        int fromAccountId = 1;
        int toAccountId = 2;
        int amountToTransfer = 500;
        Account fromAccount = new Account(1, 1000);
        fromAccount.setId(fromAccountId);
        Account toAccount = new Account(2, 2000);
        toAccount.setId(toAccountId);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        // when
        accountService.transfer(fromAccountId, toAccountId, amountToTransfer);

        // then
        assertEquals(500, fromAccount.getMoneyAmount());
        assertEquals(2500, toAccount.getMoneyAmount());
        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }

    @Test
    void transfer_InsufficientFunds() {
        // given
        int fromAccountId = 1;
        int toAccountId = 2;
        int amountToTransfer = 1500;
        Account fromAccount = new Account(1, 1000);
        fromAccount.setId(fromAccountId);
        Account toAccount = new Account(2, 2000);
        toAccount.setId(toAccountId);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.transfer(fromAccountId, toAccountId, amountToTransfer));

        assertEquals("Cannot transfer from account: id = Account{id=1, userId=1, moneyAmount=1000}, moneyAmount= 1000, attemptedTransfer = 1500", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void transfer_NonPositiveAmount() {
        // given
        int fromAccountId = 1;
        int toAccountId = 2;
        int amountToTransfer = -500;
        Account fromAccount = new Account(1, 1000);
        fromAccount.setId(fromAccountId);
        Account toAccount = new Account(2, 2000);
        toAccount.setId(toAccountId);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.transfer(fromAccountId, toAccountId, amountToTransfer));

        assertEquals("Cannot transfer not positive money: amount = -500", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }
}