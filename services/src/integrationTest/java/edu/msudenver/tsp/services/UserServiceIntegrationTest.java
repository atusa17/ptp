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
        final Account testAccount = new Account();
        testAccount.setUsername("test user");
        testAccount.setPassword("test password");
        testAccount.setAdministratorStatus(false);
        testAccount.setLastLogin(new Date());

        final Optional<Account> testCreatedAccount = userService.createNewAccount(testAccount);

        assertTrue(testCreatedAccount.isPresent());
        final Account returnedAccount = testCreatedAccount.get();
        assertEquals("test user", returnedAccount.getUsername());
        assertEquals("test password", returnedAccount.getPassword());
        assertFalse(returnedAccount.isAdministratorStatus());

        final Optional<Account> updatePasswordTestCreatedAccount = userService.updatePassword(returnedAccount, "password");

        assertTrue(updatePasswordTestCreatedAccount.isPresent());
        final Account returnedUpdatedPasswordAccount = updatePasswordTestCreatedAccount.get();
        assertEquals("test user", returnedUpdatedPasswordAccount.getUsername());
        assertEquals("password", returnedUpdatedPasswordAccount.getPassword());
        assertFalse(returnedAccount.isAdministratorStatus());

        final Optional<Account> updateUsernameTestCreatedAccount = userService.updateUsername(returnedUpdatedPasswordAccount, "user");

        assertTrue(updateUsernameTestCreatedAccount.isPresent());
        final Account returnedUpdatedUsernameAccount = updateUsernameTestCreatedAccount.get();
        assertEquals("user", returnedUpdatedUsernameAccount.getUsername());
        assertEquals("password", returnedUpdatedUsernameAccount.getPassword());
        assertFalse(returnedAccount.isAdministratorStatus());

        final boolean result = userService.deleteAccount(returnedUpdatedUsernameAccount);

        assertTrue(result);
    }

}
