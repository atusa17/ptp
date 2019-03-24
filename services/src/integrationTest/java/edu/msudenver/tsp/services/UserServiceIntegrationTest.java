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
    public void testUserService(){
        final Account testAccount = creatAccount();

        final Optional<Account> testCreatedAccount = userService.createNewAccount(testAccount);

        assertTrue(testCreatedAccount.isPresent());
        final Account returnedAccount = testCreatedAccount.get();
        assertEquals("test user", returnedAccount.getUsername());
        assertEquals("test password", returnedAccount.getPassword());
        assertFalse(returnedAccount.isAdministratorStatus());

        returnedAccount.setUsername("test updatedUser");
        returnedAccount.setPassword("test updatedPassword");

        final Optional<Account> updatedTestCreatedAccount = userService.updateAccount(returnedAccount);

        assertTrue(updatedTestCreatedAccount .isPresent());
        final Account returnedUpdatedAccount = updatedTestCreatedAccount.get();
        assertEquals("test updatedUser", returnedUpdatedAccount.getUsername());
        assertEquals("test updatedPassword", returnedUpdatedAccount.getPassword());
        assertFalse(returnedUpdatedAccount.isAdministratorStatus());

        final boolean result = userService.deleteAccount(returnedUpdatedAccount);

        assertTrue(result);
    }
    private Account creatAccount(){
        final Account testAccount = new Account();
        testAccount.setUsername("test user");
        testAccount.setPassword("test password");
        testAccount.setAdministratorStatus(false);
        testAccount.setLastLogin(new Date());

        return testAccount;
    }

}
