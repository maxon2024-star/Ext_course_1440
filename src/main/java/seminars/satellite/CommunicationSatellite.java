package seminars.satellite;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class CommunicationSatellite extends Satellite {
    private final double bandwidth;

    public CommunicationSatellite(String name, double initialEnergy, double bandwidth) {
        super(name, initialEnergy);
        this.bandwidth = bandwidth;
    }

    @Override
    public void activate() {
        if (energy.consumeEnergy(0.05)) {
            state = new SatelliteState(true, "Активен");
            System.out.println("✅ " + name + ": Активация успешна");
        } else {
            state = new SatelliteState(false, "Недостаточно энергии");
            System.out.println("❌ " + name + ": Недостаточно энергии для активации");
        }
    }

    @Override
    public void executeMission() {
        if (state.isActive() && energy.consumeEnergy(0.1)) {
            System.out.println(name + ": Передача данных со скоростью " + bandwidth + " Мбит/с");
            System.out.println(name + ": Отправил " + bandwidth + " Мбит данных!");
        } else {
            System.out.println(name + ": Невозможно выполнить миссию (не активен или недостаточно энергии)");
        }
    }

    @Override
    public SatelliteState getState() {
        return state;
    }
}