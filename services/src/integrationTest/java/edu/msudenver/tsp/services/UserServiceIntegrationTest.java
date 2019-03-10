package edu.msudenver.tsp.services;

import edu.msudenver.tsp.services.dto.Account;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServicesTestConfig.class)
@TestPropertySource("classpath:test.properties")
public class UserServiceIntegrationTest {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Test
    public void testCreateNewUser() throws ParseException {
        final Account testAccount = new Account();
        testAccount.setUsername("test user");
        testAccount.setPassword("test password");
        testAccount.setAdministratorStatus(false);
        testAccount.setLastLogin(new Date());

        final Optional<Account> testCreatedAccount = userService.createNewAccount(testAccount);

        assertTrue(testCreatedAccount.isPresent());
        final Account returnedAccount = testCreatedAccount.get();
        Assert.assertEquals("test user", returnedAccount.getUsername());
        Assert.assertEquals("test password", returnedAccount.getPassword());
        Assert.assertEquals(false, returnedAccount.isAdministratorStatus());
    }
}
