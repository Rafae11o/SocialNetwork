package com.socialNetwork.dto.response;

import lombok.Data;

@Data
public class ErrorResponse extends ResponseWithMessage {
    public ErrorResponse(String message) {
        super(message);
    }

    public ErrorResponse(Exception exception) {
        super(exception.getMessage());
    }

}
