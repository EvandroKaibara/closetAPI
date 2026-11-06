package com.closet.closetapi.controllers;

import com.closet.closetapi.models.Pants;
import com.closet.closetapi.services.PantsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pants")
@Tag(name = "Pants", description = "Pants in the closet")
public class PantsController {
    @Autowired
    private PantsService pantsService;

    @GetMapping
    @Operation(summary = "List all pants")
    public List<Pants> findAll() {
        return pantsService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pants by id")
    @ApiResponse(responseCode = "404", description = "Pants not found")
    public Pants findById(@PathVariable Integer id) {
        return pantsService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add pants")
    @ApiResponse(responseCode = "400", description = "Invalid pants data")
    public Pants create(@Valid @RequestBody Pants pants) {
        return pantsService.create(pants);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pants")
    @ApiResponse(responseCode = "400", description = "Invalid pants data")
    @ApiResponse(responseCode = "404", description = "Pants not found")
    public Pants update(@PathVariable Integer id, @Valid @RequestBody Pants pants) {
        return pantsService.update(id, pants);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove pants")
    @ApiResponse(responseCode = "404", description = "Pants not found")
    public void delete(@PathVariable Integer id) {
        pantsService.delete(id);
    }
}
