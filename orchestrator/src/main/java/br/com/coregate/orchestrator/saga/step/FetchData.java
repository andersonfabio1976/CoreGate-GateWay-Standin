package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorSagaContext;
import br.com.coregate.core.contracts.dto.rules.TransactionFactDto;
import br.com.coregate.domain.model.Transaction;
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

    public OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        log.info("🔍 Fetching merchant/card data..."+tx);
        // Aqui você faz a busca, preenche o tx e retorna
        //tx.setMerchantData("Some merchant data loaded");

        Transaction transaction = Transaction.builder()
                        .id(UUID.randomUUID().toString())
                        .build();

        tx.setTransaction(transaction);

        if(operationalModeManager.isStandIn()) {
            tx.setTransactionFactDto(domainToFact(tx.getTransaction()));
        }

        return tx;
    }

    public OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        log.warn("↩️ Rolling back fetched data for {}", tx);
        // Aqui você desfaz o que foi carregado (limpa o contexto, por exemplo)
        //tx.setMerchantData(null);
        return tx;
    }

    private TransactionFactDto domainToFact(Transaction tx) {
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

