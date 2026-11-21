package com.covoiturage.api.exception;

public class PointArretNotFound extends RuntimeException{
    public PointArretNotFound(String message){
        super(message);
    }
}
