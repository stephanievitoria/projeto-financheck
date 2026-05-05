package com.financheck.domain.entity;

import com.financheck.domain.entity.enums.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transacao {
    private Long id;
    private TipoTransacao tipo;
    private BigDecimal valor;
    private LocalDate data;
    private String descricao;
    private Long categoriaId;
    private Long usuarioId;
}
