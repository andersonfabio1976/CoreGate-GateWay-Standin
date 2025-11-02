package br.com.coregate.proto.ingress;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * =======================================================
 * 🛰️ Serviço gRPC (Ingress ↔ Context)
 * =======================================================
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: ingress-context.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ContextProtoServiceGrpc {

  private ContextProtoServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "br.com.coregate.infrastructure.ContextProtoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<br.com.coregate.proto.ingress.ContextRequestProto,
      br.com.coregate.proto.ingress.ContextResponseProto> getAuthorizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Authorize",
      requestType = br.com.coregate.proto.ingress.ContextRequestProto.class,
      responseType = br.com.coregate.proto.ingress.ContextResponseProto.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<br.com.coregate.proto.ingress.ContextRequestProto,
      br.com.coregate.proto.ingress.ContextResponseProto> getAuthorizeMethod() {
    io.grpc.MethodDescriptor<br.com.coregate.proto.ingress.ContextRequestProto, br.com.coregate.proto.ingress.ContextResponseProto> getAuthorizeMethod;
    if ((getAuthorizeMethod = ContextProtoServiceGrpc.getAuthorizeMethod) == null) {
      synchronized (ContextProtoServiceGrpc.class) {
        if ((getAuthorizeMethod = ContextProtoServiceGrpc.getAuthorizeMethod) == null) {
          ContextProtoServiceGrpc.getAuthorizeMethod = getAuthorizeMethod =
              io.grpc.MethodDescriptor.<br.com.coregate.proto.ingress.ContextRequestProto, br.com.coregate.proto.ingress.ContextResponseProto>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Authorize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.proto.ingress.ContextRequestProto.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.proto.ingress.ContextResponseProto.getDefaultInstance()))
              .setSchemaDescriptor(new ContextProtoServiceMethodDescriptorSupplier("Authorize"))
              .build();
        }
      }
    }
    return getAuthorizeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ContextProtoServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ContextProtoServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ContextProtoServiceStub>() {
        @java.lang.Override
        public ContextProtoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ContextProtoServiceStub(channel, callOptions);
        }
      };
    return ContextProtoServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ContextProtoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ContextProtoServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ContextProtoServiceBlockingStub>() {
        @java.lang.Override
        public ContextProtoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ContextProtoServiceBlockingStub(channel, callOptions);
        }
      };
    return ContextProtoServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ContextProtoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ContextProtoServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ContextProtoServiceFutureStub>() {
        @java.lang.Override
        public ContextProtoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ContextProtoServiceFutureStub(channel, callOptions);
        }
      };
    return ContextProtoServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC (Ingress ↔ Context)
   * =======================================================
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void authorize(br.com.coregate.proto.ingress.ContextRequestProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.proto.ingress.ContextResponseProto> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAuthorizeMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ContextProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC (Ingress ↔ Context)
   * =======================================================
   * </pre>
   */
  public static abstract class ContextProtoServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ContextProtoServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ContextProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC (Ingress ↔ Context)
   * =======================================================
   * </pre>
   */
  public static final class ContextProtoServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ContextProtoServiceStub> {
    private ContextProtoServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ContextProtoServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ContextProtoServiceStub(channel, callOptions);
    }

    /**
     */
    public void authorize(br.com.coregate.proto.ingress.ContextRequestProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.proto.ingress.ContextResponseProto> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAuthorizeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ContextProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC (Ingress ↔ Context)
   * =======================================================
   * </pre>
   */
  public static final class ContextProtoServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ContextProtoServiceBlockingStub> {
    private ContextProtoServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ContextProtoServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ContextProtoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public br.com.coregate.proto.ingress.ContextResponseProto authorize(br.com.coregate.proto.ingress.ContextRequestProto request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAuthorizeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ContextProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC (Ingress ↔ Context)
   * =======================================================
   * </pre>
   */
  public static final class ContextProtoServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ContextProtoServiceFutureStub> {
    private ContextProtoServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ContextProtoServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ContextProtoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<br.com.coregate.proto.ingress.ContextResponseProto> authorize(
        br.com.coregate.proto.ingress.ContextRequestProto request) {
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
          serviceImpl.authorize((br.com.coregate.proto.ingress.ContextRequestProto) request,
              (io.grpc.stub.StreamObserver<br.com.coregate.proto.ingress.ContextResponseProto>) responseObserver);
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
              br.com.coregate.proto.ingress.ContextRequestProto,
              br.com.coregate.proto.ingress.ContextResponseProto>(
                service, METHODID_AUTHORIZE)))
        .build();
  }

  private static abstract class ContextProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ContextProtoServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return br.com.coregate.proto.ingress.ContextProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ContextProtoService");
    }
  }

  private static final class ContextProtoServiceFileDescriptorSupplier
      extends ContextProtoServiceBaseDescriptorSupplier {
    ContextProtoServiceFileDescriptorSupplier() {}
  }

  private static final class ContextProtoServiceMethodDescriptorSupplier
      extends ContextProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ContextProtoServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ContextProtoServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ContextProtoServiceFileDescriptorSupplier())
              .addMethod(getAuthorizeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
