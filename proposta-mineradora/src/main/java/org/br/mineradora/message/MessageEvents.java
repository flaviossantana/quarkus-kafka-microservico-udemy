package org.br.mineradora.message;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.ProposalDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

@Slf4j
@ApplicationScoped
public class MessageEvents {

    @Channel("proposal-channel")
    @OnOverflow(value = OnOverflow.Strategy.UNBOUNDED_BUFFER)
    Emitter<ProposalDTO> emitter;

    public void sendNewEvent(ProposalDTO proposal) {
        log.info("Sending new event to kafka topic: {}", proposal);
        emitter.send(proposal).toCompletableFuture().join();

    }

}
