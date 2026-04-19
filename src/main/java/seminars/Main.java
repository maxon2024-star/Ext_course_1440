package seminars;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import seminars.repository.ConstellationRepository;
import seminars.satellite.Satellite;
import seminars.satellite.factory.CommunicationSatelliteFactory;
import seminars.satellite.factory.ImagingSatelliteFactory;
import seminars.satellite.factory.SatelliteFactory;
import seminars.satellite.param.CommunicationSatelliteParam;
import seminars.satellite.param.ImagingSatelliteParam;
import seminars.service.ConstellationService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        try {
            ConstellationRepository constellationRepository = context.getBean(ConstellationRepository.class);
            ConstellationService operationCenterService = context.getBean(ConstellationService.class);

            // Инициализация фабрик
            SatelliteFactory commFactory = new CommunicationSatelliteFactory();
            SatelliteFactory imgFactory = new ImagingSatelliteFactory();

            System.out.println("\nСОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ (ЧЕРЕЗ ФАБРИКУ):");
            System.out.println("---------------------------------------------");

            // Создаем спутники через фабричный метод, работая с абстракцией Satellite и используя классы параметров
            Satellite commSat1 = commFactory.createSatelliteWithParameter(new CommunicationSatelliteParam("Связь-1", 0.85, 500.0));
            System.out.println("Создан спутник: " + commSat1.getName() + " (" + commSat1.getEnergy().getBatteryLevel() + ")");

            Satellite commSat2 = commFactory.createSatelliteWithParameter(new CommunicationSatelliteParam("Связь-2", 0.75, 1000.0));
            System.out.println("Создан спутник: " + commSat2.getName() + " (" + commSat2.getEnergy().getBatteryLevel() + ")");

            Satellite imagingSat1 = imgFactory.createSatelliteWithParameter(new ImagingSatelliteParam("ДЗЗ-1", 0.92, 2.5));
            System.out.println("Создан спутник: " + imagingSat1.getName() + " (" + imagingSat1.getEnergy().getBatteryLevel() + ")");

            Satellite imagingSat2 = imgFactory.createSatelliteWithParameter(new ImagingSatelliteParam("ДЗЗ-2", 0.45, 1.0));
            System.out.println("Создан спутник: " + imagingSat2.getName() + " (" + imagingSat2.getEnergy().getBatteryLevel() + ")");

            Satellite imagingSat3 = imgFactory.createSatelliteWithParameter(new ImagingSatelliteParam("ДЗЗ-3", 0.15, 5.0));
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

        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }
}