package com.cboard.owlswap.owlswap_backend.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record RatingRequest(
        @DecimalMin("0.0") @DecimalMax("5.0") double rating
) {}
