package com.covoiturage.api.mapper;

import com.covoiturage.api.dto.reservation.ReservationCreateRequest;
import com.covoiturage.api.dto.reservation.ReservationResponse;
import com.covoiturage.api.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ReservationMapper {



    //ignore the champ that the mapper that can not enter.
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "montantTotal",ignore = true)
    @Mapping(target = "dateReservation",ignore = true)
    @Mapping(target = "statut",ignore = true)
    @Mapping(target = "passager",ignore = true)
    @Mapping(target = "trajet",ignore = true)
    @Mapping(target = "pointMontee",ignore = true)
    @Mapping(target = "pointDescente",ignore = true)
    Reservation toEntity(ReservationCreateRequest request);


    // response
    @Mapping(source = "passager.id",target = "passagerId")
    @Mapping(source = "trajet.id",target = "trajetId")
    @Mapping(source = "pointMontee.id",target = "pointMonteeId")
    @Mapping(source = "pointDescente.id",target = "pointDescenteId")
    ReservationResponse toResponse(Reservation reservation);
}
