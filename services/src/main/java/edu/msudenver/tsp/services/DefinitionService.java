package edu.msudenver.tsp.services;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.msudenver.tsp.services.dto.Definition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
public class DefinitionService {
    private final RestService restService;
    @Value("${persistence.api.connection.timeout.milliseconds}") private int connectionTimeoutMilliseconds;
    @Value("${persistence.api.socket.timeout.milliseconds}") private int socketTimeoutMilliseconds;
    @Value("${persistence.api.base.url}") private String persistenceApiBaseUrl;

    @Autowired
    public DefinitionService(final RestService restService) {
        this.restService = restService;
    }

    public Optional<Definition> createNewDefinition(final Definition definition) {
        if (definition == null) {
            LOG.error("Given null definition, returning {}");
            return Optional.empty();
        }
        final Instant start = Instant.now();

        try {
            final TypeToken<Definition> definitionTypeToken = new TypeToken<Definition>() {};
            final Optional<Definition> persistenceApiResponse = restService.post(persistenceApiBaseUrl + "/",
                    new GsonBuilder().create().toJson(definition),
                    definitionTypeToken,
                    connectionTimeoutMilliseconds,
                    socketTimeoutMilliseconds);

            if(persistenceApiResponse.isPresent()) {
                LOG.info("Returning {}", persistenceApiResponse.get());
            } else {
                LOG.info("Unable to create new definition {}", definition.toString());
            }

            return persistenceApiResponse;
        } catch (final Exception e) {
            LOG.error("Error creating new definition {}", e);
            return Optional.empty();
        } finally {
            LOG.info("Create new definition request took {}ms", Duration.between(start, Instant.now()).toMillis());
        }
    }
}
