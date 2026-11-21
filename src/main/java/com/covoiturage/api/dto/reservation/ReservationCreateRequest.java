package com.covoiturage.api.dto.reservation;

import com.covoiturage.api.entity.Passager;
import com.covoiturage.api.entity.PointArret;
import com.covoiturage.api.entity.Trajet;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.aspectj.weaver.ast.Not;

import java.time.LocalDateTime;

public record ReservationCreateRequest(

        @NotNull
        Long trajetId,

        @NotNull
        Long passagerId,

        @NotNull
        @Min(1)
        Integer nombrePlaces,

        @NotNull
        Long pointMonteeId,


        @NotNull
        Long pointDescenteId

) {
}
