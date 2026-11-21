package com.covoiturage.api.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "passager")
public class Passager extends Utilisateur{




    @OneToMany(mappedBy = "passager")
    private List<Reservation> reservationList;
}
