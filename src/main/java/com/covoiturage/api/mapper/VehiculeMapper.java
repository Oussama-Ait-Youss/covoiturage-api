
package com.covoiturage.api.mapper;

import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import com.covoiturage.api.dto.vehicule.VehiculeUpdateRequest;
import com.covoiturage.api.entity.Vehicule;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {

    // CREATE: DTO -> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "conducteur", ignore = true)
    Vehicule toEntity(VehiculeCreateRequest request);

    // RESPONSE: ENTITY -> DTO
    @Mapping(source = "conducteur.id", target = "conducteurId")
    VehiculeResponse toResponse(Vehicule vehicule);


    //UPDATE
    void updateEntity(
            VehiculeUpdateRequest request, @MappingTarget Vehicule vehicule
    );
}
