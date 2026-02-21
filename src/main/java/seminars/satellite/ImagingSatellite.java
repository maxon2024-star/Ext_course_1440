package seminars.satellite;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ImagingSatellite extends Satellite {
    private int photosTaken;
    private final double resolution;

    public ImagingSatellite(String name, double initialEnergy, double resolution) {
        super(name, initialEnergy);
        this.photosTaken = 0;
        this.resolution = resolution;
    }

    @Override
    public void activate() {
        if (energy.consumeEnergy(0.08)) {
            state = new SatelliteState(true, "Активен");
            System.out.println("✅ " + name + ": Активация успешна");
        } else {
            state = new SatelliteState(false, "Недостаточно энергии");
            System.out.println("❌ " + name + ": Недостаточно энергии для активации");
        }
    }

    @Override
    public void executeMission() {
        if (state.isActive() && energy.consumeEnergy(0.15)) {
            photosTaken++;
            System.out.println(name + ": Съемка территории с разрешением " + resolution + " м/пиксель");
            System.out.println(name + ": Снимок #" + photosTaken + " сделан!");
        } else {
            System.out.println(name + ": Невозможно выполнить миссию (не активен или недостаточно энергии)");
        }
    }

    @Override
    public SatelliteState getState() {
        return state;
    }
}