package br.com.currencyratetracker.currency_rate_tracker_api.domain.filter;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.specification.CotacaoSpecifications;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.specification.SpecificationBuilder;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.util.CodigoMoedaUtils;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.util.DataHoraUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Filtro do dashboard de cotações: moedas e intervalo de datas.
 * Se resolve para uma única {@link Specification}.
 */
public record FiltroDashboardCotacoes(
        List<String> codigosMoeda,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
) {

    private static final int INTERVALO_PADRAO_HORAS = 24;

    public FiltroDashboardCotacoes {
        // Sem início, evita consulta sem limite inferior; sem fim não há problema, pois não existe cotação além de agora.
        LocalDateTime inicioComPadrao = inicio != null ? inicio : LocalDateTime.now().minusHours(INTERVALO_PADRAO_HORAS);

        codigosMoeda = CodigoMoedaUtils.ordenarSemDuplicados(codigosMoeda);
        inicio = DataHoraUtils.arredondarParaMinuto(inicioComPadrao);
        fim = DataHoraUtils.arredondarParaMinuto(fim);
    }

    public Specification<Cotacao> toSpecification() {
        return new SpecificationBuilder<Cotacao>()
                .addIfPresent(codigosMoeda, CotacaoSpecifications::comCodigoMoedaEm)
                .addIfPresent(inicio, CotacaoSpecifications::comDataCotacaoAPartirDe)
                .addIfPresent(fim, CotacaoSpecifications::comDataCotacaoAte)
                .build();
    }
}
