package com.financheck.usecase.transacao;

import com.financheck.domain.entity.enums.TipoTransacao;
import com.financheck.domain.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CalcularSaldoUseCase {

    private final TransacaoRepository repository;

    public BigDecimal execute(Long usuarioId, LocalDate inicio, LocalDate fim) {
        var transacoes = repository.buscarPorPeriodo(usuarioId, inicio, fim);

        return transacoes.stream()
                .map(t -> t.getTipo() == TipoTransacao.RECEITA
                        ? t.getValor()
                        : t.getValor().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
