package com.covoiturage.api.exception;

public class PointArretNotBelongToTrajetException extends RuntimeException{
    public PointArretNotBelongToTrajetException(String message){
        super(message);
    }
}
