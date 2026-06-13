package telemetry.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import seminars.telemetry.grpc.TelemetryRequest;
import seminars.telemetry.grpc.TelemetryServiceGrpc;
import seminars.telemetry.grpc.TelemetryUpdate;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;

@GrpcService
public class TelemetryGeneratorService extends TelemetryServiceGrpc.TelemetryServiceImplBase {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();

    // Динамический реестр активных спутников (потокобезопасный)
    private final Set<String> activeSatellites = ConcurrentHashMap.newKeySet();

    // Метод для добавления спутника (вызывается из Kafka Listener)
    public void addSatellite(String satelliteId) {
        activeSatellites.add(satelliteId);
    }

    // Метод для удаления спутника (вызывается из Kafka Listener)
    public void removeSatellite(String satelliteId) {
        activeSatellites.remove(satelliteId);
    }

    @Override
    public void streamTelemetry(TelemetryRequest request, StreamObserver<TelemetryUpdate> responseObserver) {
        Runnable telemetryTask = () -> {
            try {
                // Генерируем данные только для тех спутников, которые есть в реестре
                for (String satelliteId : activeSatellites) {
                    TelemetryUpdate update = TelemetryUpdate.newBuilder()
                            .setSatelliteId(satelliteId)
                            .setTemperatureInside(20.0 + (random.nextDouble() * 5))  // От 20 до 25
                            .setTemperatureOutside(-50.0 + (random.nextDouble() * 15)) // От -50 до -35
                            .build();
                    responseObserver.onNext(update);
                }
            } catch (Exception e) {
                responseObserver.onError(e);
            }
        };

        // Отправка данных каждые 2 секунды
        scheduler.scheduleAtFixedRate(telemetryTask, 0, 2, TimeUnit.SECONDS);
    }
}