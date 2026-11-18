
package com.covoiturage.api.service;

import com.covoiturage.api.dto.utilisateur.PassagerCreateRequest;
import com.covoiturage.api.dto.utilisateur.ConducteurCreateRequest;
import com.covoiturage.api.dto.utilisateur.AdminCreateRequest;
import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;

public interface UtilisateurService {

    UtilisateurResponse createPassager(PassagerCreateRequest request);

    UtilisateurResponse createConducteur(ConducteurCreateRequest request);

    UtilisateurResponse createAdmin(AdminCreateRequest request);
}
