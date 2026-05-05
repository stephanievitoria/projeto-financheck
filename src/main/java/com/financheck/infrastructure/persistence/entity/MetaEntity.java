package com.financheck.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "TB_META")
public class MetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "valor_alvo", nullable = false)
    private BigDecimal valorAlvo;

    @Column(name = "valor_acumulado")
    private BigDecimal valorAcumulado;

    @Column(nullable = false)
    private LocalDate prazo;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
}
