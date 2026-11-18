package com.covoiturage.api.service;

import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import org.springframework.stereotype.Service;


@Service
public interface VehiculeService {




    //create vehicule
    VehiculeResponse createVehicule(VehiculeCreateRequest request);
}
