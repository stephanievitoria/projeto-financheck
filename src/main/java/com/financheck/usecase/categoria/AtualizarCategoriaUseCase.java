package com.financheck.usecase.categoria;

import com.financheck.domain.entity.Categoria;
import com.financheck.domain.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtualizarCategoriaUseCase {

    private final CategoriaRepository categoriaRepository;

    public Categoria execute(Long id, Categoria dados) {
        Categoria categoria = categoriaRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada: " + id));
        categoria.setNome(dados.getNome());
        categoria.setDescricao(dados.getDescricao());
        return categoriaRepository.atualizar(categoria);
    }
}
