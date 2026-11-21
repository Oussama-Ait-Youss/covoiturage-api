package com.covoiturage.api.exception;

public class NumberOfSeatNotEnoughException extends RuntimeException{
    public NumberOfSeatNotEnoughException(String message){
        super(message);
    }
}
