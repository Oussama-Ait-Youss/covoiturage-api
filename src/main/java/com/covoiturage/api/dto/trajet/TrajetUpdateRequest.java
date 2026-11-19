
package com.covoiturage.api.dto.trajet;

import com.covoiturage.api.dto.pointarret.PointArretCreateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record TrajetUpdateRequest(

        @NotNull
        @Min(1)
        Integer placesProposees,

        @NotNull
        @Size(min = 2)
        List<@NotNull @Valid PointArretCreateRequest> pointArrets

) {}
