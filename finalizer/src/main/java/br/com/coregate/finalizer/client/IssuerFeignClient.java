package br.com.coregate.finalizer.client;

import br.com.coregate.application.dto.TransactionCommandRequest;
import br.com.coregate.application.dto.TransactionCommandResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "issuerClient",
        url = "${coregate.issuer.url:http://localhost:9095}" // mock issuer
)
public interface IssuerFeignClient {

    @PostMapping("/api/v1/issuer/authorize")
    TransactionCommandResponse authorize(TransactionCommandRequest transaction);
}
