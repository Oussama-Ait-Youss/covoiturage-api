package com.covoiturage.api.exception;

public class ReservationCancellationException extends RuntimeException{
    public ReservationCancellationException(String message){
        super(message);
    }
}
