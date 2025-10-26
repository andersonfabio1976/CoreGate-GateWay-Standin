package br.com.coregate.infrastructure.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ==========================
 * gRPC Service
 * ==========================
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: transaction_command_proto.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TransactionProtoServiceGrpc {

  private TransactionProtoServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "TransactionProtoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest,
      br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> getProcessMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Process",
      requestType = br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest.class,
      responseType = br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest,
      br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> getProcessMethod() {
    io.grpc.MethodDescriptor<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest, br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> getProcessMethod;
    if ((getProcessMethod = TransactionProtoServiceGrpc.getProcessMethod) == null) {
      synchronized (TransactionProtoServiceGrpc.class) {
        if ((getProcessMethod = TransactionProtoServiceGrpc.getProcessMethod) == null) {
          TransactionProtoServiceGrpc.getProcessMethod = getProcessMethod =
              io.grpc.MethodDescriptor.<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest, br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Process"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionProtoServiceMethodDescriptorSupplier("Process"))
              .build();
        }
      }
    }
    return getProcessMethod;
  }

  private static volatile io.grpc.MethodDescriptor<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest,
      br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> getAuthorizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Authorize",
      requestType = br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest.class,
      responseType = br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest,
      br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> getAuthorizeMethod() {
    io.grpc.MethodDescriptor<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest, br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> getAuthorizeMethod;
    if ((getAuthorizeMethod = TransactionProtoServiceGrpc.getAuthorizeMethod) == null) {
      synchronized (TransactionProtoServiceGrpc.class) {
        if ((getAuthorizeMethod = TransactionProtoServiceGrpc.getAuthorizeMethod) == null) {
          TransactionProtoServiceGrpc.getAuthorizeMethod = getAuthorizeMethod =
              io.grpc.MethodDescriptor.<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest, br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Authorize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionProtoServiceMethodDescriptorSupplier("Authorize"))
              .build();
        }
      }
    }
    return getAuthorizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest,
      br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> getSettleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Settle",
      requestType = br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest.class,
      responseType = br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest,
      br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> getSettleMethod() {
    io.grpc.MethodDescriptor<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest, br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> getSettleMethod;
    if ((getSettleMethod = TransactionProtoServiceGrpc.getSettleMethod) == null) {
      synchronized (TransactionProtoServiceGrpc.class) {
        if ((getSettleMethod = TransactionProtoServiceGrpc.getSettleMethod) == null) {
          TransactionProtoServiceGrpc.getSettleMethod = getSettleMethod =
              io.grpc.MethodDescriptor.<br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest, br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Settle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionProtoServiceMethodDescriptorSupplier("Settle"))
              .build();
        }
      }
    }
    return getSettleMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TransactionProtoServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionProtoServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionProtoServiceStub>() {
        @java.lang.Override
        public TransactionProtoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionProtoServiceStub(channel, callOptions);
        }
      };
    return TransactionProtoServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TransactionProtoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionProtoServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionProtoServiceBlockingStub>() {
        @java.lang.Override
        public TransactionProtoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionProtoServiceBlockingStub(channel, callOptions);
        }
      };
    return TransactionProtoServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TransactionProtoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionProtoServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionProtoServiceFutureStub>() {
        @java.lang.Override
        public TransactionProtoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionProtoServiceFutureStub(channel, callOptions);
        }
      };
    return TransactionProtoServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * ==========================
   * gRPC Service
   * ==========================
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void process(br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request,
        io.grpc.stub.StreamObserver<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getProcessMethod(), responseObserver);
    }

    /**
     */
    default void authorize(br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request,
        io.grpc.stub.StreamObserver<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAuthorizeMethod(), responseObserver);
    }

    /**
     */
    default void settle(br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request,
        io.grpc.stub.StreamObserver<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSettleMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TransactionProtoService.
   * <pre>
   * ==========================
   * gRPC Service
   * ==========================
   * </pre>
   */
  public static abstract class TransactionProtoServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TransactionProtoServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TransactionProtoService.
   * <pre>
   * ==========================
   * gRPC Service
   * ==========================
   * </pre>
   */
  public static final class TransactionProtoServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TransactionProtoServiceStub> {
    private TransactionProtoServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionProtoServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionProtoServiceStub(channel, callOptions);
    }

    /**
     */
    public void process(br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request,
        io.grpc.stub.StreamObserver<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getProcessMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void authorize(br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request,
        io.grpc.stub.StreamObserver<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void settle(br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request,
        io.grpc.stub.StreamObserver<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSettleMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TransactionProtoService.
   * <pre>
   * ==========================
   * gRPC Service
   * ==========================
   * </pre>
   */
  public static final class TransactionProtoServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TransactionProtoServiceBlockingStub> {
    private TransactionProtoServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionProtoServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionProtoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse process(br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getProcessMethod(), getCallOptions(), request);
    }

    /**
     */
    public br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse authorize(br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAuthorizeMethod(), getCallOptions(), request);
    }

    /**
     */
    public br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse settle(br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSettleMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TransactionProtoService.
   * <pre>
   * ==========================
   * gRPC Service
   * ==========================
   * </pre>
   */
  public static final class TransactionProtoServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TransactionProtoServiceFutureStub> {
    private TransactionProtoServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionProtoServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionProtoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> process(
        br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getProcessMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> authorize(
        br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse> settle(
        br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSettleMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PROCESS = 0;
  private static final int METHODID_AUTHORIZE = 1;
  private static final int METHODID_SETTLE = 2;

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
        case METHODID_PROCESS:
          serviceImpl.process((br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest) request,
              (io.grpc.stub.StreamObserver<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse>) responseObserver);
          break;
        case METHODID_AUTHORIZE:
          serviceImpl.authorize((br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest) request,
              (io.grpc.stub.StreamObserver<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse>) responseObserver);
          break;
        case METHODID_SETTLE:
          serviceImpl.settle((br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest) request,
              (io.grpc.stub.StreamObserver<br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse>) responseObserver);
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
          getProcessMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest,
              br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse>(
                service, METHODID_PROCESS)))
        .addMethod(
          getAuthorizeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest,
              br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse>(
                service, METHODID_AUTHORIZE)))
        .addMethod(
          getSettleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest,
              br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse>(
                service, METHODID_SETTLE)))
        .build();
  }

  private static abstract class TransactionProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TransactionProtoServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return br.com.coregate.infrastructure.grpc.TransactionCommandProtoFiles.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TransactionProtoService");
    }
  }

  private static final class TransactionProtoServiceFileDescriptorSupplier
      extends TransactionProtoServiceBaseDescriptorSupplier {
    TransactionProtoServiceFileDescriptorSupplier() {}
  }

  private static final class TransactionProtoServiceMethodDescriptorSupplier
      extends TransactionProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TransactionProtoServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TransactionProtoServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TransactionProtoServiceFileDescriptorSupplier())
              .addMethod(getProcessMethod())
              .addMethod(getAuthorizeMethod())
              .addMethod(getSettleMethod())
              .build();
        }
      }
    }
    return result;
  }
}
