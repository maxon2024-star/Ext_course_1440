package seminars.grpc;

import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import seminars.telemetry.grpc.TelemetryRequest;
import seminars.telemetry.grpc.TelemetryServiceGrpc;
import seminars.telemetry.grpc.TelemetryUpdate;
import seminars.service.SatelliteService;

@Component
public class TelemetryConsumer {

    @GrpcClient("telemetryClient")
    private TelemetryServiceGrpc.TelemetryServiceStub telemetryStub;

    private final SatelliteService satelliteService;

    public TelemetryConsumer(SatelliteService satelliteService) {
        this.satelliteService = satelliteService;
    }

    @PostConstruct
    public void startListening() {
        TelemetryRequest request = TelemetryRequest.newBuilder().setClientId("main-server").build();

        telemetryStub.streamTelemetry(request, new StreamObserver<TelemetryUpdate>() {
            @Override
            public void onNext(TelemetryUpdate update) {
                System.out.printf("Received telemetry: [%s] In: %.2f, Out: %.2f%n",
                        update.getSatelliteId(), update.getTemperatureInside(), update.getTemperatureOutside());

                // TODO: Реализовать метод updateTelemetryByExtId в SatelliteService
                // satelliteService.updateTelemetry(update.getSatelliteId(), update.getTemperatureInside(), update.getTemperatureOutside());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in telemetry stream: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Telemetry stream closed by server.");
            }
        });
    }
}