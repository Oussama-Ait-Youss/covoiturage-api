package com.covoiturage.api.exception;

public class InvalideUtilisateurTypeException extends RuntimeException{
    public InvalideUtilisateurTypeException(String message){
        super(message);
    }
}
