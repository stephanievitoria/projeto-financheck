package com.financheck.usecase.transacao;

import com.financheck.domain.entity.Transacao;
import com.financheck.domain.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtualizarTransacaoUseCase {

    private final TransacaoRepository transacaoRepository;

    public Transacao execute(Long id, Transacao dados) {
        Transacao transacao = transacaoRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada: " + id));
        transacao.setTipo(dados.getTipo());
        transacao.setValor(dados.getValor());
        transacao.setData(dados.getData());
        transacao.setDescricao(dados.getDescricao());
        transacao.setCategoriaId(dados.getCategoriaId());
        return transacaoRepository.atualizar(transacao);
    }
}
