package com.covoiturage.api.exception;

import com.covoiturage.api.entity.Trajet;
import org.springframework.transaction.annotation.Transactional;

public class TrajetNotFoundException extends RuntimeException{
    public TrajetNotFoundException(String message){
        super(message);
    }
}
