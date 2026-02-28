package com.mipt.olgamallina.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Prototype-scoped bean: new instance per request to the container (via ObjectProvider).
 */
@Component
@Scope("prototype")
public class PrototypeScopedBean {

    public String generateTaskId() {
        return UUID.randomUUID().toString();
    }
}