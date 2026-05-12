package seminars.satellite;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import seminars.constellation.SatelliteConstellation;

@Entity
@Table(name = "satellite")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString(exclude = "constellation")
@NoArgsConstructor // Для Hibernate
public abstract class Satellite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected String name;

    @Column(name = "temperature_inside")
    private Double temperatureInside;

    @Column(name = "temperature_outside")
    private Double temperatureOutside;

    @Embedded
    protected SatelliteState state;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "energy_system_id")
    protected EnergySystem energy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constellation_id")
    @JsonBackReference // Не выводим группировку внутри спутника (защита от рекурсии)
    protected SatelliteConstellation constellation;

    public Satellite(String name, double initialEnergy) {
        this.name = name;
        this.state = new SatelliteState(false, "Не активирован");
        this.energy = EnergySystem.builder()
                .batteryLevel(initialEnergy)
                .build();
    }

    public abstract void activate();
    public abstract void executeMission();

    public SatelliteState getState() { return state; }
    public EnergySystem getEnergy() { return energy; }
}