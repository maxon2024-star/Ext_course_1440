package seminars.satellite;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EnergySystem {
    private double batteryLevel;

    // Скрытый конструктор, доступный только Строителю
    private EnergySystem(Builder builder) {
        this.batteryLevel = builder.batteryLevel;
    }

    public boolean consumeEnergy(double amount) {
        if (batteryLevel >= amount) {
            batteryLevel -= amount;
            return true;
        }
        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Вложенный класс Строителя
    public static class Builder {
        private double batteryLevel = 100.0; // Дефолтное значение уровня энергии

        public Builder batteryLevel(double batteryLevel) {
            if (batteryLevel < 0) {
                throw new IllegalArgumentException("Уровень энергии не может быть отрицательным");
            }
            this.batteryLevel = batteryLevel;
            return this;
        }

        public EnergySystem build() {
            return new EnergySystem(this);
        }
    }
}