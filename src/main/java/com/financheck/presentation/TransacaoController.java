package com.financheck.presentation;

import com.financheck.domain.entity.Transacao;
import com.financheck.presentation.dto.TransacaoDTO;
import com.financheck.usecase.transacao.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final RegistrarTransacaoUseCase registrarTransacaoUseCase;
    private final AtualizarTransacaoUseCase atualizarTransacaoUseCase;
    private final RemoverTransacaoUseCase removerTransacaoUseCase;
    private final BuscarTransacoesPorPeriodoUseCase buscarTransacoesPorPeriodoUseCase;
    private final CalcularSaldoUseCase calcularSaldoUseCase;

    @PostMapping
    public ResponseEntity<Transacao> registrar(@Valid @RequestBody TransacaoDTO dto) {
        Transacao transacao = new Transacao();
        transacao.setTipo(dto.getTipo());
        transacao.setValor(dto.getValor());
        transacao.setData(dto.getData());
        transacao.setDescricao(dto.getDescricao());
        transacao.setCategoriaId(dto.getCategoriaId());
        transacao.setUsuarioId(dto.getUsuarioId());
        Transacao saved = registrarTransacaoUseCase.execute(transacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transacao> atualizar(@PathVariable Long id, @Valid @RequestBody TransacaoDTO dto) {
        Transacao dados = new Transacao();
        dados.setTipo(dto.getTipo());
        dados.setValor(dto.getValor());
        dados.setData(dto.getData());
        dados.setDescricao(dto.getDescricao());
        dados.setCategoriaId(dto.getCategoriaId());
        Transacao updated = atualizarTransacaoUseCase.execute(id, dados);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        removerTransacaoUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> buscarPorPeriodo(
            @RequestParam Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<Transacao> transacoes = buscarTransacoesPorPeriodoUseCase.execute(usuarioId, inicio, fim);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> saldo(
            @RequestParam Long usuarioId,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(
                calcularSaldoUseCase.execute(usuarioId, inicio, fim)
        );
    }
}
