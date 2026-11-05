package br.senac.almoxarifado.repository;

import br.senac.almoxarifado.model.Produto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    @Query("SELECT p FROM Produto p ORDER BY p.nome")
    List<Produto> listarOrdenado();

    @Query("SELECT p FROM Produto p WHERE p.saldo < p.estoqueMinimo ORDER BY p.nome")
    List<Produto> findAbaixoDoMinimo();

    /** Saída atômica: só altera se o saldo for suficiente. Retorna 0 linhas se recusada. */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Produto p SET p.saldo = p.saldo - :qtd WHERE p.id = :id AND p.saldo >= :qtd")
    int baixarSaldo(@Param("id") Long id, @Param("qtd") BigDecimal qtd);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Produto p SET p.saldo = p.saldo + :qtd WHERE p.id = :id")
    int somarSaldo(@Param("id") Long id, @Param("qtd") BigDecimal qtd);
}
