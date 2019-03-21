package edu.msudenver.tsp.services.factory;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

@Service
public class RequestFactory {
    public Request delete(final String uri) {
        return Request.Delete(uri);
    }

    public Request get(final String uri) {
        return Request.Get(uri);
    }

    public Request post(final String uri, final String requestJson) {
        return StringUtils.isNotBlank(requestJson) ? Request.Post(uri).bodyString(requestJson, ContentType.APPLICATION_JSON) : Request.Post(uri);
    }

    public Request put(final String uri, final String requestJson) {
        return StringUtils.isNotBlank(requestJson) ? Request.Put(uri).bodyString(requestJson, ContentType.APPLICATION_JSON) : Request.Put(uri);
    }

    public Request patch(final String uri, final String requestJson) {
        return StringUtils.isNotBlank(requestJson) ? Request.Put(uri).bodyString(requestJson, ContentType.APPLICATION_JSON) : Request.Patch(uri);
    }
}