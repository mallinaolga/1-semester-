package com.mipt.olgamallina.scope;

import org.springframework.web.context.annotation.RequestScope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Request-scoped bean: new instance for each HTTP request.
 */
@Component
@RequestScope
public class RequestScopedBean {

    private final String requestId = UUID.randomUUID().toString();
    private final Instant startTime = Instant.now();

    public String getRequestId() {
        return requestId;
    }

    public Instant getStartTime() {
        return startTime;
    }
}