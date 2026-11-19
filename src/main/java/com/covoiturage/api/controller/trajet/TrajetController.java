
package com.covoiturage.api.controller.trajet;

import com.covoiturage.api.dto.trajet.*;
import com.covoiturage.api.service.TrajetService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trajets")
public class TrajetController {

    private final TrajetService trajetService;

    public TrajetController(TrajetService trajetService) {
        this.trajetService = trajetService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrajetResponse createTrajet(
            @Valid @RequestBody TrajetCreateRequest request) {

        return trajetService.createTrajet(request);
    }

    // GET ALL
    @GetMapping
    public List<TrajetResponse> getAllTrajets() {

        return trajetService.getAllTrajets();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public TrajetResponse getTrajetById(
            @PathVariable Long id) {

        return trajetService.getTrajetById(id);
    }

    // GET BY CONDUCTEUR
    @GetMapping("/conducteurs/{conducteurId}")
    public List<TrajetResponse> getTrajetsByConducteur(
            @PathVariable Long conducteurId) {

        return trajetService.getTrajetsByConducteur(conducteurId);
    }

    // SEARCH
    @GetMapping("/search")
    public Page<TrajetResponse> searchTrajets(
            @RequestParam String villeDepart,
            @RequestParam String villeArrivee,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(defaultValue = "1") Integer nombrePlaces,
            @RequestParam(required = false) BigDecimal prixMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return trajetService.searchTrajets(
                villeDepart,
                villeArrivee,
                date,
                nombrePlaces,
                prixMax,
                page,
                size
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public TrajetResponse updateTrajet(
            @PathVariable Long id,
            @Valid @RequestBody TrajetUpdateRequest request) {

        return trajetService.updateTrajet(id, request);
    }

    // CANCEL
    @PatchMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTrajet(
            @PathVariable Long id) {

        trajetService.cancelTrajet(id);
    }

    // FINISH
    @PatchMapping("/{id}/finish")
    public TrajetResponse terminerTrajet(
            @PathVariable Long id) {

        return trajetService.terminerTrajet(id);
    }
}
