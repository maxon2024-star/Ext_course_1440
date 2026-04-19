package seminars.satellite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "imaging_satellite")
@Getter
@Setter
@NoArgsConstructor // Для Hibernate
@ToString(callSuper = true)
public class ImagingSatellite extends Satellite {

    @Column(name = "photos_taken")
    private int photosTaken;

    @Column(nullable = false)
    private double resolution;

    public ImagingSatellite(String name, double initialEnergy, double resolution) {
        super(name, initialEnergy);
        this.photosTaken = 0;
        this.resolution = resolution;
    }

    @Override
    public void activate() {
        if (energy.consumeEnergy(0.08)) {
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
        if (state.isActive() && energy.consumeEnergy(0.15)) {
            photosTaken++;
            System.out.println(name + ": Съемка территории с разрешением " + resolution + " м/пиксель");
            System.out.println(name + ": Снимок #" + photosTaken + " сделан!");
        } else {
            System.out.println(name + ": Невозможно выполнить миссию (не активен или недостаточно энергии)");
        }
    }
}