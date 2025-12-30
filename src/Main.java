import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        // Создание спутников
        CommunicationSatellite commSat1 = new CommunicationSatellite("Связь-1", 0.85, 500.0);
        CommunicationSatellite commSat2 = new CommunicationSatellite("Связь-2", 0.75, 1000.0);
        ImagingSatellite imagingSat1 = new ImagingSatellite("ДЗЗ-1", 0.92, 2.5);
        ImagingSatellite imagingSat2 = new ImagingSatellite("ДЗЗ-2", 0.45, 1.0);
        ImagingSatellite imagingSat3 = new ImagingSatellite("ДЗЗ-3", 0.15, 0.5);

        System.out.printf("Создан спутник: %s (заряд: %.0f%%)%n", commSat1.getName(), commSat1.getBatteryLevel() * 100);
        System.out.printf("Создан спутник: %s (заряд: %.0f%%)%n", commSat2.getName(), commSat2.getBatteryLevel() * 100);
        System.out.printf("Создан спутник: %s (заряд: %.0f%%)%n", imagingSat1.getName(), imagingSat1.getBatteryLevel() * 100);
        System.out.printf("Создан спутник: %s (заряд: %.0f%%)%n", imagingSat2.getName(), imagingSat2.getBatteryLevel() * 100);
        System.out.printf("Создан спутник: %s (заряд: %.0f%%)%n", imagingSat3.getName(), imagingSat3.getBatteryLevel() * 100);

        System.out.println("---------------------------------------------");
        System.out.println("Создана спутниковая группировка: RU Basic");
        System.out.println("---------------------------------------------");

        // Создание группировки
        SatelliteConstellation constellation = new SatelliteConstellation("RU Basic");

        System.out.println("ФОРМИРОВАНИЕ ГРУППИРОВКИ:");
        System.out.println("-----------------------------------");

        // Добавление спутников в группировку
        addSatelliteToConstellation(constellation, commSat1);
        addSatelliteToConstellation(constellation, commSat2);
        addSatelliteToConstellation(constellation, imagingSat1);
        addSatelliteToConstellation(constellation, imagingSat2);
        addSatelliteToConstellation(constellation, imagingSat3);

        System.out.println("-----------------------------------");
        System.out.println(constellation.toString());
        System.out.println("-----------------------------------");

        System.out.println("АКТИВАЦИЯ СПУТНИКОВ:");
        System.out.println("-------------------------");

        // Активация спутников
        activateAndReport(commSat1);
        activateAndReport(commSat2);
        activateAndReport(imagingSat1);
        activateAndReport(imagingSat2);
        activateAndReport(imagingSat3);

        System.out.println("ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ RU BASIC");
        System.out.println("==================================================");

        // Выполнение миссий
        constellation.executeAllMissions();

        System.out.println(constellation.toString());
    }

    private static void addSatelliteToConstellation(SatelliteConstellation constellation, Satellite satellite) {
        constellation.addSatellite(satellite);
        System.out.printf("%s добавлен в группировку '%s'%n", satellite.getName(), constellation.getConstellationName());
    }

    private static void activateAndReport(Satellite satellite) {
        boolean success = satellite.activate();
        if (success) {
            System.out.printf("✅ %s: Активация успешна%n", satellite.getName());
        } else {
            System.out.printf("🛑 %s: Ошибка активации (заряд: %.0f%%)%n",
                    satellite.getName(), satellite.getBatteryLevel() * 100);
        }
    }
}