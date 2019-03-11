package edu.msudenver.tsp.services;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.msudenver.tsp.services.dto.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
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

    public Optional<Account> createNewAccount(final Account account) {
        if (account == null) {
            LOG.error("Given null account, returning {}");
            return Optional.empty();
        }
        final Instant start = Instant.now();

        try {
            final TypeToken<Account> typeToken = new TypeToken<Account>() {};
            final Optional<Account> persistenceApiResponse = restService.post(persistenceApiBaseUrl + "/accounts/",
                    new GsonBuilder().create().toJson(account),
                    typeToken,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds);

            if (persistenceApiResponse.isPresent()) {
                LOG.info("Returning {}", persistenceApiResponse.get());
            } else {
                LOG.info("Unable to create new account {}", account.toString());
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error creating new account {}", e);
            return Optional.empty();
        } finally {
            LOG.info("Create new account request took {} ms", Duration.between(start, Instant.now()).toMillis());
        }
    }
}
