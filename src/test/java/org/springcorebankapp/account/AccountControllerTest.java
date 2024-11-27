package org.springcorebankapp.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springcorebankapp.exception.AccountNotFoundException;
import org.springcorebankapp.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    private AccountService accountService;
    @InjectMocks
    private AccountController accountController;

    @Test
    void handleCreateAccount_ReturnsValidResponseEntity() {
        // given
        String login = "login 1";

        // when
        var response = this.accountController.createAccount(login);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account for user with login = %s created".formatted(login), response.getBody());
        verify(this.accountService).createAccount(login);
    }

    @Test
    void handleCreateAccount_UserNotFound() {
        String login = "login 1";

        Mockito
                .doThrow(new UserNotFoundException("User with login = %s not found"
                        .formatted(login))).when(this.accountService).createAccount(login);

        var response = this.accountController.createAccount(login);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with login = %s not found".formatted(login), response.getBody());
    }

    @Test
    void handleCreateAccount_InternalServerError() {
        String login = "login 1";

        Mockito.doThrow(new RuntimeException("Internal Server Error")).when(this.accountService).createAccount(login);
        var response = this.accountController.createAccount(login);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody());
    }


    @Test
    void handleFindAccountById_ReturnsValidResponseEntity() throws Exception {
        int accountId = 1;
        Account account1 = new Account();
        account1.setId(accountId);
        account1.setUserId(2);
        account1.setMoneyAmount(1000);

        Mockito.doReturn(account1).when(this.accountService).findAccountById(accountId);

        var response = this.accountController.findAccountById(accountId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(account1, response.getBody());
    }

    @Test
    void handleFindAccountById_AccountNotFound() throws Exception {
        int accountId = 1;

        Mockito.doThrow(new AccountNotFoundException()).when(this.accountService).findAccountById(accountId);
        var response = this.accountController.findAccountById(accountId);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleFindAccountById_InternalServerError() throws Exception {
        int accountId = 1;

        Mockito.doThrow(new RuntimeException("Unexpected error")).when(this.accountService).findAccountById(accountId);

        var response = this.accountController.findAccountById(accountId);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void depositAccount_Success() throws Exception {
        int accountId = 1;
        int amount = 2000;

        var response = this.accountController.depositAccount(accountId, amount);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account deposited successfully", response.getBody());
        verify(accountService).depositAccount(accountId, amount);
    }

    @Test
    void depositAccount_InternalServerError() throws Exception {
        int accountId = 1;
        int amount = 2000;

        Mockito.doThrow(new RuntimeException("Unexpected error")).when(this.accountService).depositAccount(accountId, amount);
        var response = this.accountController.depositAccount(accountId, amount);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error" ,response.getBody());
    }

    @Test
    void withdrawAccount_Success() throws Exception {
        int accountId = 1;
        int amount = 50;

        var response = this.accountController.withdrawAccount(accountId, amount);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account withdrawn successfully", response.getBody());
        verify(accountService).withdrawFromAccount(accountId, amount);
    }

    @Test
    void withdrawAccount_InternalServerError() throws Exception {
        int accountId = 1;
        int amount = 50;

        Mockito.doThrow(new RuntimeException("Unexpected error")).when(this.accountService).withdrawFromAccount(accountId, amount);

        var response = this.accountController.withdrawAccount(accountId, amount);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error" ,response.getBody());
    }

    @Test
    void deleteAccount_Success() throws Exception {
        int accountId = 1;

        var response = accountController.deleteAccount(accountId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account closed successfully", response.getBody());
        verify(accountService).closeAccount(accountId);
    }

    @Test
    void deleteAccount_InternalServerError() throws Exception{
        int accountId = 1;

        Mockito.doThrow(new RuntimeException("Unexpected error")).when(accountService).closeAccount(accountId);

        var response = accountController.deleteAccount(accountId);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void transfer_Success() throws Exception {
        int fromAccountId = 1;
        int toAccountId = 2;
        int amountToTransfer = 100;

        var response = accountController.transfer(fromAccountId, toAccountId, amountToTransfer);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account transfer successfully", response.getBody());
        verify(accountService).transfer(fromAccountId, toAccountId, amountToTransfer);
    }

    @Test
    void transfer_InternalServerError() throws Exception{
        int fromAccountId = 1;
        int toAccountId = 2;
        int amountToTransfer = 100;

        Mockito.doThrow(new RuntimeException("Unexpected error")).when(accountService).transfer(fromAccountId, toAccountId, amountToTransfer);

        var response = accountController.transfer(fromAccountId, toAccountId, amountToTransfer);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody());
    }
}