
package com.covoiturage.api.repository;

import com.covoiturage.api.entity.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConducteurRepository
        extends JpaRepository<Conducteur, Long> {

    boolean existsByNumeroPermis(String numeroPermis);
    boolean existsByNumeroPermisAndIdNot(
            String numeroPermis,
            Long id
    );

    // NEW: SEARCH BY NOM OR EMAIL
    List<Conducteur> findByNomContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String nom,
            String email
    );
}
