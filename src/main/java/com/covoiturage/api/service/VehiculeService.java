package com.covoiturage.api.service;

import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import com.covoiturage.api.dto.vehicule.VehiculeUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface VehiculeService {


    //create vehicule
    VehiculeResponse createVehicule(VehiculeCreateRequest request);

    //update vehicule
    VehiculeResponse updateVehicule(Long id, VehiculeUpdateRequest request);

    //get vehicule by id
    VehiculeResponse getVehiculeById(Long id);


    //get all vehicules
    List<VehiculeResponse> getAllVehicules();


    //delete vehicule
    void deleteVehicule(Long id);

}
