package edu.msudenver.tsp.services;

import edu.msudenver.tsp.services.dto.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServicesTestConfig.class)
@TestPropertySource("classpath:test.properties")
public class UserServiceIntegrationTest {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Test
    public void testCRUD() {
        final Account testAccount = createAccount();

        final Optional<Account> testCreatedAccount = userService.createAccount(testAccount);
        assertTrue(testCreatedAccount.isPresent());
        final Account returnedAccount = testCreatedAccount.get();
        assertEquals("test_user", returnedAccount.getUsername());
        assertEquals("test_password", returnedAccount.getPassword());
        assertFalse(returnedAccount.isAdministrator());

        final Optional<Account> getAccountById = userService.findAccountById(returnedAccount.getId());
        assertTrue(getAccountById.isPresent());
        final Account returnedAccountById = getAccountById.get();
        assertEquals("test_user", returnedAccountById.getUsername());
        assertEquals("test_password", returnedAccountById.getPassword());
        assertFalse(returnedAccountById.isAdministrator());

        final Optional<Account> getAccountByUsername = userService.findAccountByUsername(returnedAccount.getUsername());
        assertTrue(getAccountByUsername.isPresent());
        final Account returnedAccountByUsername = getAccountByUsername.get();
        assertEquals("test_user", returnedAccountByUsername.getUsername());
        assertEquals("test_password", returnedAccountByUsername.getPassword());
        assertFalse(returnedAccountById.isAdministrator());

        returnedAccount.setUsername("test_updatedUser");
        returnedAccount.setPassword("test_updatedPassword");

        final Optional<Account> updatedAccount = userService.updateAccount(returnedAccount);
        assertTrue(updatedAccount .isPresent());
        final Account returnedUpdatedAccount = updatedAccount.get();
        assertEquals("test_updatedUser", returnedUpdatedAccount.getUsername());
        assertEquals("test_updatedPassword", returnedUpdatedAccount.getPassword());
        assertFalse(returnedUpdatedAccount.isAdministrator());

        final boolean result = userService.deleteAccount(returnedUpdatedAccount);
        assertTrue(result);
    }

    private Account createAccount() {
        final Account testAccount = new Account();
        testAccount.setUsername("test_user");
        testAccount.setPassword("test_password");
        testAccount.setAdministrator(false);
        testAccount.setLastLogin(new Date());

        return testAccount;
    }

}
