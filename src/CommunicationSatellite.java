public class CommunicationSatellite extends Satellite {
    private double bandWidth;

    public CommunicationSatellite(String name, double batteryLevel, double bandWidth) {
        super(name, batteryLevel);
        this.bandWidth = bandWidth;
    }

    @Override
    protected void performMission() {
        if (state.isActive()) {
            System.out.printf("%s: Передача данных со скоростью %.1f Мбит/с%n", name, bandWidth);
            sendData(bandWidth);
            consumeBattery(0.05);
        } else {
            System.out.printf("🛑 %s: Не может выполнить передачу - не активен%n", name);
        }
    }

    public void sendData(double amount) {
        if (state.isActive()) {
            System.out.printf("%s: Отправил %.1f Мбит данных!%n", name, amount);
        }
    }

    public double getBandwidth() {
        return bandWidth;
    }

    @Override
    public String toString() {
        return String.format("CommunicationSatellite{bandwidth=%.1f, name='%s', isActive=%b, batteryLevel=%.2f}",
                bandWidth, name, state.isActive(), energy.getBatteryLevel());
    }
}