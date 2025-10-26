package br.com.coregate.adapters.repository;

import br.com.coregate.application.port.out.MerchantRepositoryPort;
import br.com.coregate.domain.model.Merchant;
import br.com.coregate.domain.vo.MerchantId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class MerchantRepositoryAdapter implements MerchantRepositoryPort {

    public Optional<Merchant> findByCode(String code) {
        log.info("🏪 Fetching merchant info for code {}", code);

        // Simulação: em um sistema real, viria do banco ou API
        if ("AMEX".equalsIgnoreCase(code)) {
//            Merchant merchant = new Merchant();
//            merchant.setCode("AMEX");
//            merchant.setName("American Express");
//            merchant.setActive(true);
//            merchant.setSettlementAccount("001-234-567");
            return Optional.of(null);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Merchant> findById(MerchantId id) {
        return Optional.empty();
    }
}
