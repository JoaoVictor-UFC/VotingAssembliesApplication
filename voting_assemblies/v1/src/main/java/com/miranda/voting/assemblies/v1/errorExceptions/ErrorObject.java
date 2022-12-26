package com.miranda.voting.assemblies.v1.errorExceptions;

import lombok.Data;

@Data
public class ErrorObject {

    private String message;
    private String field;
    private Object parameter;
}
