package org.springcorebankapp.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
    }

    @Test
    void testGetAndSetAccountId() {
        account.setId(1);
        assertEquals(1, account.getId());
    }

    @Test
    void testGetAndSetMoneyAmount() {
        account.setMoneyAmount(1000);
        assertEquals(1000, account.getMoneyAmount());
    }

    @Test
    void testGetAndSetUserId() {
        account.setUserId(1);
        assertEquals(1, account.getUserId());
    }
}