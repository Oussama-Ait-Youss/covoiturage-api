package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.reservation.ReservationCreateRequest;
import com.covoiturage.api.dto.reservation.ReservationResponse;
import com.covoiturage.api.entity.Passager;
import com.covoiturage.api.entity.PointArret;
import com.covoiturage.api.entity.Trajet;
import com.covoiturage.api.entity.enums.StatutTrajet;
import com.covoiturage.api.exception.*;
import com.covoiturage.api.mapper.ReservationMapper;
import com.covoiturage.api.repository.PassagerRepository;
import com.covoiturage.api.repository.PointArretRepository;
import com.covoiturage.api.repository.ReservationRepository;
import com.covoiturage.api.repository.TrajetRepository;
import com.covoiturage.api.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ReservationServiceImp implements ReservationService {


    private final ReservationRepository reservationRepository;
    private final TrajetRepository trajetRepository;
    private final PassagerRepository passagerRepository;
    private final PointArretRepository pointArretRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImp(
            ReservationRepository reservationRepository,
            TrajetRepository trajetRepository,
            PassagerRepository passagerRepository,
            PointArretRepository pointArretRepository,
            ReservationMapper reservationMapper) {

        this.reservationRepository = reservationRepository;
        this.trajetRepository = trajetRepository;
        this.passagerRepository = passagerRepository;
        this.pointArretRepository = pointArretRepository;
        this.reservationMapper = reservationMapper;
    }




    @Override
    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request){

        //get the passager
        Passager passager = passagerRepository.findById(request.passagerId()).orElseThrow(() -> new PassagerNotFoundException("passager not found"));


        //get the trajet
        Trajet trajet = trajetRepository.findById(request.trajetId()).orElseThrow(() -> new TrajetNotFoundException("trajet not found..."));


        //validate the trajet status
        if(!trajet.getStatus().equals(StatutTrajet.PUBLIE)){
            throw new TrajetNotOpenException("the trajet is not open");
        }

        //find the the stoping points
        PointArret pointMontee = pointArretRepository.findById(request.pointMonteeId()).orElseThrow(() -> new PointArretNotFound("point arret not found"));
        PointArret pointDescente = pointArretRepository.findById(request.pointDescenteId()).orElseThrow(() -> new PointArretNotFound("point arret not found"));


        //validate that the points belong the trajet.
        if(!pointMontee.getTrajet().getId().equals(trajet.getId()) && pointDescente.getTrajet().getId().equals(trajet.getId())){
            throw new PointArretNotBelongToTrajetException("point arret not belong to the trajet");
        }

        //validate the order
        if(pointMontee.getOrdre() >= pointDescente.getOrdre()){
            throw new PointArretOrderException("the order of the points arrets is invalide");
        }




    }




}
