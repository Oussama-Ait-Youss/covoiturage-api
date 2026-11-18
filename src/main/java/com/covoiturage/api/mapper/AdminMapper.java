
package com.covoiturage.api.mapper;

import com.covoiturage.api.dto.utilisateur.AdminCreateRequest;
import com.covoiturage.api.dto.utilisateur.AdminUpdateRequest;
import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;
import com.covoiturage.api.entity.Admin;
import com.covoiturage.api.entity.enums.TypeUtilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(target = "id", ignore = true)
    Admin toEntity(AdminCreateRequest request);

    @Mapping(target = "typeUtilisateur",
            expression = "java(TypeUtilisateur.ADMIN)")
    UtilisateurResponse toResponse(Admin admin);

    @Mapping(target = "id", ignore = true)
    void updateEntity(
            AdminUpdateRequest request,
            @MappingTarget Admin admin
    );
}
