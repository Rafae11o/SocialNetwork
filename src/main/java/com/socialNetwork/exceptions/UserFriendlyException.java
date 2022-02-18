package com.socialNetwork.exceptions;

public class UserFriendlyException extends Exception {
    /**
     *
     * @param msg - message to display to the user
     */
    public UserFriendlyException(String msg){
        super(msg);
    }
}
