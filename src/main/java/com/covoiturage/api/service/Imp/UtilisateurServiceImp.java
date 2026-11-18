package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;
import com.covoiturage.api.entity.Utilisateur;
import com.covoiturage.api.repository.UtilisateurRepository;
import com.covoiturage.api.service.UtilisateurService;
import com.covoiturage.api.service.exception.EmailAlreadyExistsException;
import com.covoiturage.api.service.exception.InvalideUtilisateurTypeException;
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

        //check if the email already exists
        if(utilisateurRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException("Email Already Exists");
        }

        Utilisateur utilisateur;

        switch (request.typeUtilisateur()){
            case PASSAGER -> utilisateur = utilisateurMapper.toPassager(request);
            case CONDUCTEUR -> utilisateur = utilisateurMapper.toConducteur(request);
            case ADMIN -> utilisateur = utilisateurMapper.toAdmin(request);
            default -> throw new InvalideUtilisateurTypeException("Invalide user type");
        }

        Utilisateur saveUtilisateur = utilisateurRepository.save(utilisateur);

        return utilisateurMapper.toResponse(saveUtilisateur);
    }
}
