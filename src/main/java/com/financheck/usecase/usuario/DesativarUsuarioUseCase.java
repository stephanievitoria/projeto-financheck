package com.financheck.usecase.usuario;

import com.financheck.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DesativarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public void execute(Long id) {
        usuarioRepository.desativar(id);
    }
}
