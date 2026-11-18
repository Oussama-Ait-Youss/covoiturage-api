
package com.covoiturage.api.repository;

import com.covoiturage.api.entity.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConducteurRepository
        extends JpaRepository<Conducteur, Long> {

    boolean existsByNumeroPermis(String numeroPermis);
    boolean existsByNumeroPermisAndIdNot(
            String numeroPermis,
            Long id
    );
}
