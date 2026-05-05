package com.financheck.usecase.transacao;

import com.financheck.domain.entity.Transacao;
import com.financheck.domain.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BuscarTransacoesPorPeriodoUseCase {

    private final TransacaoRepository transacaoRepository;

    public List<Transacao> execute(Long usuarioId, LocalDate inicio, LocalDate fim) {
        return transacaoRepository.buscarPorPeriodo(usuarioId, inicio, fim);
    }
}
