package com.cboard.owlswap.owlswap_backend.exception_handler;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        String code,
        String message,
        int status,
        String path,
        Instant timestamp,
        Map<String, String> fieldErrors
) {
}
