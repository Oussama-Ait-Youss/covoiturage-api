package com.covoiturage.api.controller.vehicule;


import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import com.covoiturage.api.entity.Vehicule;
import com.covoiturage.api.service.Imp.VehiculeServiceImp;
import com.covoiturage.api.service.VehiculeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
