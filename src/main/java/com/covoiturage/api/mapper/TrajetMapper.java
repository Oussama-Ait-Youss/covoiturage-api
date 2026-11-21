
package com.covoiturage.api.mapper;

import com.covoiturage.api.dto.trajet.*;
import com.covoiturage.api.dto.pointarret.*;
import com.covoiturage.api.entity.Trajet;
import com.covoiturage.api.entity.PointArret;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TrajetMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicule", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dateDepart", ignore = true)
    @Mapping(target = "pointArrets", ignore = true)
    Trajet toEntity(TrajetCreateRequest request);

    // POINT ARRET
    PointArretResponse toPointArretResponse(PointArret point);

    List<PointArretResponse> toPointArretResponses(
            List<PointArret> points
    );

    // RESPONSE
    @Mapping(target = "conducteurId",
            source = "trajet.vehicule.conducteur.id")
    @Mapping(target = "vehiculeId",
            source = "trajet.vehicule.id")
    @Mapping(target = "placesRestantes",
            source = "placesRestantes")
    TrajetResponse toResponse(
            Trajet trajet,
            Long placesRestantes
    );
}
