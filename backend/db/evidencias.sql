-- Evidência: consulta direta ao banco com as movimentações (CA1/CA2)

-- 1) Saldo atual de cada produto
SELECT id, nome, unidade, estoque_minimo, saldo FROM produtos ORDER BY nome;

-- 2) Histórico de movimentações de um produto (troque o id)
SELECT m.id, p.nome, m.tipo, m.quantidade, m.motivo, m.criado_em
FROM movimentacoes m JOIN produtos p ON p.id = m.produto_id
WHERE p.id = 1
ORDER BY m.criado_em, m.id;

-- 3) Conferência: saldo armazenado x saldo recalculado pelas movimentações (devem ser iguais)
SELECT p.nome, p.saldo AS saldo_armazenado,
       COALESCE(SUM(CASE WHEN m.tipo = 'entrada' THEN m.quantidade ELSE -m.quantidade END), 0) AS saldo_calculado
FROM produtos p LEFT JOIN movimentacoes m ON m.produto_id = p.id
GROUP BY p.id, p.nome, p.saldo
ORDER BY p.nome;

-- 4) Abaixo do mínimo
SELECT nome, saldo, estoque_minimo FROM produtos WHERE saldo < estoque_minimo;

-- 5) Última linha de defesa: o CHECK impede saldo negativo mesmo fora da aplicação
--    (deve FALHAR com "violates check constraint")
UPDATE produtos SET saldo = -1 WHERE id = 1;
