package seminars.constellation;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import seminars.satellite.Satellite;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "satellite_constellation")
@Getter
@Setter
@ToString(exclude = "satellites") // Исключаем, чтобы не было StackOverflow при логах
@NoArgsConstructor // Для Hibernate
public class SatelliteConstellation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "constellation_name", nullable = false, unique = true)
    private String constellationName;

    @OneToMany(mappedBy = "constellation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Спасает от бесконечной рекурсии в контроллере
    private List<Satellite> satellites = new ArrayList<>();

    private SatelliteConstellation(Builder builder) {
        this.constellationName = builder.constellationName;
        // Копируем спутники и жестко связываем их с группировкой
        if (builder.satellites != null) {
            builder.satellites.forEach(this::addSatellite);
        }
    }

    public void addSatellite(Satellite satellite) {
        if (satellite != null) {
            this.satellites.add(satellite);
            satellite.setConstellation(this);
        }
    }

    public static Builder builder(String constellationName) {
        return new Builder(constellationName);
    }

    public static class Builder {
        private String constellationName;
        private List<Satellite> satellites = new ArrayList<>();

        public Builder(String constellationName) {
            this.constellationName = constellationName;
        }

        public Builder addSatellite(Satellite satellite) {
            if (satellite != null) {
                this.satellites.add(satellite);
            }
            return this;
        }

        public SatelliteConstellation build() {
            return new SatelliteConstellation(this);
        }
    }
}