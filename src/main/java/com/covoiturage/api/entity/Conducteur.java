package com.covoiturage.api.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "conducteur")
public class Conducteur extends Utilisateur{

    @Column(nullable = false,length = 50,unique = true)
    private String numeroPermis;

    @OneToMany(mappedBy = "conducteur")
    private List<Vehicule> vehiculeList;

}
