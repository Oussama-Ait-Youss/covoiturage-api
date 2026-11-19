package com.covoiturage.api.exception;

public class VehiculeNotOwnedException extends RuntimeException{
    public VehiculeNotOwnedException(String message){
        super(message);
    }
}
