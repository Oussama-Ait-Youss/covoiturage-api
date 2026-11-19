package com.covoiturage.api.exception;

public class UserNotActiveException extends RuntimeException{
    public UserNotActiveException(String message){
        super(message);
    }
}
