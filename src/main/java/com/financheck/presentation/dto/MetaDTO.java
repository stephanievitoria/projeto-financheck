package com.financheck.presentation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MetaDTO {
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    @Positive
    private BigDecimal valorAlvo;

    private BigDecimal valorAcumulado;

    @NotNull
    @Future
    private LocalDate prazo;

    @NotNull
    private Long usuarioId;

    private double progresso;
}
