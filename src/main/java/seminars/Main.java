package seminars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import seminars.repository.ConstellationRepository;
import seminars.satellite.CommunicationSatellite;
import seminars.satellite.ImagingSatellite;
import seminars.service.SpaceOperationCenterService;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        try {
            ConstellationRepository constellationRepository = context.getBean(ConstellationRepository.class);
            SpaceOperationCenterService operationCenterService = context.getBean(SpaceOperationCenterService.class);

            System.out.println("\nСОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
            System.out.println("---------------------------------------------");

            CommunicationSatellite commSat1 = new CommunicationSatellite("Связь-1", 0.85, 500.0);
            System.out.println("Создан спутник: " + commSat1.getName() + " (" + commSat1.getEnergy().getBatteryLevel() + ")");

            CommunicationSatellite commSat2 = new CommunicationSatellite("Связь-2", 0.75, 1000.0);
            System.out.println("Создан спутник: " + commSat2.getName() + " (" + commSat2.getEnergy().getBatteryLevel() + ")");

            ImagingSatellite imagingSat1 = new ImagingSatellite("ДЗЗ-1", 0.92, 2.5);
            System.out.println("Создан спутник: " + imagingSat1.getName() + " (" + imagingSat1.getEnergy().getBatteryLevel() + ")");

            ImagingSatellite imagingSat2 = new ImagingSatellite("ДЗЗ-2", 0.45, 1.0);
            System.out.println("Создан спутник: " + imagingSat2.getName() + " (" + imagingSat2.getEnergy().getBatteryLevel() + ")");

            ImagingSatellite imagingSat3 = new ImagingSatellite("ДЗЗ-3", 0.15, 5.0);
            System.out.println("Создан спутник: " + imagingSat3.getName() + " (" + imagingSat3.getEnergy().getBatteryLevel() + ")");

            System.out.println("---------------------------------------------");

            operationCenterService.createAndSaveConstellation("Орбита-1");
            operationCenterService.createAndSaveConstellation("Орбита-2");

            System.out.println("---------------------------------------------");
            System.out.println("\n📡 ДОБАВЛЕНИЕ СПУТНИКОВ:");

            operationCenterService.addSatelliteToConstellation("Орбита-1", commSat1);
            operationCenterService.addSatelliteToConstellation("Орбита-1", imagingSat1);
            operationCenterService.addSatelliteToConstellation("Орбита-1", imagingSat2);
            operationCenterService.addSatelliteToConstellation("Орбита-2", commSat2);
            operationCenterService.addSatelliteToConstellation("Орбита-2", imagingSat3);

            System.out.println("-----------------------------------");

            operationCenterService.activateAllSatellites("Орбита-1");
            operationCenterService.executeConstellationMission("Орбита-1");
            operationCenterService.showConstellationStatus("Орбита-1");

            System.out.println(constellationRepository.getAllConstellations());

        } finally {
            context.close();
        }
    }
}