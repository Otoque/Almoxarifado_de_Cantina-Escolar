package br.senac.almoxarifado.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(nullable = false, length = 2)
    private String unidade;

    @Column(name = "estoque_minimo", nullable = false)
    private BigDecimal estoqueMinimo = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    protected Produto() {}

    public Produto(String nome, String unidade, BigDecimal estoqueMinimo) {
        this.nome = nome;
        this.unidade = unidade;
        this.estoqueMinimo = estoqueMinimo;
        this.saldo = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getUnidade() { return unidade; }
    public BigDecimal getEstoqueMinimo() { return estoqueMinimo; }
    public BigDecimal getSaldo() { return saldo; }
}
