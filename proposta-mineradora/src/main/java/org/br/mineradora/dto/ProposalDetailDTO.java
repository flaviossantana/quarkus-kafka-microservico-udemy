package org.br.mineradora.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class ProposalDetailDTO {
    private Long proposalId;
    private String costumer;
    private BigDecimal priceTonne;
    private Integer tonnes;
    private String country;
    private Integer proposalValidityDays;
}
