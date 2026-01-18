public abstract class Satellite {
    protected String name;
    protected SatelliteState state;
    protected EnergySystem energy;

    public Satellite(String name, double batteryLevel) {
        this.name = name;
        this.energy = new EnergySystem(batteryLevel);
        this.state = new SatelliteState(energy);
    }

    public boolean activate() {
        return state.activate();
    }

    public void deactivate() {
        state.deactivate();
    }

    protected void consumeBattery(double amount) {
        energy.consume(amount);
        if (state.requiresDeactivation()) {
            deactivate();
        }
    }

    protected abstract void performMission();

    @Override
    public String toString() {
        return String.format("%s{name='%s', isActive=%b, batteryLevel=%.2f}",
                this.getClass().getSimpleName(), name, state.isActive(), energy.getBatteryLevel());
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return state.isActive();
    }

    public double getBatteryLevel() {
        return energy.getBatteryLevel();
    }
}