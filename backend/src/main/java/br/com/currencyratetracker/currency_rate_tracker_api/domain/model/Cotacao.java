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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Cotação de uma moeda em relação ao Real (BRL), consultada na AwesomeAPI e persistida a
 * cada atualização do cache — a série de registros ao longo do tempo alimenta o gráfico.
 * Guarda o código e o nome da moeda direto (sem join column), evitando consulta extra pra
 * exibir a cotação — quem precisar cruzar com o catálogo faz isso explicitamente na query.
 */
@Entity
@Table(name = "cotacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cotacao {

    /** UUID gerado em memória (não sequencial, não adivinhável). */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "codigo_moeda", nullable = false, length = 10)
    private String codigoMoeda;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal valor;

    @Column(name = "variacao_percentual", nullable = false, precision = 9, scale = 6)
    private BigDecimal variacaoPercentual;

    /** Data da cotação em si, informada pela AwesomeAPI (não confundir com {@link #dataCriacao}). */
    @Column(name = "data_cotacao", nullable = false)
    private LocalDateTime dataCotacao;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
}
