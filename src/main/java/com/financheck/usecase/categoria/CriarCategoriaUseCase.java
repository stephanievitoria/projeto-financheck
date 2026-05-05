package com.financheck.usecase.categoria;

import com.financheck.domain.entity.Categoria;
import com.financheck.domain.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CriarCategoriaUseCase {

    private final CategoriaRepository categoriaRepository;

    public Categoria execute(Categoria categoria) {
        return categoriaRepository.salvar(categoria);
    }
}
