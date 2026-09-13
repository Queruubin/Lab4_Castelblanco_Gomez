package co.edu.eci.blueprints.dto;

import co.edu.eci.blueprints.model.Point;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * DTO de entrada para la creación de un {@code Blueprint} (POST /api/v1/blueprints).
 */
public record NewBlueprintRequest(
        @NotBlank(message = "author must not be blank") String author,
        @NotBlank(message = "name must not be blank") String name,
        @Valid List<Point> points
) { }
