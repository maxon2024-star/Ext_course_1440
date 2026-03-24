package seminars.satellite;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class Satellite {
    protected String name;
    protected SatelliteState state;
    protected EnergySystem energy;

    public Satellite(String name, double initialEnergy) {
        this.name = name;
        this.state = new SatelliteState(false, "Не активирован");
        // Использование паттерна Builder
        this.energy = EnergySystem.builder()
                .batteryLevel(initialEnergy)
                .build();
    }

    public abstract void activate();
    public abstract void executeMission();
    public abstract SatelliteState getState();

    public EnergySystem getEnergy() {
        return energy;
    }
}