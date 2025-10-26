package br.com.coregate.adapters.repository;

import br.com.coregate.application.port.out.TenantRepositoryPort;
import br.com.coregate.domain.model.Tenant;
import br.com.coregate.domain.vo.TenantId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class TenantRepositoryAdapter implements TenantRepositoryPort {

    public Optional<Tenant> findByCode(String code) {
        log.info("🏢 Fetching tenant info for code {}", code);

        // Simulação — em ambiente real viria de banco (JPA, JDBC, etc.)
        if ("UNIFOR".equalsIgnoreCase(code)) {
//            Tenant tenant = new Tenant();
//            tenant.setCode("UNIFOR");
//            tenant.setName("Universidade de Fortaleza");
//            tenant.setActive(true);
            return Optional.of(null);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Tenant> findById(TenantId id) {
        return Optional.empty();
    }
}
