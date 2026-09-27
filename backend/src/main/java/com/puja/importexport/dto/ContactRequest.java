package com.puja.importexport.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        @Pattern(regexp = "[^\\r\\n]*", message = "Name must not contain line breaks")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Size(max = 254, message = "Email must be at most 254 characters")
        String email,

        @Size(max = 100, message = "Country must be at most 100 characters")
        @Pattern(regexp = "[^\\r\\n]*", message = "Country must not contain line breaks")
        String country,

        @Size(max = 100, message = "Service needed must be at most 100 characters")
        @Pattern(regexp = "[^\\r\\n]*", message = "Service needed must not contain line breaks")
        String serviceNeeded,

        @Size(max = 2000, message = "Message must be at most 2000 characters")
        String message
) {
}
