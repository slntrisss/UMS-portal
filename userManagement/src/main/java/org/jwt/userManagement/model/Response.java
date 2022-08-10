package org.jwt.userManagement.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@Builder
public class Response {
    private HttpStatus status;
    private int statusCode;
    private String message;
    private Map<?, ?> data;
    private String developerMessage;
}
