
package com.covoiturage.api.controller.utilisateur;

import com.covoiturage.api.dto.utilisateur.ConducteurCreateRequest;
import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;
import com.covoiturage.api.service.UtilisateurService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conducteurs")
public class ConducteurController {

    private final UtilisateurService utilisateurService;

    public ConducteurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurResponse createConducteur(
            @Valid @RequestBody ConducteurCreateRequest request) {

        return utilisateurService.createConducteur(request);
    }
}
