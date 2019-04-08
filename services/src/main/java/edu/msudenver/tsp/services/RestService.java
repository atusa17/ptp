package edu.msudenver.tsp.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.msudenver.tsp.services.factory.RequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RestService {
    private static final Gson GSON = new Gson();
    private final RequestFactory requestFactory;

    @Autowired
    public RestService(final RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    public boolean delete(final String uri, final Integer connectionTimeout, final Integer socketTimeout, final HttpStatus httpStatus) {
        LOG.info("Sending DELETE {}", uri);
        final Optional<HttpResponse> response = send(requestFactory.delete(uri), null, connectionTimeout, socketTimeout);

        return response.isPresent() && response.get().getStatusLine().getStatusCode() == httpStatus.value();
    }

    <T> Optional<T> get(final String uri, final TypeToken<T> type, final Integer connectionTimeout, final Integer socketTimeout, final String auth) {
        LOG.info("Sending GET {}", uri);
        return send(requestFactory.get(uri), auth, connectionTimeout, socketTimeout, type);
    }

    <T> Optional<T> post(final String uri, final String requestJson, final TypeToken<T> type, final Integer connectionTimeout, final Integer socketTimeout) {
        LOG.info("Sending POST {} with body: {}", uri, requestJson);
        return send(requestFactory.post(uri, requestJson), null, connectionTimeout, socketTimeout, type);
    }

    Optional<HttpResponse> post(final String uri, final String requestJson, final Integer connectionTimeout, final Integer socketTimeout) {
        LOG.info("Sending POST {} with body: {}", uri, requestJson);
        return send(requestFactory.post(uri, requestJson), null, connectionTimeout, socketTimeout);
    }

    <T> Optional<T> put(final String uri, final String requestJson, final TypeToken<T> type, final Integer connectionTimeout, final Integer socketTimeout, final String auth) {
        LOG.info("Sending PUT {} with body: {}", uri, requestJson);
        return send(requestFactory.put(uri, requestJson), auth, connectionTimeout, socketTimeout, type);
    }

    <T> Optional<T> patch(final String uri, final String requestJson, final TypeToken<T> type, final Integer connectionTimeout, final Integer socketTimeout) {
        LOG.info("Sending PATCH {} with body: {}", uri, requestJson);
        return send(requestFactory.patch(uri, requestJson), null, connectionTimeout, socketTimeout, type);
    }

    private <T> Optional<T> send(final Request request, final String auth, final Integer connectionTimeout, final Integer socketTimeout, final TypeToken<T> type) {
        try {
            final Optional<HttpResponse> optionalHttpResponse = send(request, auth, connectionTimeout, socketTimeout);
            if (optionalHttpResponse.isPresent()) {
                final HttpResponse httpResponse = optionalHttpResponse.get();
                LOG.info("Received {} response", httpResponse.getStatusLine().getStatusCode());

                final String jsonResponse = httpResponse.getEntity() == null ? null : EntityUtils.toString(httpResponse.getEntity());
                if (StringUtils.isNotBlank(jsonResponse)) {
                    final T responses = GSON.fromJson(jsonResponse, type.getType());
                    if (responses instanceof List) {
                        LOG.info("Found {} responses.", ((List) responses).size());
                        if (((List) responses).isEmpty()) {
                            return Optional.empty();
                        }
                    }

                    return Optional.ofNullable(responses);
                }
            }
        } catch (final Exception e) {
            LOG.error("Could not send request", e);
        }

        return Optional.empty();
    }

    private Optional<HttpResponse> send(final Request request, final String auth, final Integer connectionTimeout, final Integer socketTimeout) {
        if (StringUtils.isNotBlank(auth)) {
            request.addHeader("Authorization", "Basic " + auth);
        }

        try {
            return Optional.ofNullable(request.connectTimeout(connectionTimeout)
                    .socketTimeout(socketTimeout)
                    .execute()
                    .returnResponse());
        } catch (final Exception e) {
            LOG.error("Could not send request", e);
            return Optional.empty();
        }
    }
}


