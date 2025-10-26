package br.com.coregate.infrastructure.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
public class TransactionServerServiceGrpc
        extends TransactionProtoServiceGrpc.TransactionProtoServiceImplBase {

    // em br.com.coregate.infrastructure.grpc.TransactionClientServiceGrpc
    // na linha 16 ele dispara o process, poderia disparar os metodos abaixo
    // authorize ou setle
    @Override
    public void process(TransactionCommandProtoRequest request,
                        StreamObserver<TransactionCommandProtoResponse> responseObserver) {

        TransactionCommandProto tx = request.getTransaction();

        log.info("📥 [GRPC SERVER] Received transaction: " + tx);

        // Simulação de processamento (poderia chamar um UseCase da aplicação aqui)
        TransactionCommandProtoResponse response = TransactionCommandProtoResponse.newBuilder()
                .setTransactionId(tx.getTenantId()+UUID.randomUUID().toString())
                .setAuthorizationCode("AUTH-" + UUID.randomUUID())
                .setResponseCode("00")
                .setStatus(TransactionStatus.AUTHORIZED)
                .build();

        log.info("📥 [GRPC SERVER] Response: " + response);

        // Retorna a resposta ao cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }




    @Override
    public void authorize(TransactionCommandProtoRequest request,
                          StreamObserver<TransactionCommandProtoResponse> responseObserver) {

        TransactionCommandProto tx = request.getTransaction();
        System.out.println("📥 [SERVER] Authorize request: " + tx);

        // Simulação de processamento (poderia chamar um UseCase da aplicação aqui)
        TransactionCommandProtoResponse response = TransactionCommandProtoResponse.newBuilder()
                .setTransactionId(tx.getTenantId()+UUID.randomUUID().toString())
                .setAuthorizationCode("AUTH-" + UUID.randomUUID())
                .setResponseCode("00")
                .setStatus(TransactionStatus.REJECTED)
                .build();

        log.info("📥 [GRPC SERVER] Response: " + response);

        // Retorna a resposta ao cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void settle(TransactionCommandProtoRequest request,
                       StreamObserver<TransactionCommandProtoResponse> responseObserver) {

        TransactionCommandProto tx = request.getTransaction();
        System.out.println("📥 [SERVER] Settle request: " + tx);

        // Simulação de processamento (poderia chamar um UseCase da aplicação aqui)
        TransactionCommandProtoResponse response = TransactionCommandProtoResponse.newBuilder()
                .setTransactionId(tx.getTenantId()+UUID.randomUUID().toString())
                .setAuthorizationCode("AUTH-" + UUID.randomUUID())
                .setResponseCode("00")
                .setStatus(TransactionStatus.CANCELED)
                .build();

        log.info("📥 [GRPC SERVER] Response: " + response);

        // Retorna a resposta ao cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
