package com.financheck.infrastructure.persistence;

import com.financheck.domain.entity.Transacao;
import com.financheck.domain.repository.TransacaoRepository;
import com.financheck.infrastructure.persistence.entity.TransacaoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface TransacaoJpaRepository extends JpaRepository<TransacaoEntity, Long> {
    List<TransacaoEntity> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);
    @Query("""
        SELECT t FROM TransacaoEntity t
        WHERE t.usuarioId = :usuarioId
        AND t.data BETWEEN :inicio AND :fim
        AND (:categoriaId IS NULL OR t.categoriaId = :categoriaId)
        AND (:descricao IS NULL OR LOWER(t.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
    """)
    List<TransacaoEntity> buscarComFiltros(
            Long usuarioId,
            LocalDate inicio,
            LocalDate fim,
            Long categoriaId,
            String descricao
    );
}


@Component
@RequiredArgsConstructor
public class TransacaoRepositoryImpl implements TransacaoRepository {

    private final TransacaoJpaRepository jpaRepository;

    @Override
    public Transacao salvar(Transacao transacao) {
        TransacaoEntity entity = toEntity(transacao);
        entity = jpaRepository.save(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<Transacao> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Transacao> buscarPorPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim) {
        return jpaRepository.findByUsuarioIdAndDataBetween(usuarioId, inicio, fim)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Transacao atualizar(Transacao transacao) {
        TransacaoEntity entity = toEntity(transacao);
        entity = jpaRepository.save(entity);
        return toDomain(entity);
    }

    @Override
    public void remover(Long id) {
        jpaRepository.deleteById(id);
    }

    private TransacaoEntity toEntity(Transacao domain) {
        TransacaoEntity entity = new TransacaoEntity();
        entity.setId(domain.getId());
        entity.setTipo(domain.getTipo());
        entity.setValor(domain.getValor());
        entity.setData(domain.getData());
        entity.setDescricao(domain.getDescricao());
        entity.setCategoriaId(domain.getCategoriaId());
        entity.setUsuarioId(domain.getUsuarioId());
        return entity;
    }

    private Transacao toDomain(TransacaoEntity entity) {
        Transacao domain = new Transacao();
        domain.setId(entity.getId());
        domain.setTipo(entity.getTipo());
        domain.setValor(entity.getValor());
        domain.setData(entity.getData());
        domain.setDescricao(entity.getDescricao());
        domain.setCategoriaId(entity.getCategoriaId());
        domain.setUsuarioId(entity.getUsuarioId());
        return domain;
    }

    public List<Transacao> buscarComFiltros(
            Long usuarioId,
            LocalDate inicio,
            LocalDate fim,
            Long categoriaId,
            String descricao
    ) {
        return jpaRepository.buscarComFiltros(usuarioId, inicio, fim, categoriaId, descricao)
                .stream()
                .map(this::toDomain)
                .toList();
    }
}
