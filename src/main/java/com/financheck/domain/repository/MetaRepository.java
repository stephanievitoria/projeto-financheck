package com.financheck.domain.repository;

import com.financheck.domain.entity.MetaFinanceira;

import java.util.List;
import java.util.Optional;

public interface MetaRepository {
    MetaFinanceira salvar(MetaFinanceira meta);
    Optional<MetaFinanceira> buscarPorId(Long id);
    List<MetaFinanceira> buscarPorUsuario(Long usuarioId);
    MetaFinanceira atualizar(MetaFinanceira meta);
    void remover(Long id);
}
