package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.utilisateur.UtilisateurCreateRequest;
import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;
import com.covoiturage.api.entity.Utilisateur;
import com.covoiturage.api.mapper.UtilisateurMapper;
import com.covoiturage.api.repository.UtilisateurRepository;
import com.covoiturage.api.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UtilisateurServiceImp implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurServiceImp(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }


    @Override
    @Transactional
    public UtilisateurResponse register(UtilisateurCreateRequest request){

    }
}
