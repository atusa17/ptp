package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.AccountDto;
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
        final AccountDto accountDto = createAccount();
        final List<AccountDto> accountDtoList = new ArrayList<>();
        accountDtoList.add(accountDto);
        accountDtoList.add(accountDto);

        when(accountsRepository.findAll()).thenReturn(accountDtoList);

        final ResponseEntity<Iterable<AccountDto>> responseEntity = accountController.getListOfAccounts();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(account -> assertEquals(accountDto, account));
    }

    @Test
    public void testGetAccountById() {
        final AccountDto accountDto = createAccount();
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.ofNullable(accountDto));

        final ResponseEntity<AccountDto> responseEntity = accountController.getAccountById(1);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accountDto, responseEntity.getBody());
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
        final AccountDto accountDto = createAccount();
        when(accountsRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(accountDto));

        final ResponseEntity<AccountDto> responseEntity = accountController.getAccountByUsername("Test username");

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accountDto, responseEntity.getBody());
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
        final AccountDto accountDto = createAccount();
        when(accountsRepository.save(any(AccountDto.class))).thenReturn(accountDto);
        when(accountsRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        final ResponseEntity<AccountDto> responseEntity = accountController.insertAccount(accountDto, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(accountDto, responseEntity.getBody());
        verify(accountsRepository).save(any(AccountDto.class));
    }

    @Test
    public void testInsertAccount_usernameAlreadyExists() {
        final AccountDto accountDto = createAccount();
        when(accountsRepository.findByUsername(anyString())).thenReturn(Optional.of(accountDto));

        final ResponseEntity<AccountDto> responseEntity = accountController.insertAccount(accountDto, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        verify(accountsRepository).findByUsername(anyString());
        verify(accountsRepository, times(0)).save(any(AccountDto.class));
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
        final AccountDto accountDto = createAccount();
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity responseEntity = accountController.insertAccount(accountDto, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount() {
        final AccountDto existingAccount = createAccount();
        existingAccount.setId(1);
        existingAccount.setVersion(1);
        final AccountDto accountUpdate = new AccountDto();
        accountUpdate.setUsername("Test Update");
        final AccountDto updatedAccount = existingAccount;
        updatedAccount.setUsername("Test Update");
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.of(existingAccount));
        when(accountsRepository.save(any(AccountDto.class))).thenReturn(updatedAccount);

        final ResponseEntity<AccountDto> responseEntity = accountController.updateAccount(1, accountUpdate, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedAccount, responseEntity.getBody());
        verify(accountsRepository).findById(anyInt());
        verify(accountsRepository).save(any(AccountDto.class));
    }

    @Test
    public void testUpdateAccount_bindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity<AccountDto> responseEntity = accountController.updateAccount(1, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount_accountsDtoIsNull() {
        final ResponseEntity<AccountDto> responseEntity = accountController.updateAccount(1, null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount_idIsNull() {
        final ResponseEntity<AccountDto> responseEntity = accountController.updateAccount(null, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount_accountDoesNotExist() {
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity<AccountDto> responseEntity = accountController.updateAccount(1, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(accountsRepository, times(0)).save(any(AccountDto.class));
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

    private AccountDto createAccount() {
        final AccountDto accountDto = new AccountDto();
        accountDto.setUsername("Test username");
        accountDto.setPassword("test password");
        accountDto.setAdministratorStatus(true);

        return accountDto;
    }
}