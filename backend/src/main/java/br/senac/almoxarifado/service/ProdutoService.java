package br.senac.almoxarifado.service;

import br.senac.almoxarifado.exception.ConflitoException;
import br.senac.almoxarifado.exception.NaoEncontradoException;
import br.senac.almoxarifado.model.Produto;
import br.senac.almoxarifado.repository.ProdutoRepository;
import br.senac.almoxarifado.web.dto.ProdutoRequest;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository repo;

    public ProdutoService(ProdutoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Produto criar(ProdutoRequest req) {
        String nome = req.nome().trim();
        if (repo.existsByNomeIgnoreCase(nome)) {
            throw new ConflitoException("Já existe um produto com o nome \"" + nome + "\"");
        }
        BigDecimal minimo = req.estoqueMinimo() == null ? BigDecimal.ZERO : req.estoqueMinimo();
        return repo.save(new Produto(nome, req.unidade(), minimo)); // saldo inicial = 0
    }

    @Transactional(readOnly = true)
    public List<Produto> listar() {
        return repo.listarOrdenado();
    }

    @Transactional(readOnly = true)
    public Produto buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new NaoEncontradoException("Produto não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Produto> abaixoDoMinimo() {
        return repo.findAbaixoDoMinimo();
    }
}
