package com.financheck.domain.repository;

import com.financheck.domain.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
    Categoria salvar(Categoria categoria);
    Optional<Categoria> buscarPorId(Long id);
    List<Categoria> buscarPorUsuario(Long usuarioId);
    Categoria atualizar(Categoria categoria);
    void remover(Long id);
    boolean possuiTransacoes(Long categoriaId);
}
