package org.autoservice.dto;

public record RegistrationRequest(String name, String email, String password, String role) {
}