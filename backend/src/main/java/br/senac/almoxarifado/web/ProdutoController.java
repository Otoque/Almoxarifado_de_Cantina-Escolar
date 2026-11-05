package br.senac.almoxarifado.web;

import br.senac.almoxarifado.service.ProdutoService;
import br.senac.almoxarifado.web.dto.ProdutoRequest;
import br.senac.almoxarifado.web.dto.ProdutoResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@Valid @RequestBody ProdutoRequest req) {
        return ProdutoResponse.de(service.criar(req));
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return service.listar().stream().map(ProdutoResponse::de).toList();
    }

    @GetMapping("/abaixo-do-minimo")
    public List<ProdutoResponse> abaixoDoMinimo() {
        return service.abaixoDoMinimo().stream().map(ProdutoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable Long id) {
        return ProdutoResponse.de(service.buscar(id));
    }
}
