package com.closet.closetapi.controllers;

import com.closet.closetapi.models.Sneaker;
import com.closet.closetapi.services.SneakerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sneakers")
@Tag(name = "Sneakers", description = "Sneakers in the closet")
public class SneakerController {
    @Autowired
    private SneakerService sneakerService;

    @GetMapping
    @Operation(summary = "List all sneakers")
    public List<Sneaker> findAll() {
        return sneakerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sneaker by id")
    @ApiResponse(responseCode = "404", description = "Sneaker not found")
    public Sneaker findById(@PathVariable Integer id) {
        return sneakerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a sneaker")
    @ApiResponse(responseCode = "400", description = "Invalid sneaker data")
    public Sneaker create(@Valid @RequestBody Sneaker sneaker) {
        return sneakerService.create(sneaker);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a sneaker")
    @ApiResponse(responseCode = "400", description = "Invalid sneaker data")
    @ApiResponse(responseCode = "404", description = "Sneaker not found")
    public Sneaker update(@PathVariable Integer id, @Valid @RequestBody Sneaker sneaker) {
        return sneakerService.update(id, sneaker);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a sneaker")
    @ApiResponse(responseCode = "404", description = "Sneaker not found")
    public void delete(@PathVariable Integer id) {
        sneakerService.delete(id);
    }
}
