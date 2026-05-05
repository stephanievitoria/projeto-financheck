package com.financheck.usecase.usuario;

import com.financheck.domain.entity.Usuario;
import com.financheck.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public Usuario execute(Long id, String nome, String email) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        usuario.setNome(nome);
        usuario.setEmail(email);
        return usuarioRepository.atualizar(usuario);
    }
}
