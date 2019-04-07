package edu.msudenver.tsp.services;

import com.google.gson.reflect.TypeToken;
import edu.msudenver.tsp.services.dto.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock private RestService restService;
    @InjectMocks private UserService userService;

    @Test
    public void testGetListOfAccounts() {
        final List<Account> accountList = new ArrayList<>();
        accountList.add(createAccount());
        accountList.add(createAccount());

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(), anyString())).thenReturn(Optional.of(accountList));

        final Optional<List<Account>> response = userService.getListOfAccounts();

        assertTrue(response.isPresent());
        assertEquals(accountList, response.get());
        verify(restService).get(anyString(), any(TypeToken.class), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testGetListOfAccounts_PersistenceApiResponseIsEmpty() {
        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString())).thenReturn(Optional.empty());

        final Optional<List<Account>> response = userService.getListOfAccounts();

        assertFalse(response.isPresent());
        verify(restService).get(anyString(), any(TypeToken.class), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testGetListOfAccounts_RestServiceThrowsException() {
        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString())).thenThrow(Exception.class);

        final Optional<List<Account>> response = userService.getListOfAccounts();

        assertFalse(response.isPresent());
        verify(restService).get(anyString(), any(TypeToken.class), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testFindAccountById() {
        final Account account = createAccount();

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString())).thenReturn(Optional.of(account));

        final Optional<Account> response = userService.findAccountById(1);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
        verify(restService).get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString());
    }

    @Test
    public void testFindAccountById_IdEqualsZero() {
        final Optional<Account> response = userService.findAccountById(0);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
        verifyZeroInteractions(restService);
    }

    @Test
    public void testFindAccountById_PersistenceApiResponseIsEmpty() {
        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString())).thenReturn(Optional.empty());

        final Optional<Account> response = userService.findAccountById(1);

        assertFalse(response.isPresent());
        verify(restService).get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString());
    }

    @Test
    public void testFindAccountById_RestServiceThrowsException() {
        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString())).thenThrow(Exception.class);

        final Optional<Account> response = userService.findAccountById(1);

        assertFalse(response.isPresent());
        verify(restService).get(anyString(), any(TypeToken.class), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testFindAccountByUsername() {
        final Account account = createAccount();

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString())).thenReturn(Optional.of(account));

        final Optional<Account> response = userService.findAccountByUsername(account.getUsername());

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
        verify(restService).get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString());
    }

    @Test
    public void testFindAccountByUsername_NullUsername() {
        final Optional<Account> response = userService.findAccountByUsername(null);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
        verifyZeroInteractions(restService);
    }

    @Test
    public void testFindAccountByUsername_PersistenceApiResponseIsEmpty() {
        final Account account = createAccount();

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString())).thenReturn(Optional.empty());

        final Optional<Account> response = userService.findAccountByUsername(account.getUsername());

        assertFalse(response.isPresent());
        verify(restService).get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString());
    }

    @Test
    public void testFindAccountByUsername_RestServiceThrowsException() {
        when(restService.get(anyString(), any(TypeToken.class), anyInt(), anyInt(), anyString())).thenThrow(Exception.class);

        final Optional<Account> response = userService.findAccountByUsername("test");

        assertFalse(response.isPresent());
        verify(restService).get(anyString(), any(TypeToken.class), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testCreateAccount() {
        final Account account = createAccount();

        when(restService.post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt())).thenReturn(Optional.of(account));

        final Optional<Account> response = userService.createAccount(account);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
        verify(restService).post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testCreateAccount_NullAccount() {
        final Optional<Account> response = userService.createAccount(null);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
        verify(restService, times(0)).post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testCreateAccount_AccountCouldNotBeCreated() {
        when(restService.post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt())).thenReturn(Optional.empty());

        final Optional<Account> response = userService.createAccount(createAccount());

        assertFalse(response.isPresent());
        verify(restService).post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testCreateAccount_RestServiceThrowsException() {
        when(restService.post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt())).thenThrow(Exception.class);

        final Optional<Account> response = userService.createAccount(createAccount());

        assertFalse(response.isPresent());
        verify(restService).post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testUpdateAccount() {
        final Account account = createAccount();
        account.setId(1);

        when(restService.patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt())).thenReturn(Optional.of(account));

        final Optional<Account> response = userService.updateAccount(account);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
        verify(restService).patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testUpdateAccount_NullAccount() {
        final Optional<Account> response = userService.updateAccount(null);

        assertFalse(response.isPresent());
        verifyZeroInteractions(restService);
    }

    @Test
    public void testUpdateAccount_IdEqualsZero() {
        final Account account = createAccount();
        account.setId(0);

        final Optional<Account> response = userService.updateAccount(account);

        assertFalse(response.isPresent());
        verifyZeroInteractions(restService);
    }

    @Test
    public void testUpdateAccount_PersistenceApiResponseIsEmpty() {
        final Account account = createAccount();
        account.setId(1);

        when(restService.patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt())).thenReturn(Optional.empty());

        final Optional<Account> response = userService.updateAccount(account);

        assertFalse(response.isPresent());
        verify(restService).patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testUpdateAccount_RestServiceThrowsException() {
        final Account account = createAccount();
        account.setId(1);

        when(restService.patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt())).thenThrow(Exception.class);

        final Optional<Account> response = userService.updateAccount(account);

        assertFalse(response.isPresent());
        verify(restService).patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testDeleteAccount() {
        final Account account = createAccount();
        account.setId(1);

        when(restService.delete(anyString(), anyInt(), anyInt(), any())).thenReturn(true);

        final boolean isDeleteSuccessful = userService.deleteAccount(account);

        assertTrue(isDeleteSuccessful);
        verify(restService).delete(anyString(), anyInt(), anyInt(), any());
    }

    @Test
    public void testDeleteAccount_NullAccount() {
        final boolean isDeleteSuccessful = userService.deleteAccount(null);

        assertFalse(isDeleteSuccessful);
        verifyZeroInteractions(restService);
    }

    @Test
    public void testDeleteAccount_IdEqualsZero() {
        final Account account = createAccount();
        account.setId(0);

        final boolean isDeleteSuccessful = userService.deleteAccount(account);

        assertFalse(isDeleteSuccessful);
        verifyZeroInteractions(restService);
    }

    @Test
    public void testDeleteAccount_PersistenceApiResponseIsEmpty() {
        final Account account = createAccount();
        account.setId(1);

        when(restService.delete(anyString(), anyInt(), anyInt(), any())).thenReturn(false);

        final boolean isDeleteSuccessful = userService.deleteAccount(account);

        assertFalse(isDeleteSuccessful);
        verify(restService).delete(anyString(), anyInt(), anyInt(), any());
    }

    @Test
    public void testDeleteAccount_RestServiceThrowsException() {
        final Account account = createAccount();
        account.setId(1);

        when(restService.delete(anyString(), anyInt(), anyInt(), any())).thenThrow(Exception.class);

        final boolean persistenceApiResponse = userService.deleteAccount(account);

        assertFalse(persistenceApiResponse);
        verify(restService).delete(anyString(), anyInt(), anyInt(), any());
    }


    private Account createAccount() {
        final Account account = new Account();
        account.setUsername("Test username");
        account.setPassword("test password");
        account.setAdministratorStatus(true);
        account.setLastLogin(new Date());
        return account;
    }
}
