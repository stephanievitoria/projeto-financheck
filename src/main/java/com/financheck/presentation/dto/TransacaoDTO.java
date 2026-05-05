package com.financheck.presentation.dto;

import com.financheck.domain.entity.enums.TipoTransacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransacaoDTO {
    private Long id;

    @NotNull
    private TipoTransacao tipo;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
    private LocalDate data;

    private String descricao;

    @NotNull
    private Long categoriaId;

    @NotNull
    private Long usuarioId;
}
