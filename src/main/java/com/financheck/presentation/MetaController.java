package com.financheck.presentation;

import com.financheck.domain.entity.MetaFinanceira;
import com.financheck.presentation.dto.MetaDTO;
import com.financheck.usecase.meta.AtualizarMetaUseCase;
import com.financheck.usecase.meta.CalcularProgressoMetaUseCase;
import com.financheck.usecase.meta.CriarMetaUseCase;
import com.financheck.usecase.meta.ListarMetasUseCase;
import com.financheck.usecase.meta.RemoverMetaUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/metas")
@RequiredArgsConstructor
public class MetaController {

    private final CriarMetaUseCase criarMetaUseCase;
    private final AtualizarMetaUseCase atualizarMetaUseCase;
    private final RemoverMetaUseCase removerMetaUseCase;
    private final ListarMetasUseCase listarMetasUseCase;
    private final CalcularProgressoMetaUseCase calcularProgressoMetaUseCase;

    @PostMapping
    public ResponseEntity<MetaFinanceira> criar(@Valid @RequestBody MetaDTO dto) {
        MetaFinanceira meta = new MetaFinanceira();
        meta.setNome(dto.getNome());
        meta.setValorAlvo(dto.getValorAlvo());
        meta.setPrazo(dto.getPrazo());
        meta.setUsuarioId(dto.getUsuarioId());
        MetaFinanceira saved = criarMetaUseCase.execute(meta);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaFinanceira> atualizar(@PathVariable Long id, @Valid @RequestBody MetaDTO dto) {
        MetaFinanceira dados = new MetaFinanceira();
        dados.setNome(dto.getNome());
        dados.setValorAlvo(dto.getValorAlvo());
        dados.setPrazo(dto.getPrazo());
        MetaFinanceira updated = atualizarMetaUseCase.execute(id, dados);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        removerMetaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MetaFinanceira>> listar(@RequestParam Long usuarioId) {
        List<MetaFinanceira> metas = listarMetasUseCase.execute(usuarioId);
        return ResponseEntity.ok(metas);
    }

    @GetMapping("/{id}/progresso")
    public ResponseEntity<Double> calcularProgresso(@PathVariable Long id) {
        double progresso = calcularProgressoMetaUseCase.execute(id);
        return ResponseEntity.ok(progresso);
    }

    @PatchMapping("/{id}/adicionar")
    public ResponseEntity<MetaFinanceira> adicionarValor(
            @PathVariable Long id,
            @RequestParam BigDecimal valor) {

        return ResponseEntity.ok(atualizarMetaUseCase.adicionarValor(id, valor));
    }

    @PatchMapping("/{id}/remover")
    public ResponseEntity<MetaFinanceira> removerValor(
            @PathVariable Long id,
            @RequestParam BigDecimal valor) {

        return ResponseEntity.ok(atualizarMetaUseCase.removerValor(id, valor));
    }
}
