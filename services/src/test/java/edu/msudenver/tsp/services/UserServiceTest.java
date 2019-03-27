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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

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
    public void testGetAccountById(){
        final Account account = createAccount();
        account.setId(1);

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenReturn(Optional.of(account));
        final Optional<Account> response = userService.getAccountById(1);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
    }

    @Test
    public void testGetAccountByUsername(){
        final Account account = createAccount();
        final String username = account.getUsername();

        when(restService.get(anyString(),any(TypeToken.class), anyInt(), anyInt(),anyString()))
                .thenReturn(Optional.of(account));
        final Optional<Account> response = userService.getAccountByUsername(username);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
    }
    @Test
    public void testCreateAccount(){
        final Account account = createAccount();

        when(restService.post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt()))
                .thenReturn(Optional.of(account));
        final Optional<Account> response = userService.createAccount(account);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
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
    }

    @Test
    public void testDeleteAccount(){
        final boolean response= true;
        final Account account = createAccount();
        account.setId(1);

        when(restService.delete(anyString(), anyInt(), anyInt(), any()))
                .thenReturn(response);
        final boolean persistenceApiResponse = userService.deleteAccount(account);

        assertTrue(persistenceApiResponse );
        assertEquals(response, persistenceApiResponse );
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
