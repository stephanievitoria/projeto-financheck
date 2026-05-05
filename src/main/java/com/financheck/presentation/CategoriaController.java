package com.financheck.presentation;

import com.financheck.domain.entity.Categoria;
import com.financheck.presentation.dto.CategoriaDTO;
import com.financheck.usecase.categoria.AtualizarCategoriaUseCase;
import com.financheck.usecase.categoria.CriarCategoriaUseCase;
import com.financheck.usecase.categoria.ListarCategoriasUseCase;
import com.financheck.usecase.categoria.RemoverCategoriaUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CriarCategoriaUseCase criarCategoriaUseCase;
    private final AtualizarCategoriaUseCase atualizarCategoriaUseCase;
    private final RemoverCategoriaUseCase removerCategoriaUseCase;
    private final ListarCategoriasUseCase listarCategoriasUseCase;

    @PostMapping
    public ResponseEntity<Categoria> criar(@Valid @RequestBody CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        categoria.setUsuarioId(dto.getUsuarioId());
        Categoria saved = criarCategoriaUseCase.execute(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaDTO dto) {
        Categoria dados = new Categoria();
        dados.setNome(dto.getNome());
        dados.setDescricao(dto.getDescricao());
        Categoria updated = atualizarCategoriaUseCase.execute(id, dados);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        removerCategoriaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar(@RequestParam Long usuarioId) {
        List<Categoria> categorias = listarCategoriasUseCase.execute(usuarioId);
        return ResponseEntity.ok(categorias);
    }
}
