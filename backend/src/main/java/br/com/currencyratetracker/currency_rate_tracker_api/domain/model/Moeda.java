package br.com.currencyratetracker.currency_rate_tracker_api.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Moeda estrangeira cotada em relação ao Real (BRL), como USD ou EUR.
 */
@Entity
@Table(name = "moeda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moeda {

    /** UUID gerado em memória (não sequencial, não adivinhável). */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Código ISO da moeda usado para consultar a AwesomeAPI, ex.: "USD". */
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
}
