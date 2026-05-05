package com.financheck.infrastructure.persistence;

import com.financheck.domain.entity.Categoria;
import com.financheck.domain.repository.CategoriaRepository;
import com.financheck.infrastructure.persistence.entity.CategoriaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, Long> {
    List<CategoriaEntity> findByUsuarioId(Long usuarioId);

    @Query("SELECT COUNT(t) > 0 FROM TransacaoEntity t WHERE t.categoriaId = :categoriaId")
    boolean existeTransacaoComCategoria(Long categoriaId);
}

@Component
@RequiredArgsConstructor
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private final CategoriaJpaRepository jpaRepository;

    @Override
    public Categoria salvar(Categoria categoria) {
        CategoriaEntity entity = toEntity(categoria);
        entity = jpaRepository.save(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Categoria> buscarPorUsuario(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Categoria atualizar(Categoria categoria) {
        CategoriaEntity entity = toEntity(categoria);
        entity = jpaRepository.save(entity);
        return toDomain(entity);
    }

    @Override
    public void remover(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean possuiTransacoes(Long categoriaId) {
        return jpaRepository.existeTransacaoComCategoria(categoriaId);
    }

    private CategoriaEntity toEntity(Categoria domain) {
        CategoriaEntity entity = new CategoriaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setUsuarioId(domain.getUsuarioId());
        return entity;
    }

    private Categoria toDomain(CategoriaEntity entity) {
        Categoria domain = new Categoria();
        domain.setId(entity.getId());
        domain.setNome(entity.getNome());
        domain.setDescricao(entity.getDescricao());
        domain.setUsuarioId(entity.getUsuarioId());
        return domain;
    }
}
