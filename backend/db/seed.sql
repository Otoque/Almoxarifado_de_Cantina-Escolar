-- Rode DEPOIS de subir o backend (o Flyway cria as tabelas).
-- docker exec -i almoxarifado-db psql -U postgres -d almoxarifado < db/seed.sql

INSERT INTO produtos (nome, unidade, estoque_minimo) VALUES
  ('Arroz',        'kg', 10),
  ('Feijão',       'kg', 8),
  ('Óleo de soja', 'L',  5),
  ('Ovos',         'un', 30),
  ('Leite',        'L',  12)
ON CONFLICT (nome) DO NOTHING;

-- Entradas de exemplo, mantendo saldo e histórico coerentes
INSERT INTO movimentacoes (produto_id, tipo, quantidade, motivo)
SELECT id, 'entrada', 20, 'Compra inicial' FROM produtos WHERE nome = 'Arroz';
UPDATE produtos SET saldo = saldo + 20 WHERE nome = 'Arroz';

INSERT INTO movimentacoes (produto_id, tipo, quantidade, motivo)
SELECT id, 'entrada', 3, 'Compra inicial' FROM produtos WHERE nome = 'Feijão';
UPDATE produtos SET saldo = saldo + 3 WHERE nome = 'Feijão';  -- fica abaixo do mínimo (8)
