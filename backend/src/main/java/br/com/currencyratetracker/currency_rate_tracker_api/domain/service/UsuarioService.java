package br.com.currencyratetracker.currency_rate_tracker_api.domain.service;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.auth.CredenciaisInvalidasException;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.usuario.EmailJaCadastradoException;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.usuario.Usuario;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.repository.UsuarioRepository;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.specification.UsuarioSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Regras de negócio de usuário: cadastro e autenticação por email/senha.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /** Cadastra um novo usuário, com a senha já hasheada. Rejeita email já cadastrado. */
    public Usuario cadastrar(String email, String nome, String senha) {
        if (usuarioRepository.exists(UsuarioSpecifications.comEmail(email))) {
            throw EmailJaCadastradoException.paraEmail(email);
        }
        Usuario usuario = Usuario.builder()
                .email(email)
                .nome(nome)
                .senha(passwordEncoder.encode(senha))
                .build();
        return usuarioRepository.save(usuario);
    }

    /** Autentica por email/senha; lança se as credenciais não conferirem. */
    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.findOne(UsuarioSpecifications.comEmail(email))
                .orElseThrow(CredenciaisInvalidasException::new);
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }
        return usuario;
    }
}
