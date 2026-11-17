package com.covoiturage.api.service.exception;

public class InvalideUtilisateurTypeException extends RuntimeException{
    public InvalideUtilisateurTypeException(String message){
        super(message);
    }
}
