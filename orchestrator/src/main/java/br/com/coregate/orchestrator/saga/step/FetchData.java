package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.core.contracts.dto.rules.TransactionFactDto;
import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import br.com.coregate.mode.OperationalModeManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
public class FetchData {

    private final OperationalModeManager operationalModeManager;

    public FetchData(OperationalModeManager operationalModeManager) {
        this.operationalModeManager = operationalModeManager;
    }

    public OrchestratorSagaContext execute(OrchestratorSagaContext tx) {
        log.info("🔍 Fetching merchant/card data..."+tx);
        // Aqui você faz a busca, preenche o tx e retorna
        //tx.setMerchantData("Some merchant data loaded");

        if(operationalModeManager.isStandIn()) {
            tx.setTransactionFactDto(dtoToFact(tx.getTransactionCommand()));
        }
        return tx;
    }

    public OrchestratorSagaContext rollback(OrchestratorSagaContext tx) {
        log.warn("↩️ Rolling back fetched data for {}", tx);
        // Aqui você desfaz o que foi carregado (limpa o contexto, por exemplo)
        //tx.setMerchantData(null);
        return tx;
    }

    private TransactionFactDto dtoToFact(TransactionCommand tx) {
       return TransactionFactDto.builder()
                .requestId(UUID.randomUUID().toString())
                .mcc(tx.mcc())
                .merchantId(tx.merchantId())
                .pan(tx.pan().getValue())
                .amountAutoApproves(false)
                .amountCents(tx.amount().longValue())
                .country("BR")
                .gamblingAllowed(false)
                .online(false)
                .mccBlacklisted(false)
                .mccGambling(false)
                .mccWhitelisted(true)
                .riskOk(true)
                .panExceedsDailyLimit(false)
                .riskScore(42.5)
               .tenantId(tx.tenantId())
                .build();
    }
}

