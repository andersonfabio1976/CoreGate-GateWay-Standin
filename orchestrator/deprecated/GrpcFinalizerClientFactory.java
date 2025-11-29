//package br.com.coregate.orchestrator.grpc.client;
//
//import br.com.coregate.proto.finalizer.FinalizerProtoServiceGrpc;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class GrpcFinalizerClientFactory {
//
//    public FinalizerProtoServiceGrpc.FinalizerProtoServiceBlockingStub createFinalizertStub(int serverPort) {
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress("localhost", serverPort)
//                .usePlaintext()
//                .build();
//        return FinalizerProtoServiceGrpc.newBlockingStub(channel);
//    }
//
//}