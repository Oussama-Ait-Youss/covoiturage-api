
package com.covoiturage.api.service;

import com.covoiturage.api.dto.trajet.*;

import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TrajetService {

    TrajetResponse createTrajet(TrajetCreateRequest request);

    TrajetResponse getTrajetById(Long id);

    List<TrajetResponse> getAllTrajets();

    List<TrajetResponse> getTrajetsByConducteur(Long conducteurId);

    Page<TrajetResponse> searchTrajets(
            String villeDepart,
            String villeArrivee,
            LocalDate date,
            Integer nombrePlaces,
            BigDecimal prixMax,
            int page,
            int size
    );

    TrajetResponse updateTrajet(
            Long id,
            TrajetUpdateRequest request
    );

    void cancelTrajet(Long id);

    TrajetResponse terminerTrajet(Long id);
}
