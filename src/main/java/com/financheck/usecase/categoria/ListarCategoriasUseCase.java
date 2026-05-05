package com.financheck.usecase.categoria;

import com.financheck.domain.entity.Categoria;
import com.financheck.domain.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListarCategoriasUseCase {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> execute(Long usuarioId) {
        return categoriaRepository.buscarPorUsuario(usuarioId);
    }
}
