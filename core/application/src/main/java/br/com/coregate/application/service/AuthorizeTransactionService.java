package br.com.coregate.application.service;

import br.com.coregate.application.dto.*;
import br.com.coregate.application.exception.BusinessException;
import br.com.coregate.application.port.in.AuthorizeTransactionUseCase;
import br.com.coregate.application.port.in.StandInEvaluationUseCase;
import br.com.coregate.application.port.out.*;
import br.com.coregate.domain.model.*;
import br.com.coregate.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthorizeTransactionService implements AuthorizeTransactionUseCase {

    private final TenantRepositoryPort tenantRepository;
    private final MerchantRepositoryPort merchantRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final StandInEvaluationUseCase standInEvaluation;

    @Override
    public AuthorizationResult authorize(TransactionCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new BusinessException("Tenant não encontrado"));

        TransactionApproval transactionApproval = null; //TODO APPROVED
        Merchant merchant = merchantRepository.findById(MerchantId.of(command.merchantId()))
                .orElseThrow(() -> new BusinessException("Merchant não encontrado"));

        Money money = Money.of(command.amount(), command.currency());
        Transaction txn = Transaction.createNew(
                tenantId,
                merchant,
                money,
                Pan.of(command.pan().getValue()),
                command.brand(),
                command.channel(),
                command.type()
        );

        try {
            // Simulação de autorização normal
            txn.authorize(UUID.randomUUID().toString().substring(0, 6));
            transactionRepository.save(txn);
            log.info("✅ Transação autorizada {}", txn.getId());
            return new AuthorizationResult(
                    txn.getId(),
                    txn.getStatus(),
                    txn.getAuthorizationCode(),
                    txn.getResponseCode(),
                    LocalDateTime.now()
            );
        } catch (Exception ex) {
            log.warn("⚠️ Erro ao autorizar transação, avaliando StandIn...");
            return standInEvaluation.evaluate(command);
        }
    }

    private TransactionApproval transactionApproval(TransactionApproval transactionApproval) {
        return transactionApproval;
    }
}
