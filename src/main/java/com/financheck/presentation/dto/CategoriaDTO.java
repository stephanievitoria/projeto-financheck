package com.financheck.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;

    @NotBlank
    private String nome;

    private String descricao;

    @NotNull
    private Long usuarioId;
}
