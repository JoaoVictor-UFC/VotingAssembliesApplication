package com.miranda.voting.assemblies.v1.errorExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ResourceUnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceUnauthorizedException(String message, Exception e) {
        super(message);
    }

}
