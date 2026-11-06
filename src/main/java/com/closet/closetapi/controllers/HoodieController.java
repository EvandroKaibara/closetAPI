package com.closet.closetapi.controllers;

import com.closet.closetapi.models.Hoodie;
import com.closet.closetapi.services.HoodieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hoodies")
@Tag(name = "Hoodies", description = "Hoodies in the closet")
public class HoodieController {
    @Autowired
    private HoodieService hoodieService;

    @GetMapping
    @Operation(summary = "List all hoodies")
    public List<Hoodie> findAll() {
        return hoodieService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a hoodie by id")
    @ApiResponse(responseCode = "404", description = "Hoodie not found")
    public Hoodie findById(@PathVariable Integer id) {
        return hoodieService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a hoodie")
    @ApiResponse(responseCode = "400", description = "Invalid hoodie data")
    public Hoodie create(@Valid @RequestBody Hoodie hoodie) {
        return hoodieService.create(hoodie);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a hoodie")
    @ApiResponse(responseCode = "400", description = "Invalid hoodie data")
    @ApiResponse(responseCode = "404", description = "Hoodie not found")
    public Hoodie update(@PathVariable Integer id, @Valid @RequestBody Hoodie hoodie) {
        return hoodieService.update(id, hoodie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a hoodie")
    @ApiResponse(responseCode = "404", description = "Hoodie not found")
    public void delete(@PathVariable Integer id) {
        hoodieService.delete(id);
    }
}
