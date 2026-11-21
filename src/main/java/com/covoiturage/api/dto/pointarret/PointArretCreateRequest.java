
package com.covoiturage.api.dto.pointarret;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointArretCreateRequest(

        @NotBlank
        String ville,

        @NotNull
        @Min(0)
        Integer ordre,

        @NotNull
        LocalDateTime heurePassage,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal prixDepuisDepart

) {}
