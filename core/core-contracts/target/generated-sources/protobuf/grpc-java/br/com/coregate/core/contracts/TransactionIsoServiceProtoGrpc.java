package br.com.coregate.core.contracts;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: transaction-iso.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TransactionIsoServiceProtoGrpc {

  private TransactionIsoServiceProtoGrpc() {}

  public static final java.lang.String SERVICE_NAME = "TransactionIsoServiceProto";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<br.com.coregate.core.contracts.RequestTransactionIsoProto,
      br.com.coregate.core.contracts.ResponseTransactionIsoProto> getConnectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "connect",
      requestType = br.com.coregate.core.contracts.RequestTransactionIsoProto.class,
      responseType = br.com.coregate.core.contracts.ResponseTransactionIsoProto.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<br.com.coregate.core.contracts.RequestTransactionIsoProto,
      br.com.coregate.core.contracts.ResponseTransactionIsoProto> getConnectMethod() {
    io.grpc.MethodDescriptor<br.com.coregate.core.contracts.RequestTransactionIsoProto, br.com.coregate.core.contracts.ResponseTransactionIsoProto> getConnectMethod;
    if ((getConnectMethod = TransactionIsoServiceProtoGrpc.getConnectMethod) == null) {
      synchronized (TransactionIsoServiceProtoGrpc.class) {
        if ((getConnectMethod = TransactionIsoServiceProtoGrpc.getConnectMethod) == null) {
          TransactionIsoServiceProtoGrpc.getConnectMethod = getConnectMethod =
              io.grpc.MethodDescriptor.<br.com.coregate.core.contracts.RequestTransactionIsoProto, br.com.coregate.core.contracts.ResponseTransactionIsoProto>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "connect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.core.contracts.RequestTransactionIsoProto.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.core.contracts.ResponseTransactionIsoProto.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionIsoServiceProtoMethodDescriptorSupplier("connect"))
              .build();
        }
      }
    }
    return getConnectMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TransactionIsoServiceProtoStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionIsoServiceProtoStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionIsoServiceProtoStub>() {
        @java.lang.Override
        public TransactionIsoServiceProtoStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionIsoServiceProtoStub(channel, callOptions);
        }
      };
    return TransactionIsoServiceProtoStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TransactionIsoServiceProtoBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionIsoServiceProtoBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionIsoServiceProtoBlockingStub>() {
        @java.lang.Override
        public TransactionIsoServiceProtoBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionIsoServiceProtoBlockingStub(channel, callOptions);
        }
      };
    return TransactionIsoServiceProtoBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TransactionIsoServiceProtoFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionIsoServiceProtoFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionIsoServiceProtoFutureStub>() {
        @java.lang.Override
        public TransactionIsoServiceProtoFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionIsoServiceProtoFutureStub(channel, callOptions);
        }
      };
    return TransactionIsoServiceProtoFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void connect(br.com.coregate.core.contracts.RequestTransactionIsoProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.core.contracts.ResponseTransactionIsoProto> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConnectMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TransactionIsoServiceProto.
   */
  public static abstract class TransactionIsoServiceProtoImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TransactionIsoServiceProtoGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TransactionIsoServiceProto.
   */
  public static final class TransactionIsoServiceProtoStub
      extends io.grpc.stub.AbstractAsyncStub<TransactionIsoServiceProtoStub> {
    private TransactionIsoServiceProtoStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionIsoServiceProtoStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionIsoServiceProtoStub(channel, callOptions);
    }

    /**
     */
    public void connect(br.com.coregate.core.contracts.RequestTransactionIsoProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.core.contracts.ResponseTransactionIsoProto> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TransactionIsoServiceProto.
   */
  public static final class TransactionIsoServiceProtoBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TransactionIsoServiceProtoBlockingStub> {
    private TransactionIsoServiceProtoBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionIsoServiceProtoBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionIsoServiceProtoBlockingStub(channel, callOptions);
    }

    /**
     */
    public br.com.coregate.core.contracts.ResponseTransactionIsoProto connect(br.com.coregate.core.contracts.RequestTransactionIsoProto request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getConnectMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TransactionIsoServiceProto.
   */
  public static final class TransactionIsoServiceProtoFutureStub
      extends io.grpc.stub.AbstractFutureStub<TransactionIsoServiceProtoFutureStub> {
    private TransactionIsoServiceProtoFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionIsoServiceProtoFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionIsoServiceProtoFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<br.com.coregate.core.contracts.ResponseTransactionIsoProto> connect(
        br.com.coregate.core.contracts.RequestTransactionIsoProto request) {
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
          serviceImpl.connect((br.com.coregate.core.contracts.RequestTransactionIsoProto) request,
              (io.grpc.stub.StreamObserver<br.com.coregate.core.contracts.ResponseTransactionIsoProto>) responseObserver);
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
              br.com.coregate.core.contracts.RequestTransactionIsoProto,
              br.com.coregate.core.contracts.ResponseTransactionIsoProto>(
                service, METHODID_CONNECT)))
        .build();
  }

  private static abstract class TransactionIsoServiceProtoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TransactionIsoServiceProtoBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return br.com.coregate.core.contracts.TransactionIsoProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TransactionIsoServiceProto");
    }
  }

  private static final class TransactionIsoServiceProtoFileDescriptorSupplier
      extends TransactionIsoServiceProtoBaseDescriptorSupplier {
    TransactionIsoServiceProtoFileDescriptorSupplier() {}
  }

  private static final class TransactionIsoServiceProtoMethodDescriptorSupplier
      extends TransactionIsoServiceProtoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TransactionIsoServiceProtoMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TransactionIsoServiceProtoGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TransactionIsoServiceProtoFileDescriptorSupplier())
              .addMethod(getConnectMethod())
              .build();
        }
      }
    }
    return result;
  }
}
