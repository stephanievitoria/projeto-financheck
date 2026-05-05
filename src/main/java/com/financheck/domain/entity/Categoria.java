package com.financheck.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    private Long id;
    private String nome;
    private String descricao;
    private Long usuarioId;
}
