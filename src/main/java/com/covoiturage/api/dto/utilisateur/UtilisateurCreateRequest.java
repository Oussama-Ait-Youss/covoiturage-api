
package com.covoiturage.api.dto.utilisateur;

import com.covoiturage.api.entity.enums.TypeUtilisateur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record UtilisateurCreateRequest(

        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotBlank(message = "Le prenom est obligatoire")
        String prenom,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Email invalide")
        String email,

        @NotBlank(message = "Le telephone est obligatoire")
        String telephone,

        @NotNull(message = "Le type utilisateur est obligatoire")
        TypeUtilisateur typeUtilisateur,

        String numeroPermis

) {
}
