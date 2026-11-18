
package com.covoiturage.api.dto.vehicule;

public record VehiculeResponse(

        Long id,

        String marque,

        String modelE,

        String immatriculation,

        String couleur,

        Integer capacite,

        Long conducteurId

) {}
