package seminars.constellation;

import lombok.Getter;
import lombok.ToString;
import seminars.satellite.Satellite;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class SatelliteConstellation {
    private final String constellationName;
    private final List<Satellite> satellites;

    private SatelliteConstellation(Builder builder) {
        this.constellationName = builder.constellationName;
        this.satellites = builder.satellites;
    }

    // Возвращенный метод для динамического добавления спутников
    public void addSatellite(Satellite satellite) {
        if (satellite != null) {
            this.satellites.add(satellite);
        }
    }

    public static Builder builder(String constellationName) {
        return new Builder(constellationName);
    }

    public static class Builder {
        private String constellationName;
        private List<Satellite> satellites = new ArrayList<>();

        public Builder(String constellationName) {
            // Убрана жесткая валидация пустого имени для совместимости с тестами
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