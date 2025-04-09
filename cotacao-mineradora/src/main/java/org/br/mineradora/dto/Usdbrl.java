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
public class Usdbrl {

    private String code;
    private String name;
    private String high;
    private String low;
    private String bid;
    private String ask;
    private String varBid;
    private String pctChange;
    private String timestamp;

    @JsonAlias(value = "codein")
    private String codeIn;

    @JsonAlias(value = "create_date")
    private String createDate;

}
