package com.covoiturage.api.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String message){
        super(message);
    }
}
