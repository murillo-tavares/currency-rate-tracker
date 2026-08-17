package br.com.currencyratetracker.currency_rate_tracker_api.domain.repository;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Acesso a dados do {@link Usuario}.
 * {@link JpaSpecificationExecutor} habilita consulta por {@code Specification}
 * (ver {@code UsuarioSpecifications}).
 */
public interface UsuarioRepository extends JpaRepository<Usuario, UUID>, JpaSpecificationExecutor<Usuario> {
}
