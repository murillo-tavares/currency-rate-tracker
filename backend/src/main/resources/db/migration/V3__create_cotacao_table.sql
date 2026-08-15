CREATE TABLE cotacao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(10) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    valor NUMERIC(18, 8) NOT NULL,
    variacao_percentual NUMERIC(9, 6) NOT NULL,
    data_cotacao TIMESTAMP NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT now()
);

-- Cobre o padrão de acesso mais provável: série de uma moeda num intervalo de datas.
CREATE INDEX idx_cotacao_codigo_data ON cotacao (codigo, data_cotacao);
