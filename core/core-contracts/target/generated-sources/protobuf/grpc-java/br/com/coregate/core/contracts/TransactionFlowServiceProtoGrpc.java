package br.com.coregate.core.contracts;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: transaction-flow.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TransactionFlowServiceProtoGrpc {

  private TransactionFlowServiceProtoGrpc() {}

  public static final java.lang.String SERVICE_NAME = "br.com.coregate.core.contracts.TransactionFlowServiceProto";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<br.com.coregate.core.contracts.RequestTransactionFlowProto,
      br.com.coregate.core.contracts.ResponseTransactionFlowProto> getConnectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "connect",
      requestType = br.com.coregate.core.contracts.RequestTransactionFlowProto.class,
      responseType = br.com.coregate.core.contracts.ResponseTransactionFlowProto.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<br.com.coregate.core.contracts.RequestTransactionFlowProto,
      br.com.coregate.core.contracts.ResponseTransactionFlowProto> getConnectMethod() {
    io.grpc.MethodDescriptor<br.com.coregate.core.contracts.RequestTransactionFlowProto, br.com.coregate.core.contracts.ResponseTransactionFlowProto> getConnectMethod;
    if ((getConnectMethod = TransactionFlowServiceProtoGrpc.getConnectMethod) == null) {
      synchronized (TransactionFlowServiceProtoGrpc.class) {
        if ((getConnectMethod = TransactionFlowServiceProtoGrpc.getConnectMethod) == null) {
          TransactionFlowServiceProtoGrpc.getConnectMethod = getConnectMethod =
              io.grpc.MethodDescriptor.<br.com.coregate.core.contracts.RequestTransactionFlowProto, br.com.coregate.core.contracts.ResponseTransactionFlowProto>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "connect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.core.contracts.RequestTransactionFlowProto.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.core.contracts.ResponseTransactionFlowProto.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionFlowServiceProtoMethodDescriptorSupplier("connect"))
              .build();
        }
      }
    }
    return getConnectMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TransactionFlowServiceProtoStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionFlowServiceProtoStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionFlowServiceProtoStub>() {
        @java.lang.Override
        public TransactionFlowServiceProtoStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionFlowServiceProtoStub(channel, callOptions);
        }
      };
    return TransactionFlowServiceProtoStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TransactionFlowServiceProtoBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionFlowServiceProtoBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionFlowServiceProtoBlockingStub>() {
        @java.lang.Override
        public TransactionFlowServiceProtoBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionFlowServiceProtoBlockingStub(channel, callOptions);
        }
      };
    return TransactionFlowServiceProtoBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TransactionFlowServiceProtoFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionFlowServiceProtoFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionFlowServiceProtoFutureStub>() {
        @java.lang.Override
        public TransactionFlowServiceProtoFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionFlowServiceProtoFutureStub(channel, callOptions);
        }
      };
    return TransactionFlowServiceProtoFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void connect(br.com.coregate.core.contracts.RequestTransactionFlowProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.core.contracts.ResponseTransactionFlowProto> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConnectMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TransactionFlowServiceProto.
   */
  public static abstract class TransactionFlowServiceProtoImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TransactionFlowServiceProtoGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TransactionFlowServiceProto.
   */
  public static final class TransactionFlowServiceProtoStub
      extends io.grpc.stub.AbstractAsyncStub<TransactionFlowServiceProtoStub> {
    private TransactionFlowServiceProtoStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionFlowServiceProtoStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionFlowServiceProtoStub(channel, callOptions);
    }

    /**
     */
    public void connect(br.com.coregate.core.contracts.RequestTransactionFlowProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.core.contracts.ResponseTransactionFlowProto> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TransactionFlowServiceProto.
   */
  public static final class TransactionFlowServiceProtoBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TransactionFlowServiceProtoBlockingStub> {
    private TransactionFlowServiceProtoBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionFlowServiceProtoBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionFlowServiceProtoBlockingStub(channel, callOptions);
    }

    /**
     */
    public br.com.coregate.core.contracts.ResponseTransactionFlowProto connect(br.com.coregate.core.contracts.RequestTransactionFlowProto request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getConnectMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TransactionFlowServiceProto.
   */
  public static final class TransactionFlowServiceProtoFutureStub
      extends io.grpc.stub.AbstractFutureStub<TransactionFlowServiceProtoFutureStub> {
    private TransactionFlowServiceProtoFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionFlowServiceProtoFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionFlowServiceProtoFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<br.com.coregate.core.contracts.ResponseTransactionFlowProto> connect(
        br.com.coregate.core.contracts.RequestTransactionFlowProto request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CONNECT = 0;

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
        case METHODID_CONNECT:
          serviceImpl.connect((br.com.coregate.core.contracts.RequestTransactionFlowProto) request,
              (io.grpc.stub.StreamObserver<br.com.coregate.core.contracts.ResponseTransactionFlowProto>) responseObserver);
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
          getConnectMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              br.com.coregate.core.contracts.RequestTransactionFlowProto,
              br.com.coregate.core.contracts.ResponseTransactionFlowProto>(
                service, METHODID_CONNECT)))
        .build();
  }

  private static abstract class TransactionFlowServiceProtoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TransactionFlowServiceProtoBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return br.com.coregate.core.contracts.TransactionFlowProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TransactionFlowServiceProto");
    }
  }

  private static final class TransactionFlowServiceProtoFileDescriptorSupplier
      extends TransactionFlowServiceProtoBaseDescriptorSupplier {
    TransactionFlowServiceProtoFileDescriptorSupplier() {}
  }

  private static final class TransactionFlowServiceProtoMethodDescriptorSupplier
      extends TransactionFlowServiceProtoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TransactionFlowServiceProtoMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TransactionFlowServiceProtoGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TransactionFlowServiceProtoFileDescriptorSupplier())
              .addMethod(getConnectMethod())
              .build();
        }
      }
    }
    return result;
  }
}
