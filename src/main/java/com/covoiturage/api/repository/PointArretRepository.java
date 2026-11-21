
package com.covoiturage.api.repository;

import com.covoiturage.api.entity.PointArret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointArretRepository
        extends JpaRepository<PointArret, Long> {

    List<PointArret> findByTrajetIdOrderByOrdreAsc(Long trajetId);
}
