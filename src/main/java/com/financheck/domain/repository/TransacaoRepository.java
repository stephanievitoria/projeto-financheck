package com.financheck.domain.repository;

import com.financheck.domain.entity.Transacao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransacaoRepository {
    Transacao salvar(Transacao transacao);
    Optional<Transacao> buscarPorId(Long id);
    List<Transacao> buscarPorPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim);
    Transacao atualizar(Transacao transacao);
    void remover(Long id);
    List<Transacao> buscarComFiltros(Long usuarioId, LocalDate inicio, LocalDate fim, Long categoriaId, String descricao);
}
