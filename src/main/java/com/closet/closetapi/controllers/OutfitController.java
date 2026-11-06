package com.closet.closetapi.controllers;

import com.closet.closetapi.models.Outfit;
import com.closet.closetapi.models.OutfitRequest;
import com.closet.closetapi.services.OutfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outfits")
@Tag(name = "Outfits", description = "Combinations of closet pieces")
public class OutfitController {
    @Autowired
    private OutfitService outfitService;

    @GetMapping
    @Operation(summary = "List all outfits with their pieces")
    public List<Outfit> findAll() {
        return outfitService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an outfit by id")
    @ApiResponse(responseCode = "404", description = "Outfit not found")
    public Outfit findById(@PathVariable Integer id) {
        return outfitService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an outfit from existing pieces")
    @ApiResponse(responseCode = "400", description = "Invalid data or a referenced piece does not exist")
    public Outfit create(@Valid @RequestBody OutfitRequest request) {
        return outfitService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace the name and pieces of an outfit")
    @ApiResponse(responseCode = "400", description = "Invalid data or a referenced piece does not exist")
    @ApiResponse(responseCode = "404", description = "Outfit not found")
    public Outfit update(@PathVariable Integer id, @Valid @RequestBody OutfitRequest request) {
        return outfitService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove an outfit (its pieces stay in the closet)")
    @ApiResponse(responseCode = "404", description = "Outfit not found")
    public void delete(@PathVariable Integer id) {
        outfitService.delete(id);
    }
}
