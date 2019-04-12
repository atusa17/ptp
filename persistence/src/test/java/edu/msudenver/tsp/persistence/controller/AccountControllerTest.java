package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.Account;
import edu.msudenver.tsp.persistence.exception.BadRequestException;
import edu.msudenver.tsp.persistence.exception.UnprocessableEntityException;
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
    @Mock private AccountsRepository accountsRepository;
    @InjectMocks private AccountController accountController;
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
    public void testGetAccountById() throws BadRequestException {
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

    @Test(expected = BadRequestException.class)
    public void testGetAccountById_nullId() throws BadRequestException {
        accountController.getAccountById(null);
    }

    @Test
    public void testGetAccountById_noAccountFound() throws BadRequestException {
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = accountController.getAccountById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(accountsRepository).findById(anyInt());
    }

    @Test
    public void testGetAccountByUsername() throws BadRequestException {
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

    @Test(expected = BadRequestException.class)
    public void testGetAccountById_nullUsername() throws BadRequestException {
        accountController.getAccountByUsername(null);
    }

    @Test
    public void testGetAccountByUsername_noAccountFound() throws BadRequestException {
        when(accountsRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        final ResponseEntity responseEntity = accountController.getAccountByUsername("Test username");

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(accountsRepository).findByUsername(anyString());
    }

    @Test
    public void testInsertAccount() throws UnprocessableEntityException, BadRequestException {
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
    public void testInsertAccount_usernameAlreadyExists() throws UnprocessableEntityException, BadRequestException {
        final Account account = createAccount();
        when(accountsRepository.findByUsername(anyString())).thenReturn(Optional.of(account));

        final ResponseEntity<Account> responseEntity = accountController.insertAccount(account, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        verify(accountsRepository).findByUsername(anyString());
        verify(accountsRepository, times(0)).save(any(Account.class));
    }

    @Test(expected = BadRequestException.class)
    public void testInsertAccount_accountsDtoIsNull() throws UnprocessableEntityException, BadRequestException {
        final ResponseEntity responseEntity = accountController.insertAccount(null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test(expected = UnprocessableEntityException.class)
    public void testInsertAccount_bindingResultHasErrors() throws UnprocessableEntityException, BadRequestException {
        final Account account = createAccount();
        when(bindingResult.hasErrors()).thenReturn(true);

        accountController.insertAccount(account, bindingResult);
    }

    @Test
    public void testUpdateAccount() throws UnprocessableEntityException, BadRequestException {
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

    @Test(expected = UnprocessableEntityException.class)
    public void testUpdateAccount_bindingResultHasErrors() throws UnprocessableEntityException, BadRequestException {
        when(bindingResult.hasErrors()).thenReturn(true);

        accountController.updateAccount(1, createAccount(), bindingResult);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateAccount_accountsDtoIsNull() throws UnprocessableEntityException, BadRequestException {
        accountController.updateAccount(1, null, bindingResult);
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateAccount_idIsNull() throws UnprocessableEntityException, BadRequestException {
        accountController.updateAccount(null, createAccount(), bindingResult);
    }

    @Test
    public void testUpdateAccount_accountDoesNotExist() throws UnprocessableEntityException, BadRequestException {
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity<Account> responseEntity = accountController.updateAccount(1, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(accountsRepository, times(0)).save(any(Account.class));
    }

    @Test
    public void testDeleteAccountById() throws BadRequestException {
        doNothing().when(accountsRepository).deleteById(anyInt());

        final ResponseEntity responseEntity = accountController.deleteAccountById(1);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(accountsRepository).deleteById(anyInt());
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteAccountById_idIsNull() throws BadRequestException {
        accountController.deleteAccountById(null);
    }

    private Account createAccount() {
        final Account account = new Account();
        account.setUsername("Test username");
        account.setPassword("test password");
        account.setAdministrator(true);

        return account;
    }
}