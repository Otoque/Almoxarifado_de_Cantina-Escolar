package br.senac.almoxarifado.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MovimentacaoRequest(
        @NotBlank @Pattern(regexp = "entrada|saida", message = "deve ser entrada ou saida") String tipo,
        @NotNull @DecimalMin(value = "0", inclusive = false, message = "deve ser maior que zero")
        @Digits(integer = 9, fraction = 3) BigDecimal quantidade,
        @NotBlank @Size(max = 200) String motivo) {
}
