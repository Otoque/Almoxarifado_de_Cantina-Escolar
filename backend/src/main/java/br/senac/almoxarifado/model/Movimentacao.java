package br.senac.almoxarifado.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes")
public class Movimentacao {

    public static final String ENTRADA = "entrada";
    public static final String SAIDA = "saida";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(nullable = false)
    private BigDecimal quantidade;

    @Column(nullable = false, length = 200)
    private String motivo;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    protected Movimentacao() {}

    public Movimentacao(Long produtoId, String tipo, BigDecimal quantidade, String motivo) {
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.motivo = motivo;
    }

    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public String getTipo() { return tipo; }
    public BigDecimal getQuantidade() { return quantidade; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
