package com.socialNetwork.dto.response;

import lombok.Data;

@Data
public class ErrorResponse extends ResponseWithMessage {
    public ErrorResponse(String message) {
        super(message, false);
    }

    public ErrorResponse(Exception exception) {
        super(exception.getMessage(), false);
    }

}
