package com.financheck.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetaFinanceira {
    private Long id;
    private String nome;
    private BigDecimal valorAlvo;
    private BigDecimal valorAcumulado;
    private LocalDate prazo;
    private Long usuarioId;

    public double calcularProgresso() {
        if (valorAlvo == null || valorAlvo.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return valorAcumulado.divide(valorAlvo, 4, RoundingMode.HALF_UP).doubleValue() * 100;
    }
}
