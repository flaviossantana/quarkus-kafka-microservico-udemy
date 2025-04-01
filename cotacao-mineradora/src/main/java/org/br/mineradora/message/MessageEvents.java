package org.br.mineradora.message;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.QuotationDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class MessageEvents {

    @Channel("quotation-channel")
    Emitter<QuotationDTO> emitter;

    public void sendNewEvent(QuotationDTO quotation) {
        log.info("Sending new event to kafka topic: {}", quotation);
        emitter.send(quotation).toCompletableFuture().join();
    }

}
