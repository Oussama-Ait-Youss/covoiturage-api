
package com.covoiturage.api.service;

import com.covoiturage.api.dto.utilisateur.*;

import java.util.List;

public interface UtilisateurService {

    // CREATE
    UtilisateurResponse createPassager(PassagerCreateRequest request);
    UtilisateurResponse createConducteur(ConducteurCreateRequest request);
    UtilisateurResponse createAdmin(AdminCreateRequest request);

    // UPDATE
    UtilisateurResponse updatePassager(Long id, PassagerUpdateRequest request);
    UtilisateurResponse updateConducteur(Long id, ConducteurUpdateRequest request);
    UtilisateurResponse updateAdmin(Long id, AdminUpdateRequest request);

    // DELETE
    void deletePassager(Long id);
    void deleteConducteur(Long id);
    void deleteAdmin(Long id);

    // GET PASSAGERS
    UtilisateurResponse getPassagerById(Long id);
    List<UtilisateurResponse> getAllPassagers();

    // GET CONDUCTEURS
    UtilisateurResponse getConducteurById(Long id);
    List<UtilisateurResponse> getAllConducteurs();

    // GET ADMINS
    UtilisateurResponse getAdminById(Long id);
    List<UtilisateurResponse> getAllAdmins();

    // SEARCH CONDUCTEUR BY NOM OR EMAIL
    List<UtilisateurResponse> searchConducteurs(String keyword);

    // get passager by email
    UtilisateurResponse getPassagerByEmail(String email);

}
