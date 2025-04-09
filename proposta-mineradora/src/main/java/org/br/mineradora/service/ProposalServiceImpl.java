package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.ProposalDetailDTO;
import org.br.mineradora.entity.ProposalEntity;
import org.br.mineradora.message.MessageEvents;
import org.br.mineradora.repository.ProposalRepository;

import java.util.Date;

@Slf4j
@ApplicationScoped
public class ProposalServiceImpl implements ProposalService {

    @Inject
    ProposalRepository proposalRepository;

    @Inject
    MessageEvents messageEvents;


    @Override
    public ProposalDetailDTO findFullProposal(Long id) {
        ProposalEntity proposal = proposalRepository.findById(id);

        return ProposalDetailDTO
                .builder()
                .tonnes(proposal.getTonnes())
                .proposalId(proposal.getId())
                .country(proposal.getCountry())
                .costumer(proposal.getCostumer())
                .priceTonne(proposal.getPriceTonne())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .build();
    }

    @Override
    @Transactional
    public void createAndSendProposal(ProposalDetailDTO proposalDetailDTO) {

        try {
            ProposalEntity proposalEntity = ProposalEntity
                    .builder()
                    .created(new Date())
                    .tonnes(proposalDetailDTO.getTonnes())
                    .id(proposalDetailDTO.getProposalId())
                    .country(proposalDetailDTO.getCountry())
                    .costumer(proposalDetailDTO.getCostumer())
                    .priceTonne(proposalDetailDTO.getPriceTonne())
                    .proposalValidityDays(proposalDetailDTO.getProposalValidityDays())
                    .build();

            proposalRepository.persist(proposalEntity);

            messageEvents.sendNewEvent(
                    ProposalDTO
                            .builder()
                            .proposalId(proposalEntity.getId())
                            .costumer(proposalEntity.getCostumer())
                            .priceTonne(proposalEntity.getPriceTonne())
                            .build());

        } catch (Exception e) {
            log.error("error when try create and send proposal: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void removeProposal(Long id) {
        proposalRepository.deleteById(id);
    }
}
