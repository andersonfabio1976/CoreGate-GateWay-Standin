//package br.com.coregate.context.grpc.client;
//
//import br.com.coregate.proto.Orchestrator.OrchestratorProtoServiceGrpc;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class GrpcContextClientFactory {
//
//    public OrchestratorProtoServiceGrpc.OrchestratorProtoServiceBlockingStub createOrquestratorStub(int serverPort) {
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress("localhost", serverPort)
//                .usePlaintext()
//                .build();
//        return OrchestratorProtoServiceGrpc.newBlockingStub(channel);
//    }
//}
//
