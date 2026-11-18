
package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.utilisateur.*;
import com.covoiturage.api.entity.Admin;
import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.Passager;

import com.covoiturage.api.mapper.AdminMapper;
import com.covoiturage.api.mapper.ConducteurMapper;
import com.covoiturage.api.mapper.PassagerMapper;

import com.covoiturage.api.repository.UtilisateurRepository;
import com.covoiturage.api.service.UtilisateurService;
import com.covoiturage.api.exception.EmailAlreadyExistsException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtilisateurServiceImp implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PassagerMapper passagerMapper;
    private final ConducteurMapper conducteurMapper;
    private final AdminMapper adminMapper;

    public UtilisateurServiceImp(
            UtilisateurRepository utilisateurRepository,
            PassagerMapper passagerMapper,
            ConducteurMapper conducteurMapper,
            AdminMapper adminMapper) {

        this.utilisateurRepository = utilisateurRepository;
        this.passagerMapper = passagerMapper;
        this.conducteurMapper = conducteurMapper;
        this.adminMapper = adminMapper;
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
