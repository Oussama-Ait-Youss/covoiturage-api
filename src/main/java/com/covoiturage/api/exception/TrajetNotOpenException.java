package com.covoiturage.api.exception;

public class TrajetNotOpenException extends RuntimeException{
    public TrajetNotOpenException(String message){
        super(message);
    }
}
