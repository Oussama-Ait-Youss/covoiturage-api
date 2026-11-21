package com.covoiturage.api.service;

import com.covoiturage.api.dto.vehicule.VehiculeCreateRequest;
import com.covoiturage.api.dto.vehicule.VehiculeResponse;
import com.covoiturage.api.dto.vehicule.VehiculeUpdateRequest;
import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.Vehicule;
import com.covoiturage.api.exception.CapacityNumberInvalideException;
import com.covoiturage.api.exception.ConducteurNotFoundException;
import com.covoiturage.api.exception.VehiculeAlreadyExists;
import com.covoiturage.api.exception.VehiculeNotFoundException;
import com.covoiturage.api.mapper.VehiculeMapper;
import com.covoiturage.api.repository.ConducteurRepository;
import com.covoiturage.api.repository.TrajetRepository;
import com.covoiturage.api.repository.UtilisateurRepository;
import com.covoiturage.api.repository.VehiculeRepository;
import com.covoiturage.api.service.Imp.VehiculeServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehiculeServiceImpTest {

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private ConducteurRepository conducteurRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private VehiculeMapper vehiculeMapper;

    @Mock
    private TrajetRepository trajetRepository;

    @InjectMocks
    private VehiculeServiceImp vehiculeService;

    private Conducteur conducteur;
    private Vehicule vehicule;
    private VehiculeCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        conducteur = new Conducteur();
        conducteur.setId(1L);
        conducteur.setActif(true);

        vehicule = new Vehicule();
        vehicule.setId(10L);
        vehicule.setMarque("Dacia");
        vehicule.setModele("Logan");
        vehicule.setImmatriculation("123-ABC");
        vehicule.setCouleur("Blue");
        vehicule.setCapacite(4);
        vehicule.setConducteur(conducteur);

        createRequest = new VehiculeCreateRequest(
                "Dacia", "Logan", "123-ABC", "Blue", 4, 1L
        );
    }

    @Test
    void shouldCreateVehicleSuccessfully() {
        VehiculeResponse response = new VehiculeResponse(
                10L, "Dacia", "Logan", "123-ABC", "Blue", 4, 1L
        );

        when(vehiculeRepository.existsByImmatriculation("123-ABC")).thenReturn(false);
        when(conducteurRepository.findById(1L)).thenReturn(Optional.of(conducteur));
        when(vehiculeMapper.toEntity(createRequest)).thenReturn(vehicule);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        VehiculeResponse result = vehiculeService.createVehicule(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.immatriculation()).isEqualTo("123-ABC");
        assertThat(result.conducteurId()).isEqualTo(1L);
        verify(vehiculeRepository).save(vehicule);
    }

    @Test
    void shouldThrowExceptionWhenDriverNotFoundForCreation() {
        when(vehiculeRepository.existsByImmatriculation("123-ABC")).thenReturn(false);
        when(conducteurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculeService.createVehicule(createRequest))
                .isInstanceOf(ConducteurNotFoundException.class);
    }

    @Test
    void shouldRefuseDuplicateRegistrationNumber() {
        when(vehiculeRepository.existsByImmatriculation("123-ABC")).thenReturn(true);

        assertThatThrownBy(() -> vehiculeService.createVehicule(createRequest))
                .isInstanceOf(VehiculeAlreadyExists.class);
    }

    @Test
    void shouldRejectInvalidCapacityDuringUpdate() {
        VehiculeUpdateRequest request = new VehiculeUpdateRequest(
                "Dacia", "Logan", "123-ABC", "Blue", 9, 1L
        );

        when(vehiculeRepository.findById(10L)).thenReturn(Optional.of(vehicule));
        when(conducteurRepository.findById(1L)).thenReturn(Optional.of(conducteur));

        assertThatThrownBy(() -> vehiculeService.updateVehicule(10L, request))
                .isInstanceOf(CapacityNumberInvalideException.class);
    }

    @Test
    void shouldUpdateVehicleSuccessfully() {
        VehiculeUpdateRequest request = new VehiculeUpdateRequest(
                "Renault", "Clio", "456-DEF", "Red", 5, 1L
        );
        VehiculeResponse response = new VehiculeResponse(
                10L, "Renault", "Clio", "456-DEF", "Red", 5, 1L
        );

        when(vehiculeRepository.findById(10L)).thenReturn(Optional.of(vehicule));
        when(vehiculeRepository.existsByImmatriculation("456-DEF")).thenReturn(false);
        when(conducteurRepository.findById(1L)).thenReturn(Optional.of(conducteur));
        when(trajetRepository.existsByVehiculeIdAndPlacesProposeesGreaterThan(10L, 5)).thenReturn(false);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        VehiculeResponse result = vehiculeService.updateVehicule(10L, request);

        assertThat(result).isNotNull();
        assertThat(result.marque()).isEqualTo("Renault");
        assertThat(result.capacite()).isEqualTo(5);
        verify(vehiculeRepository).save(vehicule);
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFoundForUpdate() {
        VehiculeUpdateRequest request = new VehiculeUpdateRequest(
                "Dacia", "Logan", "123-ABC", "Blue", 4, 1L
        );
        when(vehiculeRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculeService.updateVehicule(10L, request))
                .isInstanceOf(VehiculeNotFoundException.class);
    }

    @Test
    void shouldDeleteExistingVehicle() {
        when(vehiculeRepository.findById(10L)).thenReturn(Optional.of(vehicule));

        vehiculeService.deleteVehicule(10L);

        verify(vehiculeRepository).delete(vehicule);
        verify(vehiculeRepository).flush();
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFoundForDeletion() {
        when(vehiculeRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculeService.deleteVehicule(10L))
                .isInstanceOf(VehiculeNotFoundException.class);
    }

    @Test
    void shouldGetVehicleById() {
        VehiculeResponse response = new VehiculeResponse(
                10L, "Dacia", "Logan", "123-ABC", "Blue", 4, 1L
        );
        when(vehiculeRepository.findById(10L)).thenReturn(Optional.of(vehicule));
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        VehiculeResponse result = vehiculeService.getVehiculeById(10L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.immatriculation()).isEqualTo("123-ABC");
        assertThat(result.conducteurId()).isEqualTo(1L);
    }
}
