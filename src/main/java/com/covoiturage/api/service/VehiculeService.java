package com.covoiturage.api.service;

import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import com.covoiturage.api.dto.vehicule.VehiculeUpdateRequest;
import org.springframework.stereotype.Service;


@Service
public interface VehiculeService {


    //create vehicule
    VehiculeResponse createVehicule(VehiculeCreateRequest request);

    //update vehicule
    VehiculeResponse updateVehicule(Long id, VehiculeUpdateRequest request);

}
