package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.br.mineradora.dto.OpportunityDTO;

import java.io.ByteArrayInputStream;
import java.util.List;

@ApplicationScoped
public class ReportServiceImpl implements ReportService {

    @Override
    public ByteArrayInputStream generateCSVROpportunities() {
        return null;
    }

    @Override
    public List<OpportunityDTO> findAllOpportunities() {
        return List.of();
    }
}
