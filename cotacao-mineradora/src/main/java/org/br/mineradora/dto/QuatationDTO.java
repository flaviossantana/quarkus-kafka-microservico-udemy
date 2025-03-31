package org.br.mineradora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class QuatationDTO {

    private Date date;
    private BigDecimal currencyPrice;

}
