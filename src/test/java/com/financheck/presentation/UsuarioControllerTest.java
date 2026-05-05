package com.financheck.presentation;

import com.financheck.domain.entity.Usuario;
import com.financheck.presentation.dto.usuario.UsuarioAtualizacaoDTO;
import com.financheck.presentation.dto.usuario.UsuarioCadastroDTO;
import com.financheck.presentation.dto.usuario.UsuarioLoginDTO;
import com.financheck.presentation.dto.usuario.UsuarioResponseDTO;
import com.financheck.usecase.usuario.AtualizarUsuarioUseCase;
import com.financheck.usecase.usuario.CadastrarUsuarioUseCase;
import com.financheck.usecase.usuario.DesativarUsuarioUseCase;
import com.financheck.usecase.usuario.LoginUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UsuarioController - Testes Unitários")
class UsuarioControllerTest {

    @Mock
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @Mock
    private LoginUsuarioUseCase loginUsuarioUseCase;

    @Mock
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    @Mock
    private DesativarUsuarioUseCase desativarUsuarioUseCase;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNome("João Silva");
        usuarioMock.setEmail("joao@example.com");
        usuarioMock.setSenha("$2a$10$encryptedPassword");
        usuarioMock.setAtivo(true);
    }

    @Test
    @DisplayName("Deve cadastrar usuário e retornar 201")
    void testCadastrarUsuarioComSucesso() {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO();
        dto.setNome("João Silva");
        dto.setEmail("joao@example.com");
        dto.setSenha("SenhaForte123!");

        when(cadastrarUsuarioUseCase.execute(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        ResponseEntity<UsuarioResponseDTO> response = usuarioController.cadastrar(dto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
        assertEquals("joao@example.com", response.getBody().getEmail());
        assertTrue(response.getBody().isAtivo());
        verify(cadastrarUsuarioUseCase, times(1)).execute(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve retornar ResponseDTO sem campo senha")
    void testResponseNaoExpoeSenha() {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO();
        dto.setNome("João Silva");
        dto.setEmail("joao@example.com");
        dto.setSenha("SenhaForte123!");

        when(cadastrarUsuarioUseCase.execute(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        ResponseEntity<UsuarioResponseDTO> response = usuarioController.cadastrar(dto);

        // Assert
        // Verificar que ResponseDTO não contém campo senha através dos getter/setter
        UsuarioResponseDTO responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals("João Silva", responseDto.getNome());
        assertEquals("joao@example.com", responseDto.getEmail());

        // Verificar que não há acesso a campo senha na DTO
        // A DTO ResponseDTO não possui campo senha, apenas: id, nome, email, ativo
        try {
            responseDto.getClass().getDeclaredMethod("getSenha");
            fail("ResponseDTO não deve ter método getSenha()");
        } catch (NoSuchMethodException e) {
            // Esperado - não deve ter o método
        }
    }

    @Test
    @DisplayName("Deve fazer login e retornar 200")
    void testLoginComSucesso() {
        // Arrange
        UsuarioLoginDTO dto = new UsuarioLoginDTO();
        dto.setEmail("joao@example.com");
        dto.setSenha("SenhaForte123!");

        when(loginUsuarioUseCase.execute(dto.getEmail(), dto.getSenha())).thenReturn(usuarioMock);

        // Act
        ResponseEntity<UsuarioResponseDTO> response = usuarioController.login(dto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getNome());
        assertEquals("joao@example.com", response.getBody().getEmail());
        verify(loginUsuarioUseCase, times(1)).execute("joao@example.com", "SenhaForte123!");
    }

    @Test
    @DisplayName("Deve atualizar usuário e retornar 200")
    void testAtualizarUsuarioComSucesso() {
        // Arrange
        Long usuarioId = 1L;
        UsuarioAtualizacaoDTO dto = new UsuarioAtualizacaoDTO();
        dto.setNome("João Silva Novo");
        dto.setEmail("joao.novo@example.com");

        usuarioMock.setNome("João Silva Novo");
        usuarioMock.setEmail("joao.novo@example.com");

        when(atualizarUsuarioUseCase.execute(usuarioId, dto.getNome(), dto.getEmail())).thenReturn(usuarioMock);

        // Act
        ResponseEntity<UsuarioResponseDTO> response = usuarioController.atualizar(usuarioId, dto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva Novo", response.getBody().getNome());
        assertEquals("joao.novo@example.com", response.getBody().getEmail());
        verify(atualizarUsuarioUseCase, times(1)).execute(usuarioId, dto.getNome(), dto.getEmail());
    }

    @Test
    @DisplayName("Deve desativar usuário e retornar 204")
    void testDesativarUsuarioComSucesso() {
        // Arrange
        Long usuarioId = 1L;

        // Act
        ResponseEntity<Void> response = usuarioController.desativar(usuarioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(desativarUsuarioUseCase, times(1)).execute(usuarioId);
    }

    @Test
    @DisplayName("Deve criar usuário correto com DTO")
    void testCriarUsuarioAPartirDTO() {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO();
        dto.setNome("Maria Santos");
        dto.setEmail("maria@example.com");
        dto.setSenha("OutraSenha456!");

        Usuario usuarioEsperado = new Usuario();
        usuarioEsperado.setNome("Maria Santos");
        usuarioEsperado.setEmail("maria@example.com");
        usuarioEsperado.setSenha("OutraSenha456!");

        when(cadastrarUsuarioUseCase.execute(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        usuarioController.cadastrar(dto);

        // Assert
        verify(cadastrarUsuarioUseCase).execute(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve fazer login com email e senha do DTO")
    void testLoginComCredenciaisDTO() {
        // Arrange
        UsuarioLoginDTO dto = new UsuarioLoginDTO();
        dto.setEmail("joao@example.com");
        dto.setSenha("SenhaCorreta");

        when(loginUsuarioUseCase.execute("joao@example.com", "SenhaCorreta")).thenReturn(usuarioMock);

        // Act
        usuarioController.login(dto);

        // Assert
        verify(loginUsuarioUseCase).execute("joao@example.com", "SenhaCorreta");
    }

    @Test
    @DisplayName("Deve chamar use case de cadastro")
    void testChamarUseCaseCadastro() {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO();
        dto.setNome("João");
        dto.setEmail("joao@example.com");
        dto.setSenha("Senha123!");

        when(cadastrarUsuarioUseCase.execute(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        usuarioController.cadastrar(dto);

        // Assert
        verify(cadastrarUsuarioUseCase, times(1)).execute(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve chamar use case de login")
    void testChamarUseCaseLogin() {
        // Arrange
        UsuarioLoginDTO dto = new UsuarioLoginDTO();
        dto.setEmail("joao@example.com");
        dto.setSenha("Senha123!");

        when(loginUsuarioUseCase.execute(anyString(), anyString())).thenReturn(usuarioMock);

        // Act
        usuarioController.login(dto);

        // Assert
        verify(loginUsuarioUseCase, times(1)).execute(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve chamar use case de atualização")
    void testChamarUseCaseAtualizacao() {
        // Arrange
        Long userId = 1L;
        UsuarioAtualizacaoDTO dto = new UsuarioAtualizacaoDTO();
        dto.setNome("João Novo");
        dto.setEmail("joao.novo@example.com");

        when(atualizarUsuarioUseCase.execute(anyLong(), anyString(), anyString())).thenReturn(usuarioMock);

        // Act
        usuarioController.atualizar(userId, dto);

        // Assert
        verify(atualizarUsuarioUseCase, times(1)).execute(userId, dto.getNome(), dto.getEmail());
    }

    @Test
    @DisplayName("Deve chamar use case de desativação")
    void testChamarUseCaseDesativacao() {
        // Arrange
        Long userId = 1L;

        // Act
        usuarioController.desativar(userId);

        // Assert
        verify(desativarUsuarioUseCase, times(1)).execute(userId);
    }

    @Test
    @DisplayName("Deve retornar UsuarioResponseDTO com dados corretos")
    void testRetornarResponseDTOCorreto() {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO();
        dto.setNome("João Silva");
        dto.setEmail("joao@example.com");
        dto.setSenha("Senha123!");

        when(cadastrarUsuarioUseCase.execute(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        ResponseEntity<UsuarioResponseDTO> response = usuarioController.cadastrar(dto);

        // Assert
        UsuarioResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("João Silva", responseDTO.getNome());
        assertEquals("joao@example.com", responseDTO.getEmail());
        assertTrue(responseDTO.isAtivo());
    }

    @Test
    @DisplayName("Deve processar múltiplos cadastros")
    void testMultiplosCadastros() {
        // Arrange
        when(cadastrarUsuarioUseCase.execute(any(Usuario.class))).thenReturn(usuarioMock);

        UsuarioCadastroDTO dto1 = new UsuarioCadastroDTO();
        dto1.setNome("João");
        dto1.setEmail("joao@example.com");
        dto1.setSenha("Senha1");

        UsuarioCadastroDTO dto2 = new UsuarioCadastroDTO();
        dto2.setNome("Maria");
        dto2.setEmail("maria@example.com");
        dto2.setSenha("Senha2");

        // Act
        ResponseEntity<UsuarioResponseDTO> response1 = usuarioController.cadastrar(dto1);
        ResponseEntity<UsuarioResponseDTO> response2 = usuarioController.cadastrar(dto2);

        // Assert
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        verify(cadastrarUsuarioUseCase, times(2)).execute(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve processar múltiplos logins")
    void testMultiplosLogins() {
        // Arrange
        when(loginUsuarioUseCase.execute(anyString(), anyString())).thenReturn(usuarioMock);

        UsuarioLoginDTO dto1 = new UsuarioLoginDTO();
        dto1.setEmail("joao@example.com");
        dto1.setSenha("Senha1");

        UsuarioLoginDTO dto2 = new UsuarioLoginDTO();
        dto2.setEmail("maria@example.com");
        dto2.setSenha("Senha2");

        // Act
        ResponseEntity<UsuarioResponseDTO> response1 = usuarioController.login(dto1);
        ResponseEntity<UsuarioResponseDTO> response2 = usuarioController.login(dto2);

        // Assert
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        verify(loginUsuarioUseCase, times(2)).execute(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve transmitir corretamente ID para atualização")
    void testTransmitirIDParaAtualizacao() {
        // Arrange
        Long userId = 5L;
        UsuarioAtualizacaoDTO dto = new UsuarioAtualizacaoDTO();
        dto.setNome("Novo Nome");
        dto.setEmail("novo@example.com");

        when(atualizarUsuarioUseCase.execute(anyLong(), anyString(), anyString())).thenReturn(usuarioMock);

        // Act
        usuarioController.atualizar(userId, dto);

        // Assert
        verify(atualizarUsuarioUseCase).execute(5L, "Novo Nome", "novo@example.com");
    }

    @Test
    @DisplayName("Deve transmitir corretamente ID para desativação")
    void testTransmitirIDParaDesativacao() {
        // Arrange
        Long userId = 7L;

        // Act
        usuarioController.desativar(userId);

        // Assert
        verify(desativarUsuarioUseCase).execute(7L);
    }
}



