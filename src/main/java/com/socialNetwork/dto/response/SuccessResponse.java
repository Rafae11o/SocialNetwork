package com.socialNetwork.dto.response;

import lombok.Data;

@Data
public class SuccessResponse extends ResponseWithMessage{
    public SuccessResponse(String message) {
        super(message, true);
    }
}
