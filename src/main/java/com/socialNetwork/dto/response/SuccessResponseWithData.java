package com.socialNetwork.dto.response;

public class SuccessResponseWithData<T> extends SuccessResponse{

    private final T data;

    public SuccessResponseWithData(String message, T data) {
        super(message);
        this.data = data;
    }

    public SuccessResponseWithData(T data){
        this("Success", data);
    }

    public T getData() {
        return data;
    }
}
