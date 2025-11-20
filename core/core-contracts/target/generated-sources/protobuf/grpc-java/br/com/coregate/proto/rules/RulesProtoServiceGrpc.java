package br.com.coregate.proto.rules;

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
    comments = "Source: orquestrator-rules.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RulesProtoServiceGrpc {

  private RulesProtoServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "br.com.coregate.infrastructure.RulesProtoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<br.com.coregate.proto.rules.RulesRequestProto,
      br.com.coregate.proto.rules.RulesResponseProto> getEvaluateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Evaluate",
      requestType = br.com.coregate.proto.rules.RulesRequestProto.class,
      responseType = br.com.coregate.proto.rules.RulesResponseProto.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<br.com.coregate.proto.rules.RulesRequestProto,
      br.com.coregate.proto.rules.RulesResponseProto> getEvaluateMethod() {
    io.grpc.MethodDescriptor<br.com.coregate.proto.rules.RulesRequestProto, br.com.coregate.proto.rules.RulesResponseProto> getEvaluateMethod;
    if ((getEvaluateMethod = RulesProtoServiceGrpc.getEvaluateMethod) == null) {
      synchronized (RulesProtoServiceGrpc.class) {
        if ((getEvaluateMethod = RulesProtoServiceGrpc.getEvaluateMethod) == null) {
          RulesProtoServiceGrpc.getEvaluateMethod = getEvaluateMethod =
              io.grpc.MethodDescriptor.<br.com.coregate.proto.rules.RulesRequestProto, br.com.coregate.proto.rules.RulesResponseProto>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Evaluate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.proto.rules.RulesRequestProto.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  br.com.coregate.proto.rules.RulesResponseProto.getDefaultInstance()))
              .setSchemaDescriptor(new RulesProtoServiceMethodDescriptorSupplier("Evaluate"))
              .build();
        }
      }
    }
    return getEvaluateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RulesProtoServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RulesProtoServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RulesProtoServiceStub>() {
        @java.lang.Override
        public RulesProtoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RulesProtoServiceStub(channel, callOptions);
        }
      };
    return RulesProtoServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RulesProtoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RulesProtoServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RulesProtoServiceBlockingStub>() {
        @java.lang.Override
        public RulesProtoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RulesProtoServiceBlockingStub(channel, callOptions);
        }
      };
    return RulesProtoServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RulesProtoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RulesProtoServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RulesProtoServiceFutureStub>() {
        @java.lang.Override
        public RulesProtoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RulesProtoServiceFutureStub(channel, callOptions);
        }
      };
    return RulesProtoServiceFutureStub.newStub(factory, channel);
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
    default void evaluate(br.com.coregate.proto.rules.RulesRequestProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.proto.rules.RulesResponseProto> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEvaluateMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RulesProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC
   * =======================================================
   * </pre>
   */
  public static abstract class RulesProtoServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return RulesProtoServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RulesProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC
   * =======================================================
   * </pre>
   */
  public static final class RulesProtoServiceStub
      extends io.grpc.stub.AbstractAsyncStub<RulesProtoServiceStub> {
    private RulesProtoServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RulesProtoServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RulesProtoServiceStub(channel, callOptions);
    }

    /**
     */
    public void evaluate(br.com.coregate.proto.rules.RulesRequestProto request,
        io.grpc.stub.StreamObserver<br.com.coregate.proto.rules.RulesResponseProto> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEvaluateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RulesProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC
   * =======================================================
   * </pre>
   */
  public static final class RulesProtoServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RulesProtoServiceBlockingStub> {
    private RulesProtoServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RulesProtoServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RulesProtoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public br.com.coregate.proto.rules.RulesResponseProto evaluate(br.com.coregate.proto.rules.RulesRequestProto request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEvaluateMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RulesProtoService.
   * <pre>
   * =======================================================
   * 🛰️ Serviço gRPC
   * =======================================================
   * </pre>
   */
  public static final class RulesProtoServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<RulesProtoServiceFutureStub> {
    private RulesProtoServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RulesProtoServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RulesProtoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<br.com.coregate.proto.rules.RulesResponseProto> evaluate(
        br.com.coregate.proto.rules.RulesRequestProto request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEvaluateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_EVALUATE = 0;

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
        case METHODID_EVALUATE:
          serviceImpl.evaluate((br.com.coregate.proto.rules.RulesRequestProto) request,
              (io.grpc.stub.StreamObserver<br.com.coregate.proto.rules.RulesResponseProto>) responseObserver);
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
          getEvaluateMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              br.com.coregate.proto.rules.RulesRequestProto,
              br.com.coregate.proto.rules.RulesResponseProto>(
                service, METHODID_EVALUATE)))
        .build();
  }

  private static abstract class RulesProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RulesProtoServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return br.com.coregate.proto.rules.RulesProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RulesProtoService");
    }
  }

  private static final class RulesProtoServiceFileDescriptorSupplier
      extends RulesProtoServiceBaseDescriptorSupplier {
    RulesProtoServiceFileDescriptorSupplier() {}
  }

  private static final class RulesProtoServiceMethodDescriptorSupplier
      extends RulesProtoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    RulesProtoServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (RulesProtoServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RulesProtoServiceFileDescriptorSupplier())
              .addMethod(getEvaluateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
