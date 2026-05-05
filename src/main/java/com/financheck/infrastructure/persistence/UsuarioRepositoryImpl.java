package com.financheck.infrastructure.persistence;

import com.financheck.domain.entity.Usuario;
import com.financheck.domain.repository.UsuarioRepository;
import com.financheck.infrastructure.persistence.entity.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);
}

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = toEntity(usuario);
        entity = jpaRepository.save(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        UsuarioEntity entity = toEntity(usuario);
        entity = jpaRepository.save(entity);
        return toDomain(entity);
    }

    @Override
    public void desativar(Long id) {
        UsuarioEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        entity.setAtivo(false);
        jpaRepository.save(entity);
    }

    private UsuarioEntity toEntity(Usuario domain) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEmail(domain.getEmail());
        entity.setSenha(domain.getSenha());
        entity.setAtivo(domain.isAtivo());
        return entity;
    }

    private Usuario toDomain(UsuarioEntity entity) {
        Usuario domain = new Usuario();
        domain.setId(entity.getId());
        domain.setNome(entity.getNome());
        domain.setEmail(entity.getEmail());
        domain.setSenha(entity.getSenha());
        domain.setAtivo(entity.isAtivo());
        return domain;
    }
}
