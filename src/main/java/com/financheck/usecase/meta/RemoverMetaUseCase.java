package com.financheck.usecase.meta;

import com.financheck.domain.repository.MetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoverMetaUseCase {

    private final MetaRepository metaRepository;

    public void execute(Long id) {
        metaRepository.remover(id);
    }
}
