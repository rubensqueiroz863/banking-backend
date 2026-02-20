package com.backend.banking_backend.dto;

public record UserResponse(
		String id,
		String email,
		String password,
		String name,
		String cpf
) {}