package com.covoiturage.api.controller.vehicule;


import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import com.covoiturage.api.dto.vehicule.VehiculeUpdateRequest;
import com.covoiturage.api.entity.Vehicule;
import com.covoiturage.api.service.Imp.VehiculeServiceImp;
import com.covoiturage.api.service.VehiculeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/vehicule")
public class VehiculeController {


    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }


    @PostMapping
    public VehiculeResponse createVehicule(
            @Valid @RequestBody VehiculeCreateRequest request){
        return vehiculeService.createVehicule(request);
    }

    @PutMapping("/{id}")
    public VehiculeResponse updateVehicule(
            Long id,
            @Valid VehiculeUpdateRequest request
    ){
        return vehiculeService.updateVehicule(id,request);
    }
}
