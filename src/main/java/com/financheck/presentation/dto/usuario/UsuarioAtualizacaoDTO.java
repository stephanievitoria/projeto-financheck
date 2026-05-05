package com.financheck.presentation.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioAtualizacaoDTO {
    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;
}
