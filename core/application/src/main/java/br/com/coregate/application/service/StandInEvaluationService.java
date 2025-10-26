package br.com.coregate.application.service;

import br.com.coregate.application.dto.*;
import br.com.coregate.application.exception.BusinessException;
import br.com.coregate.application.port.in.StandInEvaluationUseCase;
import br.com.coregate.application.port.out.*;
import br.com.coregate.domain.model.*;
import br.com.coregate.domain.service.StandInDomainService;
import br.com.coregate.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class StandInEvaluationService implements StandInEvaluationUseCase {

    private final TenantRepositoryPort tenantRepository;
    private final MerchantRepositoryPort merchantRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final StandInDomainService standInDomainService;

    @Override
    public AuthorizationResult evaluate(TransactionCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new BusinessException("Tenant não encontrado"));

        Merchant merchant = merchantRepository.findById(MerchantId.of(command.merchantId()))
                .orElseThrow(() -> new BusinessException("Merchant não encontrado"));

        TransactionApproval transactionApproval = null; // TODO APROVAL
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

        //boolean approved = standInDomainService.shouldApproveStandIn(tenant, txn, BigDecimal.ZERO);
        //txn.applyStandInDecision(approved, approved ? "00" : "05");
        transactionRepository.save(txn);

        log.info("🏦 StandIn aplicado: {} / Status: {}", txn.getId(), txn.getStatus());
        return new AuthorizationResult(
                txn.getId(),
                txn.getStatus(),
                txn.getAuthorizationCode(),
                txn.getResponseCode(),
                LocalDateTime.now()
        );
    }
}
