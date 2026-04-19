package seminars.satellite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "communication_satellite")
@Getter
@Setter
@NoArgsConstructor // Для Hibernate
@ToString(callSuper = true)
public class CommunicationSatellite extends Satellite {

    @Column(nullable = false)
    private double bandwidth;

    public CommunicationSatellite(String name, double initialEnergy, double bandwidth) {
        super(name, initialEnergy);
        this.bandwidth = bandwidth;
    }

    @Override
    public void activate() {
        if (energy.consumeEnergy(0.05)) {
            state.setActive(true);
            state.setMessage("Активен");
            System.out.println("✅ " + name + ": Активация успешна");
        } else {
            state.setActive(false);
            state.setMessage("Недостаточно энергии");
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
}