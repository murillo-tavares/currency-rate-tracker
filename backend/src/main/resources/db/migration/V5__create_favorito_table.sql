CREATE TABLE favorito (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL,
    codigo_moeda VARCHAR(10) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (usuario_id, codigo_moeda)
);
