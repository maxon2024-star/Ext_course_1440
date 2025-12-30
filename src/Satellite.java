public abstract class Satellite {
    protected String name;
    protected boolean isActive;
    protected double batteryLevel;

    public Satellite(String name, double batteryLevel) {
        this.name = name;
        this.batteryLevel = batteryLevel;
        this.isActive = false;
    }

    public boolean activate() {
        if (batteryLevel > 0.2) {
            isActive = true;
            return true;
        }
        return false;
    }

    public void deactivate() {
        isActive = false;
    }

    public void consumeBattery(double amount) {
        batteryLevel -= amount;
        if (batteryLevel <= 0.2) {
            deactivate();
        }
    }

    protected abstract void performMission();

    @Override
    public String toString() {
        return String.format("%s{name='%s', isActive=%b, batteryLevel=%.2f}",
                this.getClass().getSimpleName(), name, isActive, batteryLevel);
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }
}