
package com.covoiturage.api.dto.reservation;

import com.covoiturage.api.entity.enums.StatutReservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponse(

        Long id,

        Long trajetId,

        Long passagerId,

        Integer nombrePlaces,

        Long pointMonteeId,

        Long pointDescenteId,

        BigDecimal montantTotal,

        LocalDateTime dateReservation,

        StatutReservation statut

) {
}
