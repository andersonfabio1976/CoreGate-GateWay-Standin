package br.com.coregate.application.port.out;

import br.com.coregate.domain.model.Tenant;
import br.com.coregate.domain.vo.TenantId;
import java.util.Optional;

public interface TenantRepositoryPort {
    Optional<Tenant> findById(TenantId id);
}