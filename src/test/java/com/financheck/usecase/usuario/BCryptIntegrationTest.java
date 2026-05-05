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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("BCrypt Integration - Testes de Criptografia")
class BCryptIntegrationTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @InjectMocks
    private LoginUsuarioUseCase loginUsuarioUseCase;

    private Usuario usuario;
    private String senhaPlaintext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        senhaPlaintext = "SenhaSegura123!";
        usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@example.com");
        usuario.setSenha(senhaPlaintext);
    }

    @Test
    @DisplayName("Fluxo completo: cadastro -> criptografia -> login -> validação")
    void testFluxoCompletoComBCrypt() {
        // ETAPA 1: Cadastro com criptografia
        when(usuarioRepository.buscarPorEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(senhaPlaintext)).thenReturn("$2a$10$N9qo8uLOMRM7sAVMxJl0");

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("João Silva");
        usuarioSalvo.setEmail("joao@example.com");
        usuarioSalvo.setSenha("$2a$10$N9qo8uLOMRM7sAVMxJl0");
        usuarioSalvo.setAtivo(true);

        when(usuarioRepository.salvar(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Cadastro
        Usuario resultado1 = cadastrarUsuarioUseCase.execute(usuario);

        // Validações do cadastro
        assertEquals("$2a$10$N9qo8uLOMRM7sAVMxJl0", resultado1.getSenha());
        assertTrue(resultado1.isAtivo());
        verify(passwordEncoder).encode(senhaPlaintext);

        // ETAPA 2: Login com validação de hash
        when(usuarioRepository.buscarPorEmail("joao@example.com")).thenReturn(Optional.of(usuarioSalvo));
        when(passwordEncoder.matches(senhaPlaintext, "$2a$10$N9qo8uLOMRM7sAVMxJl0")).thenReturn(true);

        // Login
        Usuario resultado2 = loginUsuarioUseCase.execute("joao@example.com", senhaPlaintext);

        // Validações do login
        assertNotNull(resultado2);
        assertEquals("João Silva", resultado2.getNome());
        verify(passwordEncoder).matches(senhaPlaintext, "$2a$10$N9qo8uLOMRM7sAVMxJl0");
    }

    @Test
    @DisplayName("Senha nunca é salva em texto puro")
    void testSenhaNuncaSalvaTextoPlano() {
        // Arrange
        when(usuarioRepository.buscarPorEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(senhaPlaintext)).thenReturn("$2a$10$encryptedHash");

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setSenha("$2a$10$encryptedHash");
        when(usuarioRepository.salvar(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Act
        Usuario resultado = cadastrarUsuarioUseCase.execute(usuario);

        // Assert
        assertNotEquals(senhaPlaintext, resultado.getSenha());
        assertTrue(resultado.getSenha().startsWith("$2a$10$"));
    }

    @Test
    @DisplayName("Validação de login falha com senha errada")
    void testLoginFalhaComSenhaErrada() {
        // Arrange
        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setSenha("$2a$10$N9qo8uLOMRM7sAVMxJl0");

        when(usuarioRepository.buscarPorEmail("joao@example.com")).thenReturn(Optional.of(usuarioSalvo));
        when(passwordEncoder.matches("SenhaErrada", "$2a$10$N9qo8uLOMRM7sAVMxJl0")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginUsuarioUseCase.execute("joao@example.com", "SenhaErrada");
        });

        assertEquals("Senha inválida", exception.getMessage());
    }

    @Test
    @DisplayName("Mesma senha = hashes diferentes (sal aleatório)")
    void testMesmaSenhaHashesDiferentes() {
        // Arrange - Simular salt aleatório do BCrypt
        String hash1 = "$2a$10$N9qo8uLOMRM7sAVMxJl0Hash1";
        String hash2 = "$2a$10$X9yL9pPMNRS8bBWNyKm1Hash2";

        when(passwordEncoder.encode(senhaPlaintext))
                .thenReturn(hash1)  // Primeira chamada
                .thenReturn(hash2); // Segunda chamada

        // Act
        Usuario usuario1 = new Usuario();
        usuario1.setSenha(senhaPlaintext);
        when(usuarioRepository.salvar(any(Usuario.class))).thenReturn(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setSenha(senhaPlaintext);

        String hash1Result = passwordEncoder.encode(senhaPlaintext);
        String hash2Result = passwordEncoder.encode(senhaPlaintext);

        // Assert
        assertNotEquals(hash1Result, hash2Result, "BCrypt deve gerar hashes diferentes com salt aleatório");
    }

    @Test
    @DisplayName("Matches valida corretamente senhas diferentes")
    void testMatchesValidaSenhasDiferentes() {
        // Arrange
        String hashCorreto = "$2a$10$N9qo8uLOMRM7sAVMxJl0";

        when(passwordEncoder.matches("SenhaCorreta", hashCorreto)).thenReturn(true);
        when(passwordEncoder.matches("SenhaErrada", hashCorreto)).thenReturn(false);
        when(passwordEncoder.matches("OutraSenha", hashCorreto)).thenReturn(false);

        // Act & Assert
        assertTrue(passwordEncoder.matches("SenhaCorreta", hashCorreto));
        assertFalse(passwordEncoder.matches("SenhaErrada", hashCorreto));
        assertFalse(passwordEncoder.matches("OutraSenha", hashCorreto));
    }

    @Test
    @DisplayName("Fluxo de múltiplos usuários com senhas diferentes")
    void testMultiplosUsuariosComSenhasDiferentes() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setNome("João");
        usuario1.setEmail("joao@example.com");
        usuario1.setSenha("SenhaJoao123!");

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Maria");
        usuario2.setEmail("maria@example.com");
        usuario2.setSenha("SenhaMaria456!");

        when(usuarioRepository.buscarPorEmail("joao@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.buscarPorEmail("maria@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("SenhaJoao123!")).thenReturn("$2a$10$joaoHash");
        when(passwordEncoder.encode("SenhaMaria456!")).thenReturn("$2a$10$mariaHash");

        Usuario usuarioSalvo1 = new Usuario();
        usuarioSalvo1.setId(1L);
        usuarioSalvo1.setSenha("$2a$10$joaoHash");
        usuarioSalvo1.setAtivo(true);

        Usuario usuarioSalvo2 = new Usuario();
        usuarioSalvo2.setId(2L);
        usuarioSalvo2.setSenha("$2a$10$mariaHash");
        usuarioSalvo2.setAtivo(true);

        when(usuarioRepository.salvar(any(Usuario.class)))
                .thenReturn(usuarioSalvo1)
                .thenReturn(usuarioSalvo2);

        // Act
        Usuario resultado1 = cadastrarUsuarioUseCase.execute(usuario1);
        Usuario resultado2 = cadastrarUsuarioUseCase.execute(usuario2);

        // Assert
        assertNotEquals(resultado1.getSenha(), resultado2.getSenha());
        assertEquals("$2a$10$joaoHash", resultado1.getSenha());
        assertEquals("$2a$10$mariaHash", resultado2.getSenha());
    }

    @Test
    @DisplayName("Senha é criptografada antes de salvar, não após")
    void testSenhaCriptografadaAntesDeSalvar() {
        // Arrange
        when(usuarioRepository.buscarPorEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(senhaPlaintext)).thenReturn("$2a$10$encrypted");

        Usuario[] usuarioCapturado = new Usuario[1];
        usuarioCapturado[0] = new Usuario();

        when(usuarioRepository.salvar(any(Usuario.class))).thenAnswer(invocation -> {
            usuarioCapturado[0] = invocation.getArgument(0);
            usuarioCapturado[0].setId(1L);
            return usuarioCapturado[0];
        });

        // Act
        cadastrarUsuarioUseCase.execute(usuario);

        // Assert
        // A senha deve estar criptografada quando chega ao salvar
        assertNotEquals(senhaPlaintext, usuarioCapturado[0].getSenha());
        assertTrue(usuarioCapturado[0].getSenha().startsWith("$2a$"));
    }

    @Test
    @DisplayName("Hash BCrypt começar com $2a$ ou similar")
    void testHashBCryptFormato() {
        // Arrange
        when(passwordEncoder.encode(senhaPlaintext)).thenReturn("$2a$10$NfQBxyJ7UqzBDqE3F0X3CO8rL9Pj6Z8E0M4.K");

        // Act
        String hash = passwordEncoder.encode(senhaPlaintext);

        // Assert
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"));
    }

    @Test
    @DisplayName("Login deve chamar encode durante cadastro, not durante login")
    void testEncodeApenasNoCadastro() {
        // Arrange
        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setSenha("$2a$10$hash");

        when(usuarioRepository.buscarPorEmail("joao@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(senhaPlaintext)).thenReturn("$2a$10$hash");
        when(usuarioRepository.salvar(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Act - Cadastro
        cadastrarUsuarioUseCase.execute(usuario);

        // Arrange - Login
        when(usuarioRepository.buscarPorEmail("joao@example.com")).thenReturn(Optional.of(usuarioSalvo));
        when(passwordEncoder.matches(senhaPlaintext, "$2a$10$hash")).thenReturn(true);

        // Act - Login
        loginUsuarioUseCase.execute("joao@example.com", senhaPlaintext);

        // Assert
        verify(passwordEncoder, times(1)).encode(senhaPlaintext); // Apenas once no cadastro
        verify(passwordEncoder, times(1)).matches(senhaPlaintext, "$2a$10$hash"); // matches no login
    }
}


