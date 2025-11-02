package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.orquestrator.OrquestratorSagaContext;
import br.com.coregate.application.dto.rules.TransactionFactDto;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.infrastructure.mode.OperationalModeManager;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class FetchData {

    private static OperationalModeManager operationalModeManager;

    public static OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        log.info("🔍 Fetching merchant/card data..."+tx);
        // Aqui você faz a busca, preenche o tx e retorna
        //tx.setMerchantData("Some merchant data loaded");

        tx.getTransaction().setId(UUID.randomUUID().toString());

        if(operationalModeManager.isStandin()) {
            tx.setTransactionFactDto(domainToFact(tx.getTransaction()));
        }

        return tx;
    }

    public static OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        log.warn("↩️ Rolling back fetched data for {}", tx);
        // Aqui você desfaz o que foi carregado (limpa o contexto, por exemplo)
        //tx.setMerchantData(null);
        return tx;
    }

    private static TransactionFactDto domainToFact(Transaction tx) {
       return TransactionFactDto.builder()
                .requestId(tx.getId())
                .mcc(tx.getMcc())
                .merchantId(tx.getMerchantId())
                .pan(tx.getPan().getValue())
                .amountAutoApproves(false)
                .amountCents(tx.getMoney().getAmount().longValue())
                .country("BR")
                .gamblingAllowed(false)
                .online(false)
                .mccBlacklisted(false)
                .mccGambling(false)
                .mccWhitelisted(true)
                .riskOk(true)
                .panExceedsDailyLimit(false)
                .riskScore(42.5)
               .tenantId(tx.getTenantId())
                .build();
    }
}

