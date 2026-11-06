package com.closet.closetapi.controllers;

import com.closet.closetapi.models.Accessory;
import com.closet.closetapi.services.AccessoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accessories")
@Tag(name = "Accessories", description = "Accessories in the closet")
public class AccessoryController {
    @Autowired
    private AccessoryService accessoryService;

    @GetMapping
    @Operation(summary = "List all accessories")
    public List<Accessory> findAll() {
        return accessoryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an accessory by id")
    @ApiResponse(responseCode = "404", description = "Accessory not found")
    public Accessory findById(@PathVariable Integer id) {
        return accessoryService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add an accessory")
    @ApiResponse(responseCode = "400", description = "Invalid accessory data")
    public Accessory create(@Valid @RequestBody Accessory accessory) {
        return accessoryService.create(accessory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an accessory")
    @ApiResponse(responseCode = "400", description = "Invalid accessory data")
    @ApiResponse(responseCode = "404", description = "Accessory not found")
    public Accessory update(@PathVariable Integer id, @Valid @RequestBody Accessory accessory) {
        return accessoryService.update(id, accessory);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove an accessory")
    @ApiResponse(responseCode = "404", description = "Accessory not found")
    public void delete(@PathVariable Integer id) {
        accessoryService.delete(id);
    }
}
