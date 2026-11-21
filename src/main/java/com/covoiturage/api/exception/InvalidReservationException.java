package com.covoiturage.api.exception;

public class InvalidReservationException extends RuntimeException{
    public InvalidReservationException(String message){
        super(message);
    }
}
