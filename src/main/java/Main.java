import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

// ⭐ Полностью отключаем автонастройку
@SpringBootApplication(scanBasePackages = "", exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class,
        org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration.class,
        org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration.class,
        org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration.class
})

public class Main implements CommandLineRunner {

    public static void main(String[] args) {
        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================\n");

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        // Получаем бины из контекста Spring
        ConstellationRepository constellationRepository = context.getBean(ConstellationRepository.class);
        SpaceOperationCenterService operationCenterService = context.getBean(SpaceOperationCenterService.class);

        // Создание спутников
        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        CommunicationSatellite commSat1 = new CommunicationSatellite("Связь-1", 0.85, 500.0);
        CommunicationSatellite commSat2 = new CommunicationSatellite("Связь-2", 0.75, 1000.0);
        ImagingSatellite imagingSat1 = new ImagingSatellite("ДЗЗ-1", 0.92, 2.5);
        ImagingSatellite imagingSat2 = new ImagingSatellite("ДЗЗ-2", 0.45, 1.0);
        ImagingSatellite imagingSat3 = new ImagingSatellite("ДЗЗ-3", 0.15, 0.5);

        System.out.printf("Создан спутник: %s (%.2f)%n", commSat1.getName(), commSat1.getBatteryLevel());
        System.out.printf("Создан спутник: %s (%.2f)%n", commSat2.getName(), commSat2.getBatteryLevel());
        System.out.printf("Создан спутник: %s (%.2f)%n", imagingSat1.getName(), imagingSat1.getBatteryLevel());
        System.out.printf("Создан спутник: %s (%.2f)%n", imagingSat2.getName(), imagingSat2.getBatteryLevel());
        System.out.printf("Создан спутник: %s (%.2f)%n", imagingSat3.getName(), imagingSat3.getBatteryLevel());

        System.out.println("---------------------------------------------");

        // Создание группировок через сервис
        SatelliteConstellation constellation1 = operationCenterService.createAndSaveConstellation("Орбита-1");
        SatelliteConstellation constellation2 = operationCenterService.createAndSaveConstellation("Орбита-2");

        System.out.println("---------------------------------------------");

        // Добавление спутников в группировки
        System.out.println("\n📡 ДОБАВЛЕНИЕ СПУТНИКОВ:");
        operationCenterService.addSatelliteToConstellation("Орбита-1", commSat1);
        operationCenterService.addSatelliteToConstellation("Орбита-1", imagingSat1);
        operationCenterService.addSatelliteToConstellation("Орбита-1", imagingSat2);
        operationCenterService.addSatelliteToConstellation("Орбита-2", commSat2);
        operationCenterService.addSatelliteToConstellation("Орбита-2", imagingSat3);

        System.out.println("-----------------------------------");

        // Активация спутников
        operationCenterService.activateAllSatellites("Орбита-1");
        System.out.println();

        // Выполнение миссий
        operationCenterService.executeConstellationMission("Орбита-1");
        System.out.println();

        // Показ статуса
        operationCenterService.showConstellationStatus("Орбита-1");

        // Вывод всех группировок из репозитория
        operationCenterService.displayAllConstellations();

        context.close();
    }

    @Override
    public void run(String... args) throws Exception {
        // Spring Boot автоматически запускает приложение
    }
}