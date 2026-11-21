
package com.covoiturage.api.service.Imp;

import com.covoiturage.api.dto.utilisateur.*;
import com.covoiturage.api.entity.Admin;
import com.covoiturage.api.entity.Conducteur;
import com.covoiturage.api.entity.Passager;
import com.covoiturage.api.entity.Utilisateur;

import com.covoiturage.api.exception.EmailAlreadyExistsException;
import com.covoiturage.api.exception.NumeroPermisAlreadyExistsException;
import com.covoiturage.api.exception.PassagerNotFoundException;
import com.covoiturage.api.exception.UtilisateurNotFoundException;

import com.covoiturage.api.mapper.AdminMapper;
import com.covoiturage.api.mapper.ConducteurMapper;
import com.covoiturage.api.mapper.PassagerMapper;

import com.covoiturage.api.repository.AdminRepository;
import com.covoiturage.api.repository.ConducteurRepository;
import com.covoiturage.api.repository.PassagerRepository;
import com.covoiturage.api.repository.UtilisateurRepository;
import com.covoiturage.api.service.UtilisateurService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UtilisateurServiceImp implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final ConducteurRepository conducteurRepository;
    private final PassagerRepository passagerRepository;
    private final AdminRepository adminRepository;

    private final PassagerMapper passagerMapper;
    private final ConducteurMapper conducteurMapper;
    private final AdminMapper adminMapper;

    public UtilisateurServiceImp(
            UtilisateurRepository utilisateurRepository,
            ConducteurRepository conducteurRepository, PassagerRepository passagerRepository, AdminRepository adminRepository,
            PassagerMapper passagerMapper,
            ConducteurMapper conducteurMapper,
            AdminMapper adminMapper) {

        this.utilisateurRepository = utilisateurRepository;
        this.conducteurRepository = conducteurRepository;
        this.passagerRepository = passagerRepository;
        this.adminRepository = adminRepository;
        this.passagerMapper = passagerMapper;
        this.conducteurMapper = conducteurMapper;
        this.adminMapper = adminMapper;
    }

    // ========== CREATE PASSAGER ==========

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

    // ========== CREATE CONDUCTEUR ==========

    @Override
    @Transactional
    public UtilisateurResponse createConducteur(
            ConducteurCreateRequest request) {

        if (utilisateurRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (conducteurRepository.existsByNumeroPermis(request.numeroPermis())) {
            throw new NumeroPermisAlreadyExistsException(
                    "Numero de permis already exists"
            );
        }

        Conducteur conducteur = conducteurMapper.toEntity(request);

        Conducteur savedConducteur = utilisateurRepository.save(conducteur);

        return conducteurMapper.toResponse(savedConducteur);
    }

    // ========== CREATE ADMIN ==========

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

    // ========== UPDATE PASSAGER ==========

    @Override
    @Transactional
    public UtilisateurResponse updatePassager(
            Long id,
            PassagerUpdateRequest request) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(
                        "Passager not found"
                ));

        if (!(utilisateur instanceof Passager)) {
            throw new UtilisateurNotFoundException("Passager not found");
        }

        if (utilisateurRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Passager passager = (Passager) utilisateur;

        passagerMapper.updateEntity(request, passager);

        Passager savedPassager = utilisateurRepository.save(passager);

        return passagerMapper.toResponse(savedPassager);
    }

    // ========== UPDATE CONDUCTEUR ==========

    @Override
    @Transactional
    public UtilisateurResponse updateConducteur(
            Long id,
            ConducteurUpdateRequest request) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(
                        "Conducteur not found"
                ));

        if (!(utilisateur instanceof Conducteur)) {
            throw new UtilisateurNotFoundException("Conducteur not found");
        }

        if (utilisateurRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (conducteurRepository.existsByNumeroPermisAndIdNot(
                request.numeroPermis(), id)) {

            throw new NumeroPermisAlreadyExistsException(
                    "Numero de permis already exists"
            );
        }

        Conducteur conducteur = (Conducteur) utilisateur;

        conducteurMapper.updateEntity(request, conducteur);

        Conducteur savedConducteur = utilisateurRepository.save(conducteur);

        return conducteurMapper.toResponse(savedConducteur);
    }

    // ========== UPDATE ADMIN ==========

    @Override
    @Transactional
    public UtilisateurResponse updateAdmin(
            Long id,
            AdminUpdateRequest request) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(
                        "Admin not found"
                ));

        if (!(utilisateur instanceof Admin)) {
            throw new UtilisateurNotFoundException("Admin not found");
        }

        if (utilisateurRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Admin admin = (Admin) utilisateur;

        adminMapper.updateEntity(request, admin);

        Admin savedAdmin = utilisateurRepository.save(admin);

        return adminMapper.toResponse(savedAdmin);
    }

    // ========== DELETE PASSAGER ==========

    @Override
    @Transactional
    public void deletePassager(Long id) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(
                        "Passager not found"
                ));

        if (!(utilisateur instanceof Passager)) {
            throw new UtilisateurNotFoundException("Passager not found");
        }

        utilisateurRepository.delete(utilisateur);
        utilisateurRepository.flush();
    }

    // ========== DELETE CONDUCTEUR ==========

    @Override
    @Transactional
    public void deleteConducteur(Long id) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(
                        "Conducteur not found"
                ));

        if (!(utilisateur instanceof Conducteur)) {
            throw new UtilisateurNotFoundException("Conducteur not found");
        }

        utilisateurRepository.delete(utilisateur);
        utilisateurRepository.flush();
    }

    // ========== DELETE ADMIN ==========

    @Override
    @Transactional
    public void deleteAdmin(Long id) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException(
                        "Admin not found"
                ));

        if (!(utilisateur instanceof Admin)) {
            throw new UtilisateurNotFoundException("Admin not found");
        }

        utilisateurRepository.delete(utilisateur);
        utilisateurRepository.flush();
    }


    @Override
    @Transactional(readOnly = true)
    public UtilisateurResponse getPassagerById(Long id) {

        Passager passager = passagerRepository.findById(id)
                .orElseThrow(() ->
                        new UtilisateurNotFoundException("Passager not found")
                );

        return passagerMapper.toResponse(passager);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtilisateurResponse> getAllPassagers() {

        return passagerRepository.findAll()
                .stream()
                .map(passagerMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public UtilisateurResponse getConducteurById(Long id) {

        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() ->
                        new UtilisateurNotFoundException("Conducteur not found")
                );

        return conducteurMapper.toResponse(conducteur);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtilisateurResponse> getAllConducteurs() {

        return conducteurRepository.findAll()
                .stream()
                .map(conducteurMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public UtilisateurResponse getAdminById(Long id) {

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() ->
                        new UtilisateurNotFoundException("Admin not found")
                );

        return adminMapper.toResponse(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtilisateurResponse> getAllAdmins() {

        return adminRepository.findAll()
                .stream()
                .map(adminMapper::toResponse)
                .toList();
    }


// ========== SEARCH CONDUCTEUR BY NOM OR EMAIL ==========

    @Override
    @Transactional(readOnly = true)
    public List<UtilisateurResponse> searchConducteurs(String keyword) {

        return conducteurRepository
                .findByNomContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword,
                        keyword
                )
                .stream()
                .map(conducteurMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public UtilisateurResponse getPassagerByEmail(String email) {

        Passager passager = passagerRepository
                .findByEmailIgnoreCase(email.trim())
                .orElseThrow(() ->
                        new PassagerNotFoundException(
                                "Passenger not found with email: " + email
                        )
                );

        return passagerMapper.toResponse(passager);
    }



}
