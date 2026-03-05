package com.cboard.owlswap.owlswap_backend.model.orders;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(@NotNull Integer itemId) {
}
