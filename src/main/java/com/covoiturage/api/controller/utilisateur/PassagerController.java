
package com.covoiturage.api.controller.utilisateur;

import com.covoiturage.api.dto.utilisateur.*;
import com.covoiturage.api.service.UtilisateurService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passagers")
public class PassagerController {

    private final UtilisateurService utilisateurService;

    public PassagerController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurResponse createPassager(
            @Valid @RequestBody PassagerCreateRequest request) {

        return utilisateurService.createPassager(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public UtilisateurResponse updatePassager(
            @PathVariable Long id,
            @Valid @RequestBody PassagerUpdateRequest request) {

        return utilisateurService.updatePassager(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassager(@PathVariable Long id) {

        utilisateurService.deletePassager(id);
    }

    // GET PASSAGER BY ID
    @GetMapping("/{id}")
    public UtilisateurResponse getPassagerById(@PathVariable Long id) {

        return utilisateurService.getPassagerById(id);
    }

    // GET ALL PASSAGERS
    @GetMapping
    public List<UtilisateurResponse> getAllPassagers() {

        return utilisateurService.getAllPassagers();
    }


    // SEARCH PASSAGER BY EMAIL
    @GetMapping("/search")
    public UtilisateurResponse searchPassagerByEmail(
            @RequestParam String email
    ) {
        return utilisateurService.getPassagerByEmail(email);
    }


}
