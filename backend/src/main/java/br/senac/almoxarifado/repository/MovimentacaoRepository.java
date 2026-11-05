package br.senac.almoxarifado.repository;

import br.senac.almoxarifado.model.Movimentacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByProdutoIdOrderByCriadoEmDescIdDesc(Long produtoId);
}
