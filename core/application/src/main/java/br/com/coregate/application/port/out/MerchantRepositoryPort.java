package br.com.coregate.application.port.out;

import br.com.coregate.domain.model.Merchant;
import br.com.coregate.domain.vo.MerchantId;
import java.util.Optional;

public interface MerchantRepositoryPort {
    Optional<Merchant> findById(MerchantId id);
}