
package com.covoiturage.api.repository;

import com.covoiturage.api.entity.Trajet;
import com.covoiturage.api.entity.enums.StatutTrajet;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "vehicule",
            "vehicule.conducteur",
            "pointArrets"
    })
    Optional<Trajet> findById(Long id);

    List<Trajet> findByVehiculeConducteurId(Long conducteurId);

    @Query(
            value = """
            SELECT t
            FROM Trajet t
            JOIN t.pointArrets depart
            JOIN t.pointArrets arrivee
            WHERE LOWER(depart.ville) = LOWER(:villeDepart)
              AND LOWER(arrivee.ville) = LOWER(:villeArrivee)
              AND depart.ordre < arrivee.ordre
              AND t.dateDepart >= :debut
              AND t.dateDepart < :fin
              AND t.status = :status
              AND (
                  :prixMax IS NULL
                  OR (arrivee.prixDepuisDepart - depart.prixDepuisDepart)
                     <= :prixMax
              )
              AND t.placesProposees - COALESCE(
                  (SELECT SUM(r.nombrePlaces)
                   FROM Reservation r
                   WHERE r.trajet = t
                     AND r.statut = 'CONFIRMEE'),
                  0
              ) >= :nombrePlaces
            """,
            countQuery = """
            SELECT COUNT(t)
            FROM Trajet t
            JOIN t.pointArrets depart
            JOIN t.pointArrets arrivee
            WHERE LOWER(depart.ville) = LOWER(:villeDepart)
              AND LOWER(arrivee.ville) = LOWER(:villeArrivee)
              AND depart.ordre < arrivee.ordre
              AND t.dateDepart >= :debut
              AND t.dateDepart < :fin
              AND t.status = :status
              AND (
                  :prixMax IS NULL
                  OR (arrivee.prixDepuisDepart - depart.prixDepuisDepart)
                     <= :prixMax
              )
              AND t.placesProposees - COALESCE(
                  (SELECT SUM(r.nombrePlaces)
                   FROM Reservation r
                   WHERE r.trajet = t
                     AND r.statut = 'CONFIRMEE'),
                  0
              ) >= :nombrePlaces
            """
    )
    Page<Trajet> searchTrajets(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin,
            @Param("status") StatutTrajet status,
            @Param("nombrePlaces") Integer nombrePlaces,
            @Param("prixMax") BigDecimal prixMax,
            Pageable pageable
    );


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Trajet t WHERE t.id = :id")
    Optional<Trajet> findByIdForUpdate(@Param("id") Long id);

}
