
package com.covoiturage.api.mapper;

import com.covoiturage.api.dto.utilisateur.ConducteurCreateRequest;
import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;
import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.enums.TypeUtilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConducteurMapper {

    @Mapping(target = "id", ignore = true)
    Conducteur toEntity(ConducteurCreateRequest request);

    @Mapping(target = "typeUtilisateur",
            expression = "java(TypeUtilisateur.CONDUCTEUR)")
    UtilisateurResponse toResponse(Conducteur conducteur);
}
