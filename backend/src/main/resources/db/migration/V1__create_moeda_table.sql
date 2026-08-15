CREATE TABLE moeda (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT now()
);
