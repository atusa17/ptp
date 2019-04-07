package edu.msudenver.tsp.persistence;

import edu.msudenver.tsp.persistence.dto.Account;
import edu.msudenver.tsp.persistence.repository.AccountsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class AccountsIntegrationTest {
    @Autowired private AccountsRepository accountsRepository;

    @Test
    public void testCRUDFunctionality() {
        final Account account = createAccount();
        final Account savedAccount = accountsRepository.save(account);

        assertNotNull(savedAccount);
        assertEquals(Integer.valueOf(0), savedAccount.getVersion());

        final int id = savedAccount.getId();

        assertEquals("Test username", savedAccount.getUsername());
        assertEquals("test password", savedAccount.getPassword());
        assertTrue(savedAccount.isAdministrator());

        savedAccount.setPassword("Test Update");

        final Account updatedAccount = accountsRepository.save(savedAccount);

        assertEquals("Test username", savedAccount.getUsername());
        assertEquals("Test Update", savedAccount.getPassword());
        assertTrue(savedAccount.isAdministrator());
        assertEquals(updatedAccount.getId(), id);

        accountsRepository.delete(account);
        final Optional<Account> deletedAccount = accountsRepository.findById(id);
        assertFalse(deletedAccount.isPresent());
    }

    private Account createAccount() {
        final Account account = new Account();
        account.setUsername("Test username");
        account.setPassword("test password");
        account.setAdministrator(true);

        return account;
    }
    
}
