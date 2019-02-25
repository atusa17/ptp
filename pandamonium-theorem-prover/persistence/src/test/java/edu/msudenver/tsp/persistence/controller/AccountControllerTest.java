package edu.msudenver.tsp.persistence.controller;

import edu.msudenver.tsp.persistence.dto.AccountsDto;
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
        final AccountsDto accountsDto = createAccount();
        final List<AccountsDto> accountsDtoList = new ArrayList<>();
        accountsDtoList.add(accountsDto);
        accountsDtoList.add(accountsDto);

        when(accountsRepository.findAll()).thenReturn(accountsDtoList);

        final ResponseEntity<Iterable<AccountsDto>> responseEntity = accountController.getListOfAccounts();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());

        responseEntity.getBody().forEach(account -> assertEquals(account, accountsDto));
    }

    @Test
    public void testGetAccountById() {
        final AccountsDto accountsDto = createAccount();
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.ofNullable(accountsDto));

        final ResponseEntity<AccountsDto> responseEntity = accountController.getAccountById(1);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accountsDto, responseEntity.getBody());
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
    public void testInsertAccount() {
        final AccountsDto accountsDto = createAccount();
        when(accountsRepository.save(any(AccountsDto.class))).thenReturn(accountsDto);

        final ResponseEntity<AccountsDto> responseEntity = accountController.insertAccount(accountsDto, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(accountsDto, responseEntity.getBody());
        verify(accountsRepository).save(any(AccountsDto.class));
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
        final AccountsDto definitionDto = createAccount();
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity responseEntity = accountController.insertAccount(definitionDto, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount() {
        final AccountsDto existingAccount = createAccount();
        existingAccount.setId(1);
        existingAccount.setVersion(1);
        final AccountsDto accountUpdate = new AccountsDto();
        accountUpdate.setUsername("Test Update");
        final AccountsDto updatedAccount = existingAccount;
        updatedAccount.setUsername("Test Update");
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.of(existingAccount));
        when(accountsRepository.save(any(AccountsDto.class))).thenReturn(updatedAccount);

        final ResponseEntity<AccountsDto> responseEntity = accountController.updateAccount(1, accountUpdate, bindingResult);

        assertNotNull(responseEntity);
        assertTrue(responseEntity.hasBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedAccount, responseEntity.getBody());
        verify(accountsRepository).findById(anyInt());
        verify(accountsRepository).save(any(AccountsDto.class));
    }

    @Test
    public void testUpdateAccount_bindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        final ResponseEntity<AccountsDto> responseEntity = accountController.updateAccount(1, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount_accountsDtoIsNull() {
        final ResponseEntity<AccountsDto> responseEntity = accountController.updateAccount(1, null, bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount_idIsNull() {
        final ResponseEntity<AccountsDto> responseEntity = accountController.updateAccount(null, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyZeroInteractions(accountsRepository);
    }

    @Test
    public void testUpdateAccount_accountDoesNotExist() {
        when(accountsRepository.findById(anyInt())).thenReturn(Optional.empty());

        final ResponseEntity<AccountsDto> responseEntity = accountController.updateAccount(1, createAccount(), bindingResult);

        assertNotNull(responseEntity);
        assertFalse(responseEntity.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(accountsRepository, times(0)).save(any(AccountsDto.class));
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

    private AccountsDto createAccount() {
        final AccountsDto accountsDto = new AccountsDto();
        accountsDto.setUsername("Test username");
        accountsDto.setPassword("test password");
        accountsDto.setAdministratorStatus(true);

        return accountsDto;
    }
}