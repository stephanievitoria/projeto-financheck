package com.financheck.usecase.meta;

import com.financheck.domain.entity.MetaFinanceira;
import com.financheck.domain.repository.MetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CriarMetaUseCase {

    private final MetaRepository metaRepository;

    public MetaFinanceira execute(MetaFinanceira meta) {
        meta.setValorAcumulado(BigDecimal.ZERO);
        return metaRepository.salvar(meta);
    }
}
