
package com.covoiturage.api.dto.trajet;

import com.covoiturage.api.dto.pointarret.PointArretResponse;
import com.covoiturage.api.entity.enums.StatutTrajet;

import java.time.LocalDateTime;
import java.util.List;

public record TrajetResponse(

        Long id,
        Long conducteurId,
        Long vehiculeId,
        LocalDateTime dateDepart,
        Integer placesProposees,
        Long placesRestantes,
        StatutTrajet status,
        List<PointArretResponse> pointArrets

) {}
