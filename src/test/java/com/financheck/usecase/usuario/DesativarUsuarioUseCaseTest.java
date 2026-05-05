package com.financheck.usecase.usuario;

import com.financheck.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@DisplayName("DesativarUsuarioUseCase - Testes Unitários")
class DesativarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DesativarUsuarioUseCase desativarUsuarioUseCase;

    private Long usuarioId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioId = 1L;
    }

    @Test
    @DisplayName("Deve desativar usuário com sucesso")
    void testDesativarUsuarioComSucesso() {
        // Act
        desativarUsuarioUseCase.execute(usuarioId);

        // Assert
        verify(usuarioRepository, times(1)).desativar(usuarioId);
    }

    @Test
    @DisplayName("Deve chamar repository para desativar")
    void testChamarRepositorioParaDesativar() {
        // Act
        desativarUsuarioUseCase.execute(1L);

        // Assert
        verify(usuarioRepository).desativar(1L);
    }

    @Test
    @DisplayName("Deve desativar múltiplos usuários")
    void testDesativarMultiplosUsuarios() {
        // Act
        desativarUsuarioUseCase.execute(1L);
        desativarUsuarioUseCase.execute(2L);
        desativarUsuarioUseCase.execute(3L);

        // Assert
        verify(usuarioRepository, times(1)).desativar(1L);
        verify(usuarioRepository, times(1)).desativar(2L);
        verify(usuarioRepository, times(1)).desativar(3L);
        verify(usuarioRepository, times(3)).desativar(anyLong());
    }

    @Test
    @DisplayName("Deve desativar com ID diferente")
    void testDesativarComIDDiferente() {
        // Act
        desativarUsuarioUseCase.execute(999L);

        // Assert
        verify(usuarioRepository).desativar(999L);
    }

    @Test
    @DisplayName("Deve permitir desativação de qualquer ID")
    void testDesativacaoComQualquerID() {
        // Test com diversos IDs
        long[] ids = {1L, 100L, 999L, 12345L};

        for (long id : ids) {
            // Act
            desativarUsuarioUseCase.execute(id);

            // Assert
            verify(usuarioRepository).desativar(id);
        }
    }

    @Test
    @DisplayName("Deve chamar repository exatamente uma vez por execução")
    void testRepositorioChamadoUmaVezPorExecucao() {
        // Act
        desativarUsuarioUseCase.execute(1L);

        // Assert
        verify(usuarioRepository, times(1)).desativar(1L);
        verify(usuarioRepository, times(1)).desativar(anyLong());
    }

    @Test
    @DisplayName("Deve executar sequencialmente sem interferência")
    void testExecuçãoSequencial() {
        // Act
        desativarUsuarioUseCase.execute(1L);
        desativarUsuarioUseCase.execute(2L);

        // Assert - Verificar order de chamadas
        InOrder inOrder = inOrder(usuarioRepository);
        inOrder.verify(usuarioRepository).desativar(1L);
        inOrder.verify(usuarioRepository).desativar(2L);
    }

    @Test
    @DisplayName("Deve usar repository correto")
    void testAgentRepositorioCorreto() {
        // Arrange
        usuarioId = 50L;

        // Act
        desativarUsuarioUseCase.execute(usuarioId);

        // Assert
        verify(usuarioRepository, only()).desativar(50L);
    }
}


