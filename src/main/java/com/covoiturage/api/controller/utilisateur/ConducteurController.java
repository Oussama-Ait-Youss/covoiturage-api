
package com.covoiturage.api.controller.utilisateur;

import com.covoiturage.api.dto.utilisateur.*;
import com.covoiturage.api.service.UtilisateurService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conducteurs")
public class ConducteurController {

    private final UtilisateurService utilisateurService;

    public ConducteurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurResponse createConducteur(
            @Valid @RequestBody ConducteurCreateRequest request) {

        return utilisateurService.createConducteur(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public UtilisateurResponse updateConducteur(
            @PathVariable Long id,
            @Valid @RequestBody ConducteurUpdateRequest request) {

        return utilisateurService.updateConducteur(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConducteur(@PathVariable Long id) {

        utilisateurService.deleteConducteur(id);
    }


    // GET CONDUCTEUR BY ID
    @GetMapping("/{id}")
    public UtilisateurResponse getConducteurById(@PathVariable Long id) {

        return utilisateurService.getConducteurById(id);
    }

    // GET ALL CONDUCTEURS
    @GetMapping
    public List<UtilisateurResponse> getAllConducteurs() {

        return utilisateurService.getAllConducteurs();
    }

}
