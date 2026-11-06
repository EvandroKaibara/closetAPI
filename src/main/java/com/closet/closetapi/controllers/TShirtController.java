package com.closet.closetapi.controllers;

import com.closet.closetapi.models.TShirt;
import com.closet.closetapi.services.TShirtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tshirts")
@Tag(name = "T-shirts", description = "T-shirts in the closet")
public class TShirtController {
    @Autowired
    private TShirtService tShirtService;

    @GetMapping
    @Operation(summary = "List all t-shirts")
    public List<TShirt> findAll() {
        return tShirtService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a t-shirt by id")
    @ApiResponse(responseCode = "404", description = "T-shirt not found")
    public TShirt findById(@PathVariable Integer id) {
        return tShirtService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a t-shirt")
    @ApiResponse(responseCode = "400", description = "Invalid t-shirt data")
    public TShirt create(@Valid @RequestBody TShirt tShirt) {
        return tShirtService.create(tShirt);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a t-shirt")
    @ApiResponse(responseCode = "400", description = "Invalid t-shirt data")
    @ApiResponse(responseCode = "404", description = "T-shirt not found")
    public TShirt update(@PathVariable Integer id, @Valid @RequestBody TShirt tShirt) {
        return tShirtService.update(id, tShirt);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a t-shirt")
    @ApiResponse(responseCode = "404", description = "T-shirt not found")
    public void delete(@PathVariable Integer id) {
        tShirtService.delete(id);
    }
}
