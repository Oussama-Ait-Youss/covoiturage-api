package com.covoiturage.api.exception;

public class InvalidVehiculeCapacityException extends RuntimeException{
    public InvalidVehiculeCapacityException(String message){
        super(message);
    }
}
