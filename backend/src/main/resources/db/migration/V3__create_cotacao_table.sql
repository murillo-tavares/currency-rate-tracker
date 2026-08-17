CREATE TABLE cotacao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_moeda VARCHAR(10) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    valor NUMERIC(18, 8) NOT NULL,
    variacao_percentual NUMERIC(9, 6) NOT NULL,
    data_cotacao TIMESTAMP NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT now()
);

-- Cobre a série de uma moeda num intervalo de datas, e casa com a ordenação usada por
-- CotacaoRepository.buscarUltimaCotacao[PorMoeda] (DISTINCT ON codigo_moeda), evitando
-- um sort extra pra resolver o desempate por data_criacao.
CREATE INDEX idx_cotacao_codigo_moeda_data ON cotacao (codigo_moeda, data_cotacao DESC, data_criacao DESC);
