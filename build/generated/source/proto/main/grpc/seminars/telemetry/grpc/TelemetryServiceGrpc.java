package seminars.telemetry.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: telemetry.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TelemetryServiceGrpc {

  private TelemetryServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "telemetry.TelemetryService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<seminars.telemetry.grpc.TelemetryRequest,
      seminars.telemetry.grpc.TelemetryUpdate> getStreamTelemetryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamTelemetry",
      requestType = seminars.telemetry.grpc.TelemetryRequest.class,
      responseType = seminars.telemetry.grpc.TelemetryUpdate.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<seminars.telemetry.grpc.TelemetryRequest,
      seminars.telemetry.grpc.TelemetryUpdate> getStreamTelemetryMethod() {
    io.grpc.MethodDescriptor<seminars.telemetry.grpc.TelemetryRequest, seminars.telemetry.grpc.TelemetryUpdate> getStreamTelemetryMethod;
    if ((getStreamTelemetryMethod = TelemetryServiceGrpc.getStreamTelemetryMethod) == null) {
      synchronized (TelemetryServiceGrpc.class) {
        if ((getStreamTelemetryMethod = TelemetryServiceGrpc.getStreamTelemetryMethod) == null) {
          TelemetryServiceGrpc.getStreamTelemetryMethod = getStreamTelemetryMethod =
              io.grpc.MethodDescriptor.<seminars.telemetry.grpc.TelemetryRequest, seminars.telemetry.grpc.TelemetryUpdate>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamTelemetry"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  seminars.telemetry.grpc.TelemetryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  seminars.telemetry.grpc.TelemetryUpdate.getDefaultInstance()))
              .setSchemaDescriptor(new TelemetryServiceMethodDescriptorSupplier("StreamTelemetry"))
              .build();
        }
      }
    }
    return getStreamTelemetryMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TelemetryServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TelemetryServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TelemetryServiceStub>() {
        @java.lang.Override
        public TelemetryServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TelemetryServiceStub(channel, callOptions);
        }
      };
    return TelemetryServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TelemetryServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TelemetryServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TelemetryServiceBlockingStub>() {
        @java.lang.Override
        public TelemetryServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TelemetryServiceBlockingStub(channel, callOptions);
        }
      };
    return TelemetryServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TelemetryServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TelemetryServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TelemetryServiceFutureStub>() {
        @java.lang.Override
        public TelemetryServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TelemetryServiceFutureStub(channel, callOptions);
        }
      };
    return TelemetryServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void streamTelemetry(seminars.telemetry.grpc.TelemetryRequest request,
        io.grpc.stub.StreamObserver<seminars.telemetry.grpc.TelemetryUpdate> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStreamTelemetryMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TelemetryService.
   */
  public static abstract class TelemetryServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TelemetryServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TelemetryService.
   */
  public static final class TelemetryServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TelemetryServiceStub> {
    private TelemetryServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TelemetryServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TelemetryServiceStub(channel, callOptions);
    }

    /**
     */
    public void streamTelemetry(seminars.telemetry.grpc.TelemetryRequest request,
        io.grpc.stub.StreamObserver<seminars.telemetry.grpc.TelemetryUpdate> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getStreamTelemetryMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TelemetryService.
   */
  public static final class TelemetryServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TelemetryServiceBlockingStub> {
    private TelemetryServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TelemetryServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TelemetryServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<seminars.telemetry.grpc.TelemetryUpdate> streamTelemetry(
        seminars.telemetry.grpc.TelemetryRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getStreamTelemetryMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TelemetryService.
   */
  public static final class TelemetryServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TelemetryServiceFutureStub> {
    private TelemetryServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TelemetryServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TelemetryServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_STREAM_TELEMETRY = 0;

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
        case METHODID_STREAM_TELEMETRY:
          serviceImpl.streamTelemetry((seminars.telemetry.grpc.TelemetryRequest) request,
              (io.grpc.stub.StreamObserver<seminars.telemetry.grpc.TelemetryUpdate>) responseObserver);
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
          getStreamTelemetryMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              seminars.telemetry.grpc.TelemetryRequest,
              seminars.telemetry.grpc.TelemetryUpdate>(
                service, METHODID_STREAM_TELEMETRY)))
        .build();
  }

  private static abstract class TelemetryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TelemetryServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return seminars.telemetry.grpc.Telemetry.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TelemetryService");
    }
  }

  private static final class TelemetryServiceFileDescriptorSupplier
      extends TelemetryServiceBaseDescriptorSupplier {
    TelemetryServiceFileDescriptorSupplier() {}
  }

  private static final class TelemetryServiceMethodDescriptorSupplier
      extends TelemetryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TelemetryServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TelemetryServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TelemetryServiceFileDescriptorSupplier())
              .addMethod(getStreamTelemetryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
