package com.socialNetwork.dto.response;

import lombok.Data;

@Data
public class SuccessResponse extends ResponseWithMessage{
    /**
     * @param message - to display to the user
     */
    public SuccessResponse(String message) {
        super(message, true);
    }
}
