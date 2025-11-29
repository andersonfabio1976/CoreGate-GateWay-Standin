//package br.com.coregate.orchestrator.grpc.client;
//
//import br.com.coregate.proto.rules.RulesProtoServiceGrpc;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class GrpcRulesClientFactory {
//
//    public RulesProtoServiceGrpc.RulesProtoServiceBlockingStub createRulestStub(int serverPort) {
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress("localhost", serverPort)
//                .usePlaintext()
//                .build();
//        return RulesProtoServiceGrpc.newBlockingStub(channel);
//    }
//}
