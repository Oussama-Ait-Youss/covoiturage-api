package com.covoiturage.api.repository;

import com.covoiturage.api.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculeRepository extends JpaRepository<Vehicule,Long> {
    boolean existsByImmatriculation(String immatriculation);

}
