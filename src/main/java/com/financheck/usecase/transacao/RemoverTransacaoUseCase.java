package com.financheck.usecase.transacao;

import com.financheck.domain.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoverTransacaoUseCase {

    private final TransacaoRepository transacaoRepository;

    public void execute(Long id) {
        transacaoRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada: " + id));
        transacaoRepository.remover(id);
    }
}
