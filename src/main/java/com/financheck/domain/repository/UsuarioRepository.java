package com.financheck.domain.repository;

import com.financheck.domain.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    Usuario atualizar(Usuario usuario);
    void desativar(Long id);
}
