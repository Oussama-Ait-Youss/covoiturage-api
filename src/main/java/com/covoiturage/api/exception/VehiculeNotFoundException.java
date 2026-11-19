package com.covoiturage.api.exception;

public class VehiculeNotFoundException extends RuntimeException{
    public VehiculeNotFoundException(String message){
        super(message);
    }
}
