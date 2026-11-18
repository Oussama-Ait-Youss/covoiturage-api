package com.covoiturage.api.repository;

import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {
    boolean existsByEmail(String email);
}
