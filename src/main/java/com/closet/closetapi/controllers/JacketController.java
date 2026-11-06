package com.closet.closetapi.controllers;

import com.closet.closetapi.models.Jacket;
import com.closet.closetapi.services.JacketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jackets")
@Tag(name = "Jackets", description = "Jackets in the closet")
public class JacketController {
    @Autowired
    private JacketService jacketService;

    @GetMapping
    @Operation(summary = "List all jackets")
    public List<Jacket> findAll() {
        return jacketService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a jacket by id")
    @ApiResponse(responseCode = "404", description = "Jacket not found")
    public Jacket findById(@PathVariable Integer id) {
        return jacketService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a jacket")
    @ApiResponse(responseCode = "400", description = "Invalid jacket data")
    public Jacket create(@Valid @RequestBody Jacket jacket) {
        return jacketService.create(jacket);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a jacket")
    @ApiResponse(responseCode = "400", description = "Invalid jacket data")
    @ApiResponse(responseCode = "404", description = "Jacket not found")
    public Jacket update(@PathVariable Integer id, @Valid @RequestBody Jacket jacket) {
        return jacketService.update(id, jacket);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a jacket")
    @ApiResponse(responseCode = "404", description = "Jacket not found")
    public void delete(@PathVariable Integer id) {
        jacketService.delete(id);
    }
}
