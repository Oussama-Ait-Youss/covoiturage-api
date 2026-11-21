package com.covoiturage.api.controller.vehicule;


import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import com.covoiturage.api.dto.vehicule.VehiculeUpdateRequest;
import com.covoiturage.api.entity.Vehicule;
import com.covoiturage.api.service.Imp.VehiculeServiceImp;
import com.covoiturage.api.service.VehiculeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vehicule")
public class VehiculeController {


    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeResponse createVehicule(
            @Valid @RequestBody VehiculeCreateRequest request){
        return vehiculeService.createVehicule(request);
    }


    @PutMapping("/{id}")
    public VehiculeResponse updateVehicule(
            @PathVariable Long id,
            @Valid @RequestBody VehiculeUpdateRequest request
    ) {
        return vehiculeService.updateVehicule(id, request);
    }



    // GET VEHICLE BY ID
    @GetMapping("/{id}")
    public VehiculeResponse getVehiculeById(
            @PathVariable Long id) {

        return vehiculeService.getVehiculeById(id);
    }

    // GET ALL VEHICLES
    @GetMapping
    public List<VehiculeResponse> getAllVehicules() {

        return vehiculeService.getAllVehicules();
    }

    // DELETE VEHICLE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicule(
            @PathVariable Long id) {

        vehiculeService.deleteVehicule(id);
    }


    // GET VEHICLES BY DRIVER
    @GetMapping("/conducteurs/{conducteurId}")
    public List<VehiculeResponse> getVehiculesByConducteur(
            @PathVariable Long conducteurId
    ) {
        return vehiculeService.getVehiculesByConducteur(
                conducteurId
        );
    }


}
