package edu.msudenver.tsp.persistence;

import edu.msudenver.tsp.persistence.dto.AccountDto;
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
        final AccountDto accountDto = createAccount();
        final AccountDto savedAccount = accountsRepository.save(accountDto);

        assertNotNull(savedAccount);
        assertEquals(Integer.valueOf(0), savedAccount.getVersion());

        final int id = savedAccount.getId();

        assertEquals("Test username", savedAccount.getUsername());
        assertEquals("test password", savedAccount.getPassword());
        assertTrue(savedAccount.getAdministratorStatus());

        savedAccount.setPassword("Test Update");

        final AccountDto updatedAccount = accountsRepository.save(savedAccount);

        assertEquals("Test username", savedAccount.getUsername());
        assertEquals("Test Update", savedAccount.getPassword());
        assertTrue(savedAccount.getAdministratorStatus());
        assertEquals(updatedAccount.getId(), id);

        accountsRepository.delete(accountDto);
        final Optional<AccountDto> deletedAccount = accountsRepository.findById(id);
        assertFalse(deletedAccount.isPresent());
    }

    private AccountDto createAccount() {
        final AccountDto accountDto = new AccountDto();
        accountDto.setUsername("Test username");
        accountDto.setPassword("test password");
        accountDto.setAdministratorStatus(true);

        return accountDto;
    }
    
}
