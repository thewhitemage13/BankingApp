package org.springcorebankapp.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    public void testGetAndSetLogin() {
        user.setLogin("login 1");
        assertEquals("login 1", user.getLogin());
    }

    @Test
    public void testGetAndSetId() {
        user.setId(1);
        assertEquals(1, user.getId());
    }
}