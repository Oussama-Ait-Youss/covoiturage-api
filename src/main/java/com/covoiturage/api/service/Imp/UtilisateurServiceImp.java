
package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.utilisateur.*;
import com.covoiturage.api.entity.Admin;
import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.Passager;

import com.covoiturage.api.entity.Utilisateur;
import com.covoiturage.api.exception.NumeroPermisAlreadyExistsException;
import com.covoiturage.api.mapper.AdminMapper;
import com.covoiturage.api.mapper.ConducteurMapper;
import com.covoiturage.api.mapper.PassagerMapper;

import com.covoiturage.api.repository.ConducteurRepository;
import com.covoiturage.api.repository.UtilisateurRepository;
import com.covoiturage.api.service.UtilisateurService;
import com.covoiturage.api.exception.EmailAlreadyExistsException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UtilisateurServiceImp implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PassagerMapper passagerMapper;
    private final ConducteurMapper conducteurMapper;
    private final AdminMapper adminMapper;
    private final ConducteurRepository conducteurRepository;

    public UtilisateurServiceImp(
            UtilisateurRepository utilisateurRepository,
            PassagerMapper passagerMapper,
            ConducteurMapper conducteurMapper,
            AdminMapper adminMapper, ConducteurRepository conducteurRepository) {

        this.utilisateurRepository = utilisateurRepository;
        this.passagerMapper = passagerMapper;
        this.conducteurMapper = conducteurMapper;
        this.adminMapper = adminMapper;
        this.conducteurRepository = conducteurRepository;
    }

    // CREATE PASSAGER
    @Override
    @Transactional
    public UtilisateurResponse createPassager(
            PassagerCreateRequest request) {

        if (utilisateurRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Passager passager = passagerMapper.toEntity(request);

        Passager savedPassager = utilisateurRepository.save(passager);

        return passagerMapper.toResponse(savedPassager);
    }

    // CREATE CONDUCTEUR
    @Override
    @Transactional
    public UtilisateurResponse createConducteur(
            ConducteurCreateRequest request) {

        if (utilisateurRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // 2. Check if numeroPermis already exists
        if (conducteurRepository.existsByNumeroPermis(request.numeroPermis())) {
            throw new NumeroPermisAlreadyExistsException(
                    "Numero de permis should be unique"
            );
        }

        Conducteur conducteur = conducteurMapper.toEntity(request);

        Conducteur savedConducteur = utilisateurRepository.save(conducteur);

        return conducteurMapper.toResponse(savedConducteur);
    }

    // CREATE ADMIN - TRUSTED USE ONLY
    @Override
    @Transactional
    public UtilisateurResponse createAdmin(
            AdminCreateRequest request) {

        if (utilisateurRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Admin admin = adminMapper.toEntity(request);

        Admin savedAdmin = utilisateurRepository.save(admin);

        return adminMapper.toResponse(savedAdmin);
    }
}
