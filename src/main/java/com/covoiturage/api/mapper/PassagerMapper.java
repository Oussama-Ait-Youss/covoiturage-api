
package com.covoiturage.api.mapper;

import com.covoiturage.api.dto.utilisateur.PassagerCreateRequest;
import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;
import com.covoiturage.api.entity.Passager;
import com.covoiturage.api.entity.enums.TypeUtilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PassagerMapper {

    @Mapping(target = "id", ignore = true)
    Passager toEntity(PassagerCreateRequest request);

    @Mapping(target = "typeUtilisateur",
            expression = "java(TypeUtilisateur.PASSAGER)")
    UtilisateurResponse toResponse(Passager passager);
}
