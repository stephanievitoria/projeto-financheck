package com.financheck.usecase.categoria;

import com.financheck.domain.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoverCategoriaUseCase {

    private final CategoriaRepository categoriaRepository;

    public void execute(Long id) {
        if (categoriaRepository.possuiTransacoes(id)) {
            throw new RuntimeException("Categoria possui transações vinculadas. Reclassifique antes de remover.");
        }
        categoriaRepository.remover(id);
    }
}
