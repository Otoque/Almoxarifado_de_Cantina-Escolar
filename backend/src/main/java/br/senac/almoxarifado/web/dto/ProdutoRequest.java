package br.senac.almoxarifado.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Pattern(regexp = "un|kg|L", message = "deve ser un, kg ou L") String unidade,
        @PositiveOrZero @Digits(integer = 9, fraction = 3) BigDecimal estoqueMinimo) {
}
