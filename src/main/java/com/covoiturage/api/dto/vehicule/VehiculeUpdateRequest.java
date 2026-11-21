
package com.covoiturage.api.dto.vehicule;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VehiculeUpdateRequest(

        String marque,

        String modele,

        String immatriculation,

        String couleur,

        Integer capacite,

        Long conducteurId

) {}
