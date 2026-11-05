package br.senac.almoxarifado.web.dto;

import br.senac.almoxarifado.model.Movimentacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoResponse(Long id, Long produtoId, String tipo, BigDecimal quantidade,
                                   String motivo, LocalDateTime criadoEm) {
    public static MovimentacaoResponse de(Movimentacao m) {
        return new MovimentacaoResponse(m.getId(), m.getProdutoId(), m.getTipo(),
                m.getQuantidade(), m.getMotivo(), m.getCriadoEm());
    }
}
