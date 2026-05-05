package com.financheck.usecase.meta;

import com.financheck.domain.entity.MetaFinanceira;
import com.financheck.domain.repository.MetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AtualizarMetaUseCase {

    private final MetaRepository metaRepository;

    public MetaFinanceira execute(Long id, MetaFinanceira dados) {
        MetaFinanceira meta = metaRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Meta não encontrada: " + id));

        meta.setNome(dados.getNome());
        meta.setValorAlvo(dados.getValorAlvo());
        meta.setPrazo(dados.getPrazo());

        return metaRepository.atualizar(meta);
    }

    public MetaFinanceira adicionarValor(Long id, BigDecimal valor) {
        MetaFinanceira meta = metaRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Meta não encontrada: " + id));

        BigDecimal novoValor = meta.getValorAcumulado().add(valor);

        // não ultrapassa o objetivo
        if (novoValor.compareTo(meta.getValorAlvo()) > 0) {
            novoValor = meta.getValorAlvo();
        }

        meta.setValorAcumulado(novoValor);

        return metaRepository.atualizar(meta);
    }

    public MetaFinanceira removerValor(Long id, BigDecimal valor) {
        MetaFinanceira meta = metaRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Meta não encontrada: " + id));

        BigDecimal novoValor = meta.getValorAcumulado().subtract(valor);

        if (novoValor.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Valor acumulado não pode ser negativo");
        }

        meta.setValorAcumulado(novoValor);

        return metaRepository.atualizar(meta);
    }
}
