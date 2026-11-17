
package com.covoiturage.api.dto.utilisateur;

import com.covoiturage.api.entity.enums.TypeUtilisateur;

public record UtilisateurResponse(

        Long id,
        String nom,
        String prenom,
        String email,
        String telephone,
        TypeUtilisateur typeUtilisateur
) {
}
