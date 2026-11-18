package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.Utilisateur;
import com.covoiturage.api.entity.Vehicule;
import com.covoiturage.api.exception.ConducteurNotFoundException;
import com.covoiturage.api.exception.UtilisateurNotFoundException;
import com.covoiturage.api.exception.VehiculeAlreadyExists;
import com.covoiturage.api.mapper.VehiculeMapper;
import com.covoiturage.api.repository.ConducteurRepository;
import com.covoiturage.api.repository.UtilisateurRepository;
import com.covoiturage.api.repository.VehiculeRepository;
import com.covoiturage.api.service.VehiculeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class VehiculeServiceImp implements VehiculeService {


    private final VehiculeRepository vehiculeRepository;
    private final ConducteurRepository conducteurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeMapper vehiculeMapper;

    public VehiculeServiceImp(VehiculeRepository vehiculeRepository, ConducteurRepository conducteurRepository, UtilisateurRepository utilisateurRepository, VehiculeMapper vehiculeMapper) {
        this.vehiculeRepository = vehiculeRepository;
        this.conducteurRepository = conducteurRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeMapper = vehiculeMapper;
    }


    @Override
    @Transactional
    public VehiculeResponse createVehicule(VehiculeCreateRequest request){

        //check if the immatriculation is  already exists
        if(vehiculeRepository.existsByImmatriculation(request.immatriculation())){
            throw new VehiculeAlreadyExists("Immatriculation is already exists.");
        }

        //if the conductor
        Conducteur conducteur = conducteurRepository.findById(
                request.conducteurId()
        ).orElseThrow(() ->
                new ConducteurNotFoundException("Conducteur not found")
        );

        Vehicule vehicule = vehiculeMapper.toEntity(request);
        vehicule.setConducteur(conducteur);

        Vehicule savedVehicule = vehiculeRepository.save(vehicule);

        return vehiculeMapper.toResponse(savedVehicule);

    }
}
