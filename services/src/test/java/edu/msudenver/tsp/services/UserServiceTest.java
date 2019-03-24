package edu.msudenver.tsp.services;

import com.google.gson.reflect.TypeToken;
import edu.msudenver.tsp.services.dto.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;




@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private RestService restService;
    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateNewAccount() throws ParseException {

        final Account account = new Account();
        account.setUsername("Test username");
        account.setPassword("test password");
        account.setAdministratorStatus(false);
        account.setLastLogin(new Date());

        when(restService.post(anyString(), anyString(), any(TypeToken.class), anyInt(), anyInt()))
                .thenReturn(Optional.of(account));

        final Optional<Account> response = userService.createNewAccount(account);

        assertTrue(response.isPresent());
        assertEquals(account, response.get());
    }
}