package com.covoiturage.api.service;

import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;

public interface UtilisateurService {

    UtilisateurResponse register(UtilisateurCreateRequest request);

}