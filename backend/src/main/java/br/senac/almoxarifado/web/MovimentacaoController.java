package br.senac.almoxarifado.web;

import br.senac.almoxarifado.service.MovimentacaoService;
import br.senac.almoxarifado.web.dto.MovimentacaoRequest;
import br.senac.almoxarifado.web.dto.MovimentacaoResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos/{produtoId}/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse registrar(@PathVariable Long produtoId, @Valid @RequestBody MovimentacaoRequest req) {
        return MovimentacaoResponse.de(service.registrar(produtoId, req.tipo(), req.quantidade(), req.motivo()));
    }

    @GetMapping
    public List<MovimentacaoResponse> historico(@PathVariable Long produtoId) {
        return service.historico(produtoId).stream().map(MovimentacaoResponse::de).toList();
    }
}
