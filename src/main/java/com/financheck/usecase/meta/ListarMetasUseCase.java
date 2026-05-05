package com.financheck.usecase.meta;

import com.financheck.domain.entity.MetaFinanceira;
import com.financheck.domain.repository.MetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListarMetasUseCase {

    private final MetaRepository metaRepository;

    public List<MetaFinanceira> execute(Long usuarioId) {
        return metaRepository.buscarPorUsuario(usuarioId);
    }
}
