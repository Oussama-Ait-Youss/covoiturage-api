package com.covoiturage.api.entity;


import com.covoiturage.api.entity.enums.StatutTrajet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "trajet")
public class Trajet {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer placesProposees;

    @Column(nullable = false)
    private LocalDateTime dateCreate;



    @Enumerated(EnumType.STRING)
    private StatutTrajet status;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;


    @OneToMany(
            mappedBy = "trajet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("ordre ASC")
    private List<PointArret> pointArrets;

    @OneToMany(mappedBy = "trajet")
    private List<Reservation> reservationList;



}
