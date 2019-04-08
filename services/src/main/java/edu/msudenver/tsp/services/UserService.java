package edu.msudenver.tsp.services;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.msudenver.tsp.services.dto.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final RestService restService;
    @Value("${persistence.api.connection.timeout.milliseconds}") private int connectionTimeoutMilliseconds;
    @Value("${persistence.api.socket.timeout.milliseconds}") private int socketTimeoutMilliseconds;
    @Value("${persistence.api.base.url}") private String persistenceApiBaseUrl;

    @Autowired
    public UserService(final RestService restService) {
        this.restService = restService;
    }

    public Optional<List<Account>> getListOfAccounts() {
        final Instant start = Instant.now();

        try {
            final TypeToken<List<Account>> typeToken = new TypeToken<List<Account>>() {};
            final Optional<List<Account>> persistenceApiResponse = restService.get(persistenceApiBaseUrl + "accounts/",
                    typeToken,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds,
                    null);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Returning {}", persistenceApiResponse.get());
            } else {
                LOG.warn("Unable to get the list of accounts");
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error getting the list of accounts", e);
            return Optional.empty();
        } finally {
            LOG.info("Get the list of accounts request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    public Optional<Account> findAccountById(final int id) {

        if (id == 0) {
            LOG.error("No user ID specified! Returning {}");
            return Optional.empty();
        }

        final Instant start = Instant.now();

        try {
            final TypeToken<Account> typeToken = new TypeToken<Account>() {};
            final Optional<Account> persistenceApiResponse = restService.get(persistenceApiBaseUrl + "accounts/id?id=" + id,
                    typeToken,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds,
                    null);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Returning {}", persistenceApiResponse.get());
            } else {
                LOG.warn("Unable to find account with id {}", id);
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error finding account by id", e);
            return Optional.empty();
        } finally {
            LOG.info("Find account by ID request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    public Optional<Account> findAccountByUsername(final String username) {
        if (username == null) {
            LOG.error("No username specified! Returning {}");
            return Optional.empty();
        }

        final Instant start = Instant.now();

        try {
            final TypeToken<Account> typeToken = new TypeToken<Account>() {};
            final Optional<Account> persistenceApiResponse = restService.get(persistenceApiBaseUrl + "accounts/username?username=" + username,
                    typeToken,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds,
                    null);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Returning {}", persistenceApiResponse.get());
            } else {
                LOG.warn("Unable to GET account with username {}", username);
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error finding account by username", e);
            return Optional.empty();
        } finally {
            LOG.info("Find account by username request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    public Optional<Account> createAccount(final Account account) {
        if (account == null) {
            LOG.error("Given null account, returning {}");
            return Optional.empty();
        }
        final Instant start = Instant.now();

        try {
            final TypeToken<Account> typeToken = new TypeToken<Account>() {};
            final Optional<Account> persistenceApiResponse = restService.post(persistenceApiBaseUrl + "accounts/",
                    new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create().toJson(account),
                    typeToken,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Returning {}", persistenceApiResponse.get());
            } else {
                LOG.warn("Unable to create new account {}", account.toString());
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error creating new account", e);
            return Optional.empty();
        } finally {
            LOG.info("Create new account request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    public Optional<Account> updateAccount(final Account account) {
        if (account == null) {
            LOG.error("Specified account is null; returning {}");
            return Optional.empty();
        }

        if (account.getId() == 0) {
            LOG.error("No user ID specified! Returning {}");
            return Optional.empty();
        }

        final int id = account.getId();
        final Instant start = Instant.now();

        try {
            final TypeToken<Account> typeToken = new TypeToken<Account>(){};
            final Optional<Account> persistenceApiResponse = restService.patch(persistenceApiBaseUrl + "accounts/" + id,
                    new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create().toJson(account),
                    typeToken,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Returning {}", persistenceApiResponse.get());
            } else {
                LOG.warn("Unable to update user with id {}", account.getId());
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error updating user", e);
            return Optional.empty();
        } finally {
            LOG.info("Update user request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }


    public boolean deleteAccount(final Account account) {
        if (account == null){
            LOG.error("Specified account is null; returning {}");
            return false;
        }

        if (account.getId() == 0) {
            LOG.error("No user ID specified! Returning {}");
            return false;
        }

        final int id = account.getId();
        final Instant start = Instant.now();

        try {

            final boolean persistenceApiResponse = restService.delete(persistenceApiBaseUrl + "accounts/" + id,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds, HttpStatus.NO_CONTENT);
            if (persistenceApiResponse) {
                LOG.info("Returning {}", persistenceApiResponse);
            }
            else {
                LOG.error("Unable to delete user {}", account);
            }

            return persistenceApiResponse;
        }catch (final Exception e) {
            LOG.error("Error deleting user", e);
            return false;
        } finally {
            LOG.info("Delete user request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }
}
