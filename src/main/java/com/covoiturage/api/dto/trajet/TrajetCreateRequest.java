
package com.covoiturage.api.dto.trajet;

import com.covoiturage.api.dto.pointarret.PointArretCreateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record TrajetCreateRequest(

        @NotNull
        Long conducteurId,

        @NotNull
        Long vehiculeId,

        @NotNull
        @Min(1)
        Integer placesProposees,

        @NotNull
        @Size(min = 2)
        List<@NotNull @Valid PointArretCreateRequest> pointArrets

) {}





//SELECT
//t.id,
//t.places_proposees,
//t.places_proposees - COALESCE(SUM(r.nombre_places), 0) AS places_disponibles,
//pa_arrival.prix - pa_depart.prix AS montant_segment
//
//FROM trajet t
//
//JOIN pointarret pa_depart
//ON t.id = pa_depart.trajet_id
//
//JOIN pointarret pa_arrival
//ON t.id = pa_arrival.trajet_id
//
//LEFT JOIN reservation r
//ON r.trajet_id = t.id
//AND r.status = 'CONFIRMEE'
//
//WHERE pa_depart.ville = DepartCity
//AND pa_arrival.ville = ArrivalCity
//AND pa_depart.ordre < pa_arrival.ordre
//AND t.date_depart  = ArrivateTime
//AND t.status = 'PUBLIE'
//AND (pa_arrival.prix - pa_depart.prix) <= 100
//
//GROUP BY
//t.id,
//pa_depart.prix,
//pa_arrival.prix
//
//HAVING
//t.places_proposees - SUM(r.nombre_places) >= 1
//
//ORDER BY t.date_depart ASC;

