package com.closet.closetapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Outfit to save. Pieces are referenced by id; every piece is optional.")
public record OutfitRequest(
        @NotBlank @Schema(example = "Weekend") String name,
        @Schema(example = "1") Integer sneakerId,
        @Schema(example = "1") Integer tshirtId,
        @Schema(example = "1") Integer pantsId,
        @Schema(example = "1") Integer hoodieId,
        @Schema(example = "1") Integer jacketId,
        @Schema(example = "[1]") List<@NotNull Integer> accessoryIds) {
}
