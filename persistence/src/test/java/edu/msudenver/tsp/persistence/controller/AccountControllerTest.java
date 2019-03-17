package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Account;
import edu.msudenver.tsp.persistence.repository.AccountsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {
    @Mock
    private AccountsRepository accountsRepository;
    @InjectMocks
    private AccountController accountController;
    @Mock private BindingResult bindingResult;

    @Test
    public void testGetAllAccounts() {
        final Account accountDto = createAccount();
        final List<Account> accountList = new ArrayList<>();
        accountList.add(accountDto);
        accountList.add(accountDto);

        when(accountsRepository.findAll()).thenReturn(accountList);

        final ResponseEntity<Iterable<Account>> responseEntity = accountController.getListOfAccounts();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(account -> assertEquals(accountDto, account));
    }

    @Test
    public void testGetAccountById() {
        final Account account = createAccount();
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.ofNullable(account));

        final ResponseEntity<Account> responseEntity = accountController.getAccountById(1);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(account, responseEntity.getBody());
        verify(accountsRepository).findById(anyInt());
    }

    @Test
    public void testGetAccountById_nullId() {
        final ResponseEntity responseEntity = accountController.getAccountById(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testGetAccountById_noAccountFound() {
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = accountController.getAccountById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(accountsRepository).findById(anyInt());
    }

    @Test
    public void testGetAccountByUsername() {
        final Account account = createAccount();
        when(accountsRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(account));

        final ResponseEntity<Account> responseEntity = accountController.getAccountByUsername("Test username");

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(account, responseEntity.getBody());
        verify(accountsRepository).findByUsername(anyString());
    }

    @Test
    public void testGetAccountById_nullUsername() {
        final ResponseEntity responseEntity = accountController.getAccountByUsername(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testGetAccountByUsername_noAccountFound() {
        when(accountsRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = accountController.getAccountByUsername("Test username");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(accountsRepository).findByUsername(anyString());
    }

    @Test
    public void testInsertAccount() {
        final Account account = createAccount();
        when(accountsRepository.save(any(Account.class))).thenReturn(account);
        when(accountsRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        final ResponseEntity<Account> responseEntity = accountController.insertAccount(account, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(account, responseEntity.getBody());
        verify(accountsRepository).save(any(Account.class));
    }

    @Test
    public void testInsertAccount_usernameAlreadyExists() {
        final Account account = createAccount();
        when(accountsRepository.findByUsername(anyString())).thenReturn(Optional.of(account));

        final ResponseEntity<Account> responseEntity = accountController.insertAccount(account, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        verify(accountsRepository).findByUsername(anyString());
        verify(accountsRepository, times(0)).save(any(Account.class));
    }

    @Test
    public void testInsertAccount_accountsDtoIsNull() {
        final ResponseEntity responseEntity = accountController.insertAccount(null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testInsertAccount_bindingResultHasErrors() {
        final Account account = createAccount();
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity responseEntity = accountController.insertAccount(account, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount() {
        final Account existingAccount = createAccount();
        existingAccount.setId(1);
        existingAccount.setVersion(1);
        final Account accountUpdate = new Account();
        accountUpdate.setUsername("Test Update");
        final Account updatedAccount = existingAccount;
        updatedAccount.setUsername("Test Update");
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.of(existingAccount));
        when(accountsRepository.save(any(Account.class))).thenReturn(updatedAccount);

        final ResponseEntity<Account> responseEntity = accountController.updateAccount(1, accountUpdate, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedAccount, responseEntity.getBody());
        verify(accountsRepository).findById(anyInt());
        verify(accountsRepository).save(any(Account.class));
    }

    @Test
    public void testUpdateAccount_bindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity<Account> responseEntity = accountController.updateAccount(1, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount_accountsDtoIsNull() {
        final ResponseEntity<Account> responseEntity = accountController.updateAccount(1, null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount_idIsNull() {
        final ResponseEntity<Account> responseEntity = accountController.updateAccount(null, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount_accountDoesNotExist() {
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity<Account> responseEntity = accountController.updateAccount(1, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(accountsRepository, times(0)).save(any(Account.class));
    }

    @Test
    public void testDeleteAccountById() {
        doNothing().when(accountsRepository).deleteById(anyInt());

        final ResponseEntity responseEntity = accountController.deleteAccountById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(accountsRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteAccountById_idIsNull() {
        final ResponseEntity responseEntity = accountController.deleteAccountById(null);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    private Account createAccount() {
        final Account account = new Account();
        account.setUsername("Test username");
        account.setPassword("test password");
        account.setAdministratorStatus(true);

        return account;
    }
}