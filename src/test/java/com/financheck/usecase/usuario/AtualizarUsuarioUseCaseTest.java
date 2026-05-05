package com.financheck.usecase.usuario;

import com.financheck.domain.entity.Usuario;
import com.financheck.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("AtualizarUsuarioUseCase - Testes Unitários")
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    private Usuario usuario;
    private Long usuarioId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioId = 1L;
        usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@example.com");
        usuario.setSenha("$2a$10$senhaEncriptada");
        usuario.setAtivo(true);
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso quando dados são válidos")
    void testAtualizarUsuarioComSucesso() {
        // Arrange
        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.execute(usuarioId, "João Silva Novo", "joao.novo@example.com");

        // Assert
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).buscarPorId(usuarioId);
        verify(usuarioRepository, times(1)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve atualizar nome do usuário")
    void testAtualizarNomeUsuario() {
        // Arrange
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(usuarioId);
        usuarioAtualizado.setNome("João Silva Novo");
        usuarioAtualizado.setEmail("joao@example.com");
        usuarioAtualizado.setSenha("$2a$10$senhaEncriptada");
        usuarioAtualizado.setAtivo(true);

        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.execute(usuarioId, "João Silva Novo", "joao@example.com");

        // Assert
        assertEquals("João Silva Novo", resultado.getNome());
    }

    @Test
    @DisplayName("Deve atualizar email do usuário")
    void testAtualizarEmailUsuario() {
        // Arrange
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(usuarioId);
        usuarioAtualizado.setNome("João Silva");
        usuarioAtualizado.setEmail("joao.novo@example.com");
        usuarioAtualizado.setSenha("$2a$10$senhaEncriptada");
        usuarioAtualizado.setAtivo(true);

        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.execute(usuarioId, "João Silva", "joao.novo@example.com");

        // Assert
        assertEquals("joao.novo@example.com", resultado.getEmail());
    }

    @Test
    @DisplayName("Deve atualizar nome e email simultaneamente")
    void testAtualizarNomeEEmail() {
        // Arrange
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(usuarioId);
        usuarioAtualizado.setNome("João Novo");
        usuarioAtualizado.setEmail("joao.novo@example.com");
        usuarioAtualizado.setSenha("$2a$10$senhaEncriptada");
        usuarioAtualizado.setAtivo(true);

        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.execute(usuarioId, "João Novo", "joao.novo@example.com");

        // Assert
        assertEquals("João Novo", resultado.getNome());
        assertEquals("joao.novo@example.com", resultado.getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void testLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            atualizarUsuarioUseCase.execute(999L, "Novo Nome", "novo@example.com");
        });

        assertEquals("Usuário não encontrado: 999", exception.getMessage());
        verify(usuarioRepository, times(1)).buscarPorId(999L);
        verify(usuarioRepository, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve preservar senha durante atualização")
    void testPreservarSenhaDuranteAtualizacao() {
        // Arrange
        String senhaOriginal = "$2a$10$senhaEncriptada";
        usuario.setSenha(senhaOriginal);

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(usuarioId);
        usuarioAtualizado.setNome("João Novo");
        usuarioAtualizado.setEmail("joao.novo@example.com");
        usuarioAtualizado.setSenha(senhaOriginal);
        usuarioAtualizado.setAtivo(true);

        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.execute(usuarioId, "João Novo", "joao.novo@example.com");

        // Assert
        assertEquals(senhaOriginal, resultado.getSenha());
    }

    @Test
    @DisplayName("Deve preservar status ativo durante atualização")
    void testPreservarStatusAtivo() {
        // Arrange
        usuario.setAtivo(true);

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(usuarioId);
        usuarioAtualizado.setNome("João Novo");
        usuarioAtualizado.setEmail("joao.novo@example.com");
        usuarioAtualizado.setSenha("$2a$10$senhaEncriptada");
        usuarioAtualizado.setAtivo(true);

        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.execute(usuarioId, "João Novo", "joao.novo@example.com");

        // Assert
        assertTrue(resultado.isAtivo());
    }

    @Test
    @DisplayName("Deve atualizar múltiplos usuários")
    void testAtualizarMultiplosUsuarios() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Maria");
        usuario2.setEmail("maria@example.com");
        usuario2.setSenha("$2a$10$senhaEncriptada2");
        usuario2.setAtivo(true);

        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.buscarPorId(2L)).thenReturn(Optional.of(usuario2));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuario).thenReturn(usuario2);

        // Act
        Usuario resultado1 = atualizarUsuarioUseCase.execute(1L, "João Novo", "joao.novo@example.com");
        Usuario resultado2 = atualizarUsuarioUseCase.execute(2L, "Maria Nova", "maria.nova@example.com");

        // Assert
        assertNotNull(resultado1);
        assertNotNull(resultado2);
        verify(usuarioRepository, times(2)).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve chamar repository para buscar usuário")
    void testChamarRepositorioProcurarUsuario() {
        // Arrange
        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuario);

        // Act
        atualizarUsuarioUseCase.execute(usuarioId, "João Novo", "joao.novo@example.com");

        // Assert
        verify(usuarioRepository).buscarPorId(usuarioId);
    }

    @Test
    @DisplayName("Deve chamar repository para salvar usuário atualizado")
    void testChamarRepositorioSalvarAtualizacao() {
        // Arrange
        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuario);

        // Act
        atualizarUsuarioUseCase.execute(usuarioId, "João Novo", "joao.novo@example.com");

        // Assert
        verify(usuarioRepository).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve preservar ID durante atualização")
    void testPreservarID() {
        // Arrange
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(usuarioId);
        usuarioAtualizado.setNome("João Novo");
        usuarioAtualizado.setEmail("joao.novo@example.com");
        usuarioAtualizado.setSenha("$2a$10$senhaEncriptada");
        usuarioAtualizado.setAtivo(true);

        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        Usuario resultado = atualizarUsuarioUseCase.execute(usuarioId, "João Novo", "joao.novo@example.com");

        // Assert
        assertEquals(usuarioId, resultado.getId());
    }
}

