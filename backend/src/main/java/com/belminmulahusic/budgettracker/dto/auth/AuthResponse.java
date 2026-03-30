package com.belminmulahusic.budgettracker.dto.auth;

public record AuthResponse(
        String token,
        String email
) {
}