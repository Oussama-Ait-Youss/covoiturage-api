package com.covoiturage.api.entity;


import jakarta.persistence.*;
import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Utilisateur {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String nom;

    @Column(nullable = false,length = 100)
    private String prenom;

    @Column(nullable = false,length = 100,unique = true)
    private String email;


    @Column(nullable = false, length = 20)
    private String telephone;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(nullable = false)
    private boolean isActif = true;

}
