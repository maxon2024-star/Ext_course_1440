public class SatelliteState {
    private boolean active;
    private final EnergySystem energySystem;

    public SatelliteState(EnergySystem energySystem) {
        this.energySystem = energySystem;
        this.active = false;
    }

    public boolean activate() {
        if (energySystem.canActivate()) {
            active = true;
            return true;
        }
        return false;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public boolean requiresDeactivation() {
        return active && !energySystem.hasEnergy();
    }
}