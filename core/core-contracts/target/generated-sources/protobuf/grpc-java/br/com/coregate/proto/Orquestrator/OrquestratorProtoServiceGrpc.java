package br.com.coregate.proto.Orquestrator;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: context-orquestrator.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class OrquestratorProtoServiceGrpc {

  private OrquestratorProtoServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "br.com.coregate.infrastructure.OrquestratorProtoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<br.com.coregate.proto.Orquestrator.OrquestratorRequestProto,
      br.com.coregate.proto.Orquestrator.OrquestratorResponseProto> getOrquestrateTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "OrquestrateTransaction",
      requestType = br.com.coregate.proto.Orquestrator.OrquestratorRequestProto.class,
      responseType = br.com.coregate.proto.Orquestrator.OrquestratorResponseProto.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<br.com.coregate.proto.Orquestrator.OrquestratorRequestProto,
      br.com.coregate.proto.Orquestrator.OrquestratorResponseProto> getOrquestrateTransactionMethod() {
    io.grpc.MethodDescriptor<br.com.coregate.proto.Orquestrator.OrquestratorRequestProto, br.com.coregate.proto.Orquestrator.OrquestratorResponseProto> getOrquestrateTransactionMethod;
    if ((getOrquestrateTransactionMethod = OrquestratorProtoServiceGrpc.getOrquestrateTransactionMethod) == null) {
      synchronized (OrquestratorProtoServiceGrpc.class) {
        if ((getOrquestrateTransactionMethod = OrquestratorProtoServiceGrpc.getOrquestrateTransactionMethod) == null) {
          OrquestratorProtoServiceGrpc.getOrquestrateTransactionMethod = getOrquestrateTransactionMethod =
              io.grpc.MethodDescriptor.<br.com.coregate.proto.Orquestrator.OrquestratorRequestProto, br.com.coregate.proto.Orquestrator.OrquestratorResponseProto>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "OrquestrateTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.proto.Orquestrator.OrquestratorRequestProto.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.proto.Orquestrator.OrquestratorResponseProto.getDefaultInstance()))
              .setSchemaDescriptor(new OrquestratorProtoServiceMethodDescriptorSupplier("OrquestrateTransaction"))
              .build();
        }
      }
    }
    return getOrquestrateTransactionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OrquestratorProtoServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrquestratorProtoServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrquestratorProtoServiceStub>() {
        @java.lang.Override
        public OrquestratorProtoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrquestratorProtoServiceStub(channel, callOptions);
        }
      };
    return OrquestratorProtoServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OrquestratorProtoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrquestratorProtoServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrquestratorProtoServiceBlockingStub>() {
        @java.lang.Override
        public OrquestratorProtoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrquestratorProtoServiceBlockingStub(channel, callOptions);
        }
      };
    return OrquestratorProtoServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OrquestratorProtoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrquestratorProtoServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrquestratorProtoServiceFutureStub>() {
        @java.lang.Override
        public OrquestratorProtoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrquestratorProtoServiceFutureStub(channel, callOptions);
        }
      };
    return OrquestratorProtoServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void orquestrateTransaction(br.com.coregate.proto.Orquestrator.OrquestratorRequestProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.proto.Orquestrator.OrquestratorResponseProto> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getOrquestrateTransactionMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service OrquestratorProtoService.
   */
  public static abstract class OrquestratorProtoServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return OrquestratorProtoServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service OrquestratorProtoService.
   */
  public static final class OrquestratorProtoServiceStub
      extends io.grpc.stub.AbstractAsyncStub<OrquestratorProtoServiceStub> {
    private OrquestratorProtoServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrquestratorProtoServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrquestratorProtoServiceStub(channel, callOptions);
    }

    /**
     */
    public void orquestrateTransaction(br.com.coregate.proto.Orquestrator.OrquestratorRequestProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.proto.Orquestrator.OrquestratorResponseProto> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getOrquestrateTransactionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service OrquestratorProtoService.
   */
  public static final class OrquestratorProtoServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<OrquestratorProtoServiceBlockingStub> {
    private OrquestratorProtoServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrquestratorProtoServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrquestratorProtoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public br.com.coregate.proto.Orquestrator.OrquestratorResponseProto orquestrateTransaction(br.com.coregate.proto.Orquestrator.OrquestratorRequestProto request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getOrquestrateTransactionMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service OrquestratorProtoService.
   */
  public static final class OrquestratorProtoServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<OrquestratorProtoServiceFutureStub> {
    private OrquestratorProtoServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrquestratorProtoServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrquestratorProtoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<br.com.coregate.proto.Orquestrator.OrquestratorResponseProto> orquestrateTransaction(
        br.com.coregate.proto.Orquestrator.OrquestratorRequestProto request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getOrquestrateTransactionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ORQUESTRATE_TRANSACTION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ORQUESTRATE_TRANSACTION:
          serviceImpl.orquestrateTransaction((br.com.coregate.proto.Orquestrator.OrquestratorRequestProto) request,
              (io.grpc.stub.StreamObserver<br.com.coregate.proto.Orquestrator.OrquestratorResponseProto>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getOrquestrateTransactionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              br.com.coregate.proto.Orquestrator.OrquestratorRequestProto,
              br.com.coregate.proto.Orquestrator.OrquestratorResponseProto>(
                service, METHODID_ORQUESTRATE_TRANSACTION)))
        .build();
  }

  private static abstract class OrquestratorProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OrquestratorProtoServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return br.com.coregate.proto.Orquestrator.OrquestratorProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OrquestratorProtoService");
    }
  }

  private static final class OrquestratorProtoServiceFileDescriptorSupplier
      extends OrquestratorProtoServiceBaseDescriptorSupplier {
    OrquestratorProtoServiceFileDescriptorSupplier() {}
  }

  private static final class OrquestratorProtoServiceMethodDescriptorSupplier
      extends OrquestratorProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    OrquestratorProtoServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (OrquestratorProtoServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OrquestratorProtoServiceFileDescriptorSupplier())
              .addMethod(getOrquestrateTransactionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
