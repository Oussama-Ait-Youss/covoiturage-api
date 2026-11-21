package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import com.covoiturage.api.dto.vehicule.VehiculeUpdateRequest;
import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.Utilisateur;
import com.covoiturage.api.entity.Vehicule;
import com.covoiturage.api.exception.*;
import com.covoiturage.api.mapper.VehiculeMapper;
import com.covoiturage.api.repository.ConducteurRepository;
import com.covoiturage.api.repository.UtilisateurRepository;
import com.covoiturage.api.repository.VehiculeRepository;
import com.covoiturage.api.service.VehiculeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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



    @Override
    @Transactional
    public VehiculeResponse updateVehicule(
            Long id,
            VehiculeUpdateRequest request) {

        // 1. Find the vehicle
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new VehiculeNotFoundException("Vehicule not found")
                );


        // Check if the vehicle belongs to the requested driver
        if (!vehicule.getConducteur().getId().equals(request.conducteurId())) {
            throw new VehiculeNotOwnedException(
                    "This vehicle does not belong to the specified conducteur"
            );
        }



        if(vehiculeRepository.existsByImmatriculation(request.immatriculation()) && !vehicule.getImmatriculation().equals(request.immatriculation())){
            throw new NumeroPermisAlreadyExistsException("Numero Permis Already exists..");
        }

        // 3. Find the driver
        Conducteur conducteur = conducteurRepository.findById(
                request.conducteurId()
        ).orElseThrow(() ->
                new ConducteurNotFoundException("Conducteur not found")
        );

        // 4. Check driver status
        if (!Boolean.TRUE.equals(conducteur.isActif())) {
            throw new UserNotActiveException("Conducteur is not active");
        }

        // 5. Validate capacity
        if (request.capacite() == null
                || request.capacite() < 1
                || request.capacite() > 8) {

            throw new CapacityNumberInvalideException(
                    "Capacity must be between 1 and 8"
            );
        }

        // 6. Update existing vehicle
        vehiculeMapper.updateEntity(request, vehicule);

        // 7. Set driver
        vehicule.setConducteur(conducteur);

        // 8. Save and return response
        Vehicule updatedVehicule = vehiculeRepository.save(vehicule);

        return vehiculeMapper.toResponse(updatedVehicule);
    }



    @Override
    @Transactional(readOnly = true)
    public VehiculeResponse getVehiculeById(Long id) {

        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new VehiculeNotFoundException("Vehicule not found")
                );

        return vehiculeMapper.toResponse(vehicule);
    }


    @Override
    @Transactional
    public void deleteVehicule(Long id) {

        // 1. Find the vehicle
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new VehiculeNotFoundException("Vehicule not found")
                );

        // 2. Delete vehicle
        vehiculeRepository.delete(vehicule);

        // 3. Execute pending SQL
        vehiculeRepository.flush();
    }


    @Override
    @Transactional(readOnly = true)
    public List<VehiculeResponse> getAllVehicules() {

        return vehiculeRepository.findAll()
                .stream()
                .map(vehiculeMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<VehiculeResponse> getVehiculesByConducteur(
            Long conducteurId
    ) {

        // 1. Verify that the driver exists
        if (!conducteurRepository.existsById(conducteurId)) {
            throw new ConducteurNotFoundException(
                    "Driver not found with id: " + conducteurId
            );
        }

        // 2. Find vehicles belonging to that driver
        List<Vehicule> vehicules =
                vehiculeRepository.findByConducteurId(conducteurId);

        // 3. Convert entities to DTOs
        return vehicules.stream()
                .map(vehiculeMapper::toResponse)
                .toList();
    }




}
