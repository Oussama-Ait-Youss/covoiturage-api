package com.covoiturage.api.exception;

public class VehiculeAlreadyExists extends RuntimeException{
    public VehiculeAlreadyExists(String message){
        super(message);
    }
}
