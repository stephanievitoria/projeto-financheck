package com.financheck.usecase.meta;

import com.financheck.domain.entity.MetaFinanceira;
import com.financheck.domain.repository.MetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalcularProgressoMetaUseCase {

    private final MetaRepository metaRepository;

    public double execute(Long metaId) {
        MetaFinanceira meta = metaRepository.buscarPorId(metaId)
                .orElseThrow(() -> new RuntimeException("Meta não encontrada: " + metaId));
        return meta.calcularProgresso();
    }
}
