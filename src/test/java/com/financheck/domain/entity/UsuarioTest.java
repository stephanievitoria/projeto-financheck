package com.financheck.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Usuario Entity - Testes Unitários")
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    @DisplayName("Deve criar usuário com constructor vazio")
    void testCriarUsuarioVazio() {
        // Act
        Usuario novoUsuario = new Usuario();

        // Assert
        assertNotNull(novoUsuario);
        assertNull(novoUsuario.getId());
        assertNull(novoUsuario.getNome());
        assertNull(novoUsuario.getEmail());
        assertNull(novoUsuario.getSenha());
        assertFalse(novoUsuario.isAtivo());
    }

    @Test
    @DisplayName("Deve criar usuário com constructor com parâmetros")
    void testCriarUsuarioComParametros() {
        // Act
        Usuario novoUsuario = new Usuario(
            1L,
            "João Silva",
            "joao@example.com",
            "$2a$10$encryptedPassword",
            true
        );

        // Assert
        assertNotNull(novoUsuario);
        assertEquals(1L, novoUsuario.getId());
        assertEquals("João Silva", novoUsuario.getNome());
        assertEquals("joao@example.com", novoUsuario.getEmail());
        assertEquals("$2a$10$encryptedPassword", novoUsuario.getSenha());
        assertTrue(novoUsuario.isAtivo());
    }

    @Test
    @DisplayName("Deve setar e obter ID")
    void testSetIdGetId() {
        // Act
        usuario.setId(1L);

        // Assert
        assertEquals(1L, usuario.getId());
    }

    @Test
    @DisplayName("Deve setar e obter Nome")
    void testSetNomeGetNome() {
        // Act
        usuario.setNome("João Silva");

        // Assert
        assertEquals("João Silva", usuario.getNome());
    }

    @Test
    @DisplayName("Deve setar e obter Email")
    void testSetEmailGetEmail() {
        // Act
        usuario.setEmail("joao@example.com");

        // Assert
        assertEquals("joao@example.com", usuario.getEmail());
    }

    @Test
    @DisplayName("Deve setar e obter Senha (hash)")
    void testSetSenhaGetSenha() {
        // Act
        String hashBCrypt = "$2a$10$N9qo8uLOMRM7sAVMxJl0";
        usuario.setSenha(hashBCrypt);

        // Assert
        assertEquals(hashBCrypt, usuario.getSenha());
    }

    @Test
    @DisplayName("Deve setar e obter Status Ativo")
    void testSetAtivoGetAtivo() {
        // Act
        usuario.setAtivo(true);

        // Assert
        assertTrue(usuario.isAtivo());
    }

    @Test
    @DisplayName("Deve permitir desativar usuário")
    void testDesativarUsuario() {
        // Arrange
        usuario.setAtivo(true);

        // Act
        usuario.setAtivo(false);

        // Assert
        assertFalse(usuario.isAtivo());
    }

    @Test
    @DisplayName("Deve suportar senha vazia (validação no Use Case)")
    void testSenhaVazia() {
        // Act
        usuario.setSenha("");

        // Assert
        assertEquals("", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve suportar nome vazio (validação no Use Case)")
    void testNomeVazio() {
        // Act
        usuario.setNome("");

        // Assert
        assertEquals("", usuario.getNome());
    }

    @Test
    @DisplayName("Deve suportar email vazio (validação no Use Case)")
    void testEmailVazio() {
        // Act
        usuario.setEmail("");

        // Assert
        assertEquals("", usuario.getEmail());
    }

    @Test
    @DisplayName("Deve permitir múltiplas atualizações")
    void testMultiplasAtualizacoes() {
        // Act - Primeira atualização
        usuario.setNome("João");
        usuario.setEmail("joao@example.com");
        usuario.setSenha("senha1");

        // Assert
        assertEquals("João", usuario.getNome());
        assertEquals("joao@example.com", usuario.getEmail());
        assertEquals("senha1", usuario.getSenha());

        // Act - Segunda atualização
        usuario.setNome("Maria");
        usuario.setEmail("maria@example.com");
        usuario.setSenha("senha2");

        // Assert
        assertEquals("Maria", usuario.getNome());
        assertEquals("maria@example.com", usuario.getEmail());
        assertEquals("senha2", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve permitir null em qualquer campo")
    void testNullEmQualquerCampo() {
        // Act
        usuario.setId(null);
        usuario.setNome(null);
        usuario.setEmail(null);
        usuario.setSenha(null);
        usuario.setAtivo(false);

        // Assert
        assertNull(usuario.getId());
        assertNull(usuario.getNome());
        assertNull(usuario.getEmail());
        assertNull(usuario.getSenha());
        assertFalse(usuario.isAtivo());
    }

    @Test
    @DisplayName("Deve permitir ID grande")
    void testIDGrande() {
        // Act
        usuario.setId(9223372036854775807L);

        // Assert
        assertEquals(9223372036854775807L, usuario.getId());
    }

    @Test
    @DisplayName("Deve permitir nome longo")
    void testNomeLongo() {
        // Act
        String nomeLongo = "João Silva Santos Oliveira Costa Pereira Ribeiro Ferreira";
        usuario.setNome(nomeLongo);

        // Assert
        assertEquals(nomeLongo, usuario.getNome());
    }

    @Test
    @DisplayName("Deve permitir email longo")
    void testEmailLongo() {
        // Act
        String emailLongo = "usuario.extremamente.longo@empresa.muito.grande.com.br";
        usuario.setEmail(emailLongo);

        // Assert
        assertEquals(emailLongo, usuario.getEmail());
    }

    @Test
    @DisplayName("Deve permitir hash BCrypt longo")
    void testHashBCryptLongo() {
        // Act
        String hashLongo = "$2a$10$N9qo8uLOMRM7sAVMxJlxYeUe2aZChYx0LHxUjL0.KKVrMqZK.quae";
        usuario.setSenha(hashLongo);

        // Assert
        assertEquals(hashLongo, usuario.getSenha());
    }

    @Test
    @DisplayName("Deve manter integridade entre múltiplos getters")
    void testIntegridadeMultiplosGetters() {
        // Arrange
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setEmail("joao@example.com");
        usuario.setSenha("$2a$10$hash");
        usuario.setAtivo(true);

        // Act & Assert - Múltiplas chamadas devem retornar o mesmo valor
        assertEquals(1L, usuario.getId());
        assertEquals(1L, usuario.getId());
        assertEquals("João", usuario.getNome());
        assertEquals("João", usuario.getNome());
        assertEquals("joao@example.com", usuario.getEmail());
        assertEquals("joao@example.com", usuario.getEmail());
        assertEquals("$2a$10$hash", usuario.getSenha());
        assertEquals("$2a$10$hash", usuario.getSenha());
        assertTrue(usuario.isAtivo());
        assertTrue(usuario.isAtivo());
    }
}

