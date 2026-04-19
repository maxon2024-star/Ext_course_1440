package seminars.satellite;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "energy_system")
@Getter
@Setter
@ToString
@NoArgsConstructor // Обязательно для Hibernate
public class EnergySystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "battery_level")
    private double batteryLevel;

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

    public static class Builder {
        private double batteryLevel = 100.0;

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