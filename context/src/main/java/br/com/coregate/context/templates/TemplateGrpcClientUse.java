package br.com.coregate.context.templates;

import br.com.coregate.infrastructure.grpc.TransactionClientServiceGrpc;
import br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest;
import br.com.coregate.infrastructure.mapper.TransactionMapper;
import br.com.coregate.infrastructure.mock.TransactionCommandMockBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TemplateGrpcClientUse {

    private final TransactionClientServiceGrpc grpcClient;
    private final TransactionMapper mapper;

    public TemplateGrpcClientUse(TransactionClientServiceGrpc grpcClient, TransactionMapper mapper) {
        this.grpcClient = grpcClient;
        this.mapper = mapper;
    }

    // Teste de Conexão com GRPC SERVER Orchestrator, Subir Orquestrator Antes de Rodar
    // Para que a porta esteja aberta, teste usando uma transactionMock que esta em
    // infrastructure mapeando-a para TransactionProtoRequest
    // Request tem que ter todos os campos preenchidos, criar validade pra isso
    // Se houver campo null gera exception.
    @Async // Para não travar o Spring com o Server do Grpc
    public void execute() {

        TransactionCommandProtoRequest request = TransactionCommandProtoRequest
                .newBuilder()
                .setTransaction(mapper
                        .toProto(TransactionCommandMockBuilder.build())).build();
        try {
            var response = grpcClient.callGrpc(request, 8090);
            log.info("Recebido do Orchestrator Response: {}", response);
        } catch (Exception e) {
            log.error("Erro ao Conectar No Grpc Server Orchestrator... ");
        }

    }
}

