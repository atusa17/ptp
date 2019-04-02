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
    public void testGetListOfAccounts(){
        final Account account1 = createAccount();
        final Account account2 = createAccount();
        final List<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenReturn(Optional.of(accountList));
        final Optional<Account> response = userService.getListOfAccount();

        assertTrue(response.isPresent());
        assertEquals(accountList, response.get());
    }

    @Test
    public void testGetListOfAccounts_persistenceApiResponseIsNotPresent(){
        final Account account1 = createAccount();
        final Account account2 = createAccount();
        final List<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenReturn(Optional.empty());
        final Optional<Account> response = userService.getListOfAccount();

        assertFalse(response.isPresent());
    }

    @Test
    public void testGetListOfAccounts_catchException(){
        final Account account1 = createAccount();
        final Account account2 = createAccount();
        final List<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);
        final Exception e = new Exception();

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenThrow(Exception.class);
        final Optional<Account> response = userService.getListOfAccount();

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
    }

    @Test
    public void testGetAccountById(){
        final Account account = createAccount();

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenReturn(Optional.of(account));
        final Optional<Account> response = userService.getAccountById(1);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
        verify(restService).get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString());
    }

    @Test
    public void testGetAccountById_nullId() {
        final Optional<Account> response = userService.getAccountById(0);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
        verifyZeroInteractions(restService);
    }

    @Test
    public void testGetAccountById_persistenceApiResponseIsNotPresent(){

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenReturn(Optional.empty());
        final Optional<Account> response = userService.getAccountById(1);

        assertFalse(response.isPresent());
        verify(restService).get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString());
    }

    @Test
    public void testGetAccountById_catchException(){

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenThrow(Exception.class);
        final Optional<Account> response = userService.getAccountById(1);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
    }

    @Test
    public void testGetAccountByUsername(){
        final Account account = createAccount();

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenReturn(Optional.of(account));
        final Optional<Account> response = userService.getAccountByUsername(account.getUsername());

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
        verify(restService).get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString());
    }

    @Test
    public void testGetAccountByUsername_nullUsername(){
        final Optional<Account> response = userService.getAccountByUsername(null);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
        verifyZeroInteractions(restService);
    }

    @Test
    public void testGetAccountByUsername_persistenceApiResponseIsNotPresent(){
        final Account account = createAccount();

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenReturn(Optional.empty());
        final Optional<Account> response = userService.getAccountByUsername(account.getUsername());

        assertFalse(response.isPresent());
        verify(restService).get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString());
    }

    @Test
    public void testCreateAccount(){
        final Account account = createAccount();

        when(restService.post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt()))
                .thenReturn(Optional.of(account));
        final Optional<Account> response = userService.createAccount(account);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
        verify(restService).post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testCreateAccount_NoAccountFound() {
        final Optional<Account> response = userService.createAccount(null);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
        verify(restService, times(0)).post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testCreateAccount_catchException(){
        final Account account = createAccount();

        when(restService.post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt())).thenThrow(Exception.class);
        final Optional<Account> response = userService.createAccount(account);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
    }

    @Test
    public void testUpdateAccount(){
        final Account account = createAccount();
        account.setId(1);

        when(restService.patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt()))
                .thenReturn(Optional.of(account));
        final Optional<Account> response = userService.updateAccount(account);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
        verify(restService).patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testUpdateAccount_nullAccount(){
        final Optional<Account> response = userService.updateAccount(null);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
        verify(restService, times(0)).patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testUpdateAccount_nullId(){
        final Account account = createAccount();
        account.setId(0);

        final Optional<Account> response = userService.updateAccount(account);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
        verify(restService, times(0)).patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt());
    }

    @Test
    public void testUpdateAccount_PersistenceApiResponseIsNotPresent(){
        final Account account = createAccount();
        account.setId(1);

        when(restService.patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt()))
                .thenReturn(Optional.empty());
        final Optional<Account> response = userService.updateAccount(account);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
    }

    @Test
    public void testUpdateAccount_catchException(){
        final Account account = createAccount();
        account.setId(1);

        when(restService.patch(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt()))
                .thenThrow(Exception.class);
        final Optional<Account> response = userService.updateAccount(account);

        assertFalse(response.isPresent());
        assertEquals(Optional.empty(), response);
    }

    @Test
    public void testDeleteAccount(){
        final boolean response= true;
        final Account account = createAccount();
        account.setId(1);

        when(restService.delete(anyString(), anyInt(), anyInt(), any()))
                .thenReturn(response);
        final boolean persistenceApiResponse = userService.deleteAccount(account);

        assertNotNull(persistenceApiResponse);
        assertTrue(persistenceApiResponse);
        assertEquals(response, persistenceApiResponse);
        verify(restService).delete(anyString(), anyInt(), anyInt(), any());
    }

    @Test
    public void testDeleteAccount_nullAccount(){
        final boolean persistenceApiResponse = userService.deleteAccount(null);

        assertNotNull(persistenceApiResponse);
        assertFalse(persistenceApiResponse);
        verify(restService, times(0)).delete(anyString(), anyInt(), anyInt(), any());
    }

    @Test
    public void testDeleteAccount_nullId(){
        final Account account = createAccount();
        account.setId(0);

        final boolean persistenceApiResponse = userService.deleteAccount(account);

        assertNotNull(persistenceApiResponse);
        assertFalse(persistenceApiResponse);
        verify(restService, times(0)).delete(anyString(), anyInt(), anyInt(), any());
    }

    @Test
    public void testDeleteAccount_persistenceApiResponseIsNotPresent(){
        final boolean response= false;
        final Account account = createAccount();
        account.setId(1);

        when(restService.delete(anyString(), anyInt(), anyInt(), any()))
                .thenReturn(response);
        final boolean persistenceApiResponse = userService.deleteAccount(account);

        assertNotNull(persistenceApiResponse);
        assertFalse(persistenceApiResponse);
        assertEquals(response, persistenceApiResponse);
        verify(restService).delete(anyString(), anyInt(), anyInt(), any());
    }

    @Test
    public void testDeleteAccount_catchException(){
        final Account account = createAccount();
        account.setId(1);

        when(restService.delete(anyString(), anyInt(), anyInt(), any())).thenThrow(Exception.class);
        final boolean persistenceApiResponse = userService.deleteAccount(account);

        assertNotNull(persistenceApiResponse);
        assertFalse(persistenceApiResponse);
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
