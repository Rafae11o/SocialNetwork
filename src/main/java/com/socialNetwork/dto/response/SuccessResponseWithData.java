package com.socialNetwork.dto.response;

public class SuccessResponseWithData<T> extends SuccessResponse{

    private final T data;

    /**
     *
     * @param message - to display to the user
     * @param data - to send to the frontend
     */
    public SuccessResponseWithData(String message, T data) {
        super(message);
        this.data = data;
    }

    /**
     *
     * @param data - to send to the frontend
     */
    public SuccessResponseWithData(T data){
        this("Success", data);
    }

    public T getData() {
        return data;
    }
}
