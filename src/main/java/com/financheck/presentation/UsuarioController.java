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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DesativarUsuarioUseCase desativarUsuarioUseCase;
    private final LoginUsuarioUseCase loginUsuarioUseCase;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioCadastroDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        Usuario saved = cadastrarUsuarioUseCase.execute(usuario);
        UsuarioResponseDTO response = new UsuarioResponseDTO(saved.getId(), saved.getNome(), saved.getEmail(), saved.isAtivo());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(
            @RequestBody UsuarioLoginDTO dto) {

        Usuario usuario = loginUsuarioUseCase.execute(dto.getEmail(), dto.getSenha());

        return ResponseEntity.ok(
                new UsuarioResponseDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.isAtivo()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioAtualizacaoDTO dto) {
        Usuario updated = atualizarUsuarioUseCase.execute(id, dto.getNome(), dto.getEmail());
        UsuarioResponseDTO response = new UsuarioResponseDTO(updated.getId(), updated.getNome(), updated.getEmail(), updated.isAtivo());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        desativarUsuarioUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
