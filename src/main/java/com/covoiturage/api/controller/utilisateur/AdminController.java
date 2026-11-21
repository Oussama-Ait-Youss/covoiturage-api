
package com.covoiturage.api.controller.utilisateur;

import com.covoiturage.api.dto.utilisateur.*;
import com.covoiturage.api.service.UtilisateurService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final UtilisateurService utilisateurService;

    public AdminController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurResponse createAdmin(
            @Valid @RequestBody AdminCreateRequest request) {

        return utilisateurService.createAdmin(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public UtilisateurResponse updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateRequest request) {

        return utilisateurService.updateAdmin(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdmin(@PathVariable Long id) {

        utilisateurService.deleteAdmin(id);
    }

    // GET ADMIN BY ID
    @GetMapping("/{id}")
    public UtilisateurResponse getAdminById(@PathVariable Long id) {

        return utilisateurService.getAdminById(id);
    }

    // GET ALL ADMINS
    @GetMapping
    public List<UtilisateurResponse> getAllAdmins() {

        return utilisateurService.getAllAdmins();
    }

}
