package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.client.CurrencyPriceClient;
import org.br.mineradora.dto.CurrencyPriceDTO;
import org.br.mineradora.dto.QuotationDTO;
import org.br.mineradora.dto.USDBRL;
import org.br.mineradora.entity.QuotationEntity;
import org.br.mineradora.message.MessageEvents;
import org.br.mineradora.repository.QuotationRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@ApplicationScoped
public class QuotationService {

    @Inject
    @RestClient
    CurrencyPriceClient currencyPriceClient;

    @Inject
    QuotationRepository quotationRepository;

    @Inject
    MessageEvents messageEvents;

    @ConfigProperty(name = "cotacao.mineradora.pair.code")
    String pairCode;

    public void getCurrencyPrice() {

        CurrencyPriceDTO currencyPriceInfo = currencyPriceClient.getPriceByPair(pairCode);

        if (updateCurrencyInfoPrice(currencyPriceInfo.getUsdbrl())) {
            messageEvents.sendNewEvent(
                    QuotationDTO
                            .builder()
                            .currencyPrice(new BigDecimal(currencyPriceInfo.getUsdbrl().getBid()))
                            .date(new Date())
                            .build());
        }
    }

    private boolean updateCurrencyInfoPrice(USDBRL usdbrl) {

        BigDecimal currentPrice = new BigDecimal(usdbrl.getBid());
        AtomicBoolean updatePrice = new AtomicBoolean(false);

        List<QuotationEntity> quotations = quotationRepository.findAll().list();

        if (quotations.isEmpty()) {

            log.info("No quotations found");

            saveQuotation(usdbrl);
            updatePrice.set(true);
        } else {

            QuotationEntity lastDollarPrice = quotations
                    .stream()
                    .reduce((f, l) -> l)
                    .orElse(new QuotationEntity());

            log.info("Last dollar price: {}", lastDollarPrice);

            if (currentPrice.floatValue() > lastDollarPrice.getCurrencyPrice().floatValue()) {

                saveQuotation(usdbrl);
                updatePrice.set(true);
            }


        }

        return updatePrice.get();
    }

    private void saveQuotation(USDBRL usdbrl) {

        QuotationEntity quotationEntity = new QuotationEntity();

        quotationEntity.setCurrencyPrice(new BigDecimal(usdbrl.getBid()));
        quotationEntity.setPctChange(usdbrl.getPctChange());
        quotationEntity.setPair(pairCode);

        quotationRepository.persist(quotationEntity);
    }


}
