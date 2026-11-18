
package com.covoiturage.api.dto.vehicule;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VehiculeCreateRequest(

        @NotBlank(message = "Marque is required")
        String marque,

        @NotBlank(message = "Model is required")
        String modele,

        @NotBlank(message = "Immatriculation is required")
        String immatriculation,

        @NotBlank(message = "Couleur is required")
        String couleur,

        @NotNull(message = "Capacite is required")
        @Min(value = 1, message = "Capacite must be at least 1")
        Integer capacite,

        @NotNull(message = "Conducteur ID is required")
        Long conducteurId

) {}
