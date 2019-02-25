package edu.msudenver.tsp.persistence;

import edu.msudenver.tsp.persistence.repository.AccountsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class AccountsIntegrationTest {
    @Autowired private AccountsRepository accountsRepository;

    @Test
    public void testCRUDFunctionality() {
        final AccountsDto accountsDto = createAccount();
        final AccountsDto savedAccount = accountsRepository.save(accountsDto);

        assertNotNull(savedAccount);
        assertEquals(Integer.valueOf(0), savedAccount.getVersion());

        final int id = savedAccount.getId();

        assertEquals("Test username", savedAccount.getUsername());
        assertEquals("test password", savedAccount.getPassword());
        assertTrue(savedAccount.isAdministratorStatus());
        assertEquals(1, savedAccount.getVersion());

        savedAccount.setPassword("Test Update");

        final AccountsDto updatedAccount = accountsRepository.save(savedAccount);

        assertEquals("Test username", savedAccount.getUsername());
        assertEquals("Test Update", savedAccount.getPassword());
        assertTrue(savedAccount.isAdministratorStatus());
        assertEquals(2, savedAccount.getVersion());
        assertEquals(updatedAccount.getId(), id);

        accountsRepository.deleteById(id);
        final Optional<AccountsDto> deletedAccount = accountsRepository.findById(id);
        assertFalse(deletedDefinition.isPresent());
    }

    private AccountsDto createAccount() {
        final AccountsDto accountsDto = new AccountsDto();
        accountsDto.setUsername("Test username");
        accountsDto.setPassword("test password");
        accountsDto.setAdministratorStatus(true);

        return accountsDto;
    }
    
}
