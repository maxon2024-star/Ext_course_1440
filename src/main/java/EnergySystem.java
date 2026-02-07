public class EnergySystem {
    private double batteryLevel;
    private final double minBatteryLevel = 0.2;

    public EnergySystem(double initialBatteryLevel) {
        this.batteryLevel = initialBatteryLevel;
        if (this.batteryLevel > 1.0) {
            this.batteryLevel = 1.0;
        } else if (this.batteryLevel < 0.0) {
            this.batteryLevel = 0.0;
        }
    }

    public void consume(double amount) {
        batteryLevel -= amount;
        if (batteryLevel < 0) {
            batteryLevel = 0;
        }
    }

    public boolean canActivate() {
        return batteryLevel > minBatteryLevel;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public boolean hasEnergy() {
        return batteryLevel > 0;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
        if (this.batteryLevel > 1.0) {
            this.batteryLevel = 1.0;
        } else if (this.batteryLevel < 0.0) {
            this.batteryLevel = 0.0;
        }
    }

    @Override
    public String toString() {
        return String.format("EnergySystem{batteryLevel=%.2f}", batteryLevel);
    }
}