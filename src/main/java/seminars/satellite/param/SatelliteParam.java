package seminars.satellite.param;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import seminars.satellite.SatelliteType;

@Data
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true // Это нужно, чтобы поле type не пропадало при десериализации
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommunicationSatelliteParam.class, name = "COMMUNICATION"),
        @JsonSubTypes.Type(value = ImagingSatelliteParam.class, name = "IMAGE")
})
public abstract class SatelliteParam {
    protected SatelliteType type;
    protected String name;
    protected double batteryLevel;

    public SatelliteParam(SatelliteType type, String name, double batteryLevel) {
        this.type = type;
        this.name = name;
        this.batteryLevel = batteryLevel;
    }
}