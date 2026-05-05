package com.financheck.usecase.usuario;

import com.financheck.domain.entity.Usuario;
import com.financheck.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("LoginUsuarioUseCase - Testes Unitários")
class LoginUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginUsuarioUseCase loginUsuarioUseCase;

    private Usuario usuario;
    private String email;
    private String senha;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        email = "joao@example.com";
        senha = "SenhaForte123!";
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail(email);
        usuario.setSenha("$2a$10$encryptedPassword");
        usuario.setAtivo(true);
    }

    @Test
    @DisplayName("Deve fazer login com sucesso quando credenciais são válidas")
    void testLoginComSucesso() {
        // Arrange
        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(true);

        // Act
        Usuario resultado = loginUsuarioUseCase.execute(email, senha);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals(email, resultado.getEmail());
        verify(usuarioRepository, times(1)).buscarPorEmail(email);
        verify(passwordEncoder, times(1)).matches(senha, usuario.getSenha());
    }

    @Test
    @DisplayName("Deve validar senha com BCrypt matches")
    void testValidacaoSenhaComBCryptMatches() {
        // Arrange
        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(true);

        // Act
        loginUsuarioUseCase.execute(email, senha);

        // Assert
        verify(passwordEncoder).matches(senha, usuario.getSenha());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void testLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginUsuarioUseCase.execute("inexistente@example.com", senha);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).buscarPorEmail("inexistente@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é inválida")
    void testLancarExcecaoQuandoSenhaInvalida() {
        // Arrange
        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("SenhaErrada", usuario.getSenha())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginUsuarioUseCase.execute(email, "SenhaErrada");
        });

        assertEquals("Senha inválida", exception.getMessage());
        verify(usuarioRepository, times(1)).buscarPorEmail(email);
        verify(passwordEncoder, times(1)).matches("SenhaErrada", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve retornar usuário correto após login bem-sucedido")
    void testRetornarDadosUsuarioAposSucesso() {
        // Arrange
        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(true);

        // Act
        Usuario resultado = loginUsuarioUseCase.execute(email, senha);

        // Assert
        assertEquals(1L, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals(email, resultado.getEmail());
        assertTrue(resultado.isAtivo());
    }

    @Test
    @DisplayName("Deve diferenciar senhas por maiúsculas/minúsculas")
    void testSenhasCaseInsensitive() {
        // Arrange
        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaforte123!", usuario.getSenha())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginUsuarioUseCase.execute(email, "senhaforte123!");
        });

        assertEquals("Senha inválida", exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar múltiplos logins sequenciais")
    void testMultiplosLoginsSequenciais() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setEmail("maria@example.com");
        usuario2.setSenha("$2a$10$outraSenhaEncriptada");

        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.buscarPorEmail("maria@example.com")).thenReturn(Optional.of(usuario2));
        when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(true);
        when(passwordEncoder.matches("OutraSenha", usuario2.getSenha())).thenReturn(true);

        // Act
        Usuario resultado1 = loginUsuarioUseCase.execute(email, senha);
        Usuario resultado2 = loginUsuarioUseCase.execute("maria@example.com", "OutraSenha");

        // Assert
        assertEquals(1L, resultado1.getId());
        assertEquals(2L, resultado2.getId());
        verify(usuarioRepository, times(2)).buscarPorEmail(anyString());
    }

    @Test
    @DisplayName("Deve falhar com senha vazia")
    void testFalharComSenhaVazia() {
        // Arrange
        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("", usuario.getSenha())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginUsuarioUseCase.execute(email, "");
        });

        assertEquals("Senha inválida", exception.getMessage());
    }

    @Test
    @DisplayName("Deve comparar com hash correto armazenado")
    void testComparacaoComHashCorreto() {
        // Arrange
        String hashArmazenado = "$2a$10$N9qo8uLOMRM7sAVMxJl0.3nKVDkE8M3";
        usuario.setSenha(hashArmazenado);

        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, hashArmazenado)).thenReturn(true);

        // Act
        Usuario resultado = loginUsuarioUseCase.execute(email, senha);

        // Assert
        assertNotNull(resultado);
        assertEquals(hashArmazenado, resultado.getSenha());
        verify(passwordEncoder).matches(senha, hashArmazenado);
    }

    @Test
    @DisplayName("Deve retornar usuário inativo após autenticação")
    void testRetornarUsuarioInativoSeAutenticado() {
        // Arrange
        usuario.setAtivo(false);
        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(true);

        // Act
        Usuario resultado = loginUsuarioUseCase.execute(email, senha);

        // Assert
        assertFalse(resultado.isAtivo());
    }
}

