
package com.covoiturage.api.entity;

import com.covoiturage.api.entity.enums.StatutReservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer nombrePlaces;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Column(nullable = false)
    private LocalDateTime dateReservation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut;

    // Many reservations can belong to one passenger
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passager_id", nullable = false)
    private Passager passager;

    // Many reservations concern one trip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    // Boarding stop
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_montee_id", nullable = false)
    private PointArret pointMontee;

    // Drop-off stop
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_descente_id", nullable = false)
    private PointArret pointDescente;
}
