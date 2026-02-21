package seminars.satellite;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EnergySystem {
    private double batteryLevel;

    public EnergySystem(double initialLevel) {
        this.batteryLevel = initialLevel;
    }

    public boolean consumeEnergy(double amount) {
        if (batteryLevel >= amount) {
            batteryLevel -= amount;
            return true;
        }
        return false;
    }
}