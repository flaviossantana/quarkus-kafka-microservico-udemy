package org.br.mineradora.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.br.mineradora.dto.OpportunityDTO;
import org.br.mineradora.exception.CSVFileException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CSVHelper {

    public static ByteArrayInputStream opportunitiesCSV(List<OpportunityDTO> opportunities) {

        try {

            log.info("Started Opportunities CSV");

            CSVFormat format = CSVFormat.DEFAULT.withHeader("ID PROPSTA", "CLIENTE", "PREÇO PRO TONELADA", "MELHOR COTAÇÃO");

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try (CSVPrinter printer = new CSVPrinter(new PrintWriter(out), format)) {
                for (OpportunityDTO opportunity : opportunities) {

                    log.info("Opportunity: {}", opportunity);

                    List<String> data = Arrays.asList(
                            String.valueOf(opportunity.getProposalId()),
                            opportunity.getCostumer(),
                            String.valueOf(opportunity.getPriceTonne()),
                            String.valueOf(opportunity.getLastDollarQuotation()));

                    printer.printRecord(data);
                }
                printer.flush();
            }


            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            log.error("fail to import data to CSV file: {}", e.getMessage());
            throw new CSVFileException("fail to import data to CSV file: " + e.getMessage());
        }


    }

}
