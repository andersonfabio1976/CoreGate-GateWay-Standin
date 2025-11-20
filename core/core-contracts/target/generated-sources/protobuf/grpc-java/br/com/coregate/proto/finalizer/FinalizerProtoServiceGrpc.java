package br.com.coregate.proto.finalizer;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * =======================================================
 * 🛰️ Serviço gRPC
 * =======================================================
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: orquestrator-finalizer.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FinalizerProtoServiceGrpc {

  private FinalizerProtoServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "br.com.coregate.infrastructure.FinalizerProtoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<br.com.coregate.proto.finalizer.FinalizerRequestProto,
      br.com.coregate.proto.finalizer.FinalizerResponseProto> getAuthorizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Authorize",
      requestType = br.com.coregate.proto.finalizer.FinalizerRequestProto.class,
      responseType = br.com.coregate.proto.finalizer.FinalizerResponseProto.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<br.com.coregate.proto.finalizer.FinalizerRequestProto,
      br.com.coregate.proto.finalizer.FinalizerResponseProto> getAuthorizeMethod() {
    io.grpc.MethodDescriptor<br.com.coregate.proto.finalizer.FinalizerRequestProto, br.com.coregate.proto.finalizer.FinalizerResponseProto> getAuthorizeMethod;
    if ((getAuthorizeMethod = FinalizerProtoServiceGrpc.getAuthorizeMethod) == null) {
      synchronized (FinalizerProtoServiceGrpc.class) {
        if ((getAuthorizeMethod = FinalizerProtoServiceGrpc.getAuthorizeMethod) == null) {
          FinalizerProtoServiceGrpc.getAuthorizeMethod = getAuthorizeMethod =
              io.grpc.MethodDescriptor.<br.com.coregate.proto.finalizer.FinalizerRequestProto, br.com.coregate.proto.finalizer.FinalizerResponseProto>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Authorize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.proto.finalizer.FinalizerRequestProto.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.proto.finalizer.FinalizerResponseProto.getDefaultInstance()))
              .setSchemaDescriptor(new FinalizerProtoServiceMethodDescriptorSupplier("Authorize"))
              .build();
        }
      }
    }
    return getAuthorizeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FinalizerProtoServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FinalizerProtoServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FinalizerProtoServiceStub>() {
        @java.lang.Override
        public FinalizerProtoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FinalizerProtoServiceStub(channel, callOptions);
        }
      };
    return FinalizerProtoServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FinalizerProtoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FinalizerProtoServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FinalizerProtoServiceBlockingStub>() {
        @java.lang.Override
        public FinalizerProtoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FinalizerProtoServiceBlockingStub(channel, callOptions);
        }
      };
    return FinalizerProtoServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FinalizerProtoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FinalizerProtoServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FinalizerProtoServiceFutureStub>() {
        @java.lang.Override
        public FinalizerProtoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FinalizerProtoServiceFutureStub(channel, callOptions);
        }
      };
    return FinalizerProtoServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC
   * =======================================================
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void authorize(br.com.coregate.proto.finalizer.FinalizerRequestProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.proto.finalizer.FinalizerResponseProto> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAuthorizeMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FinalizerProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC
   * =======================================================
   * </pre>
   */
  public static abstract class FinalizerProtoServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FinalizerProtoServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FinalizerProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC
   * =======================================================
   * </pre>
   */
  public static final class FinalizerProtoServiceStub
      extends io.grpc.stub.AbstractAsyncStub<FinalizerProtoServiceStub> {
    private FinalizerProtoServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FinalizerProtoServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FinalizerProtoServiceStub(channel, callOptions);
    }

    /**
     */
    public void authorize(br.com.coregate.proto.finalizer.FinalizerRequestProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.proto.finalizer.FinalizerResponseProto> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FinalizerProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC
   * =======================================================
   * </pre>
   */
  public static final class FinalizerProtoServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FinalizerProtoServiceBlockingStub> {
    private FinalizerProtoServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FinalizerProtoServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FinalizerProtoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public br.com.coregate.proto.finalizer.FinalizerResponseProto authorize(br.com.coregate.proto.finalizer.FinalizerRequestProto request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAuthorizeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FinalizerProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC
   * =======================================================
   * </pre>
   */
  public static final class FinalizerProtoServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<FinalizerProtoServiceFutureStub> {
    private FinalizerProtoServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FinalizerProtoServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FinalizerProtoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<br.com.coregate.proto.finalizer.FinalizerResponseProto> authorize(
        br.com.coregate.proto.finalizer.FinalizerRequestProto request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_AUTHORIZE = 0;

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
        case METHODID_AUTHORIZE:
          serviceImpl.authorize((br.com.coregate.proto.finalizer.FinalizerRequestProto) request,
              (io.grpc.stub.StreamObserver<br.com.coregate.proto.finalizer.FinalizerResponseProto>) responseObserver);
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
          getAuthorizeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              br.com.coregate.proto.finalizer.FinalizerRequestProto,
              br.com.coregate.proto.finalizer.FinalizerResponseProto>(
                service, METHODID_AUTHORIZE)))
        .build();
  }

  private static abstract class FinalizerProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FinalizerProtoServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return br.com.coregate.proto.finalizer.FinalizerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FinalizerProtoService");
    }
  }

  private static final class FinalizerProtoServiceFileDescriptorSupplier
      extends FinalizerProtoServiceBaseDescriptorSupplier {
    FinalizerProtoServiceFileDescriptorSupplier() {}
  }

  private static final class FinalizerProtoServiceMethodDescriptorSupplier
      extends FinalizerProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FinalizerProtoServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (FinalizerProtoServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FinalizerProtoServiceFileDescriptorSupplier())
              .addMethod(getAuthorizeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
