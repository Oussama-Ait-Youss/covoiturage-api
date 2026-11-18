
package com.covoiturage.api.controller;

import com.covoiturage.api.dto.utilisateur.PassagerCreateRequest;
import com.covoiturage.api.dto.utilisateur.UtilisateurResponse;
import com.covoiturage.api.service.UtilisateurService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/passagers")
public class PassagerController {

    private final UtilisateurService utilisateurService;

    public PassagerController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurResponse createPassager(
            @Valid @RequestBody PassagerCreateRequest request) {

        return utilisateurService.createPassager(request);
    }
}
