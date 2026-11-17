
package com.covoiturage.api.mapper;

import com.covoiturage.api.dto.utilisateur.UtilisateurCreateRequest;
import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;
import com.covoiturage.api.entity.Admin;
import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.Passager;
import com.covoiturage.api.entity.Utilisateur;
import com.covoiturage.api.entity.enums.TypeUtilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    // DTO -> Passager
    @Mapping(target = "id", ignore = true)
    Passager toPassager(UtilisateurCreateRequest request);

    // DTO -> Conducteur
    @Mapping(target = "id", ignore = true)
    Conducteur toConducteur(UtilisateurCreateRequest request);

    // DTO -> Admin
    @Mapping(target = "id", ignore = true)
    Admin toAdmin(UtilisateurCreateRequest request);

    // Entity -> Response DTO
    @Mapping(
            target = "typeUtilisateur",
            expression = "java(getTypeUtilisateur(utilisateur))"
    )

    UtilisateurResponse toResponse(Utilisateur utilisateur);

    default TypeUtilisateur getTypeUtilisateur(
            Utilisateur utilisateur) {

        if (utilisateur instanceof Conducteur) {
            return TypeUtilisateur.CONDUCTEUR;
        }

        if (utilisateur instanceof Passager) {
            return TypeUtilisateur.PASSAGER;
        }

        if (utilisateur instanceof Admin) {
            return TypeUtilisateur.ADMIN;
        }

        throw new IllegalArgumentException(
                "Type utilisateur inconnu"
        );
    }
}
