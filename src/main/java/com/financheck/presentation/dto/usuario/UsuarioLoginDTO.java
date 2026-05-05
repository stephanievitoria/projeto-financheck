package com.financheck.presentation.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioLoginDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;
}
