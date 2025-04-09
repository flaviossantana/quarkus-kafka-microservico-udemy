package org.br.mineradora.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class CurrencyPriceDTO {

    @JsonAlias(value = "USDBRL")
    private Usdbrl usdbrl;

}
