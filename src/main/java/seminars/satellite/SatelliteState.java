package seminars.satellite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SatelliteState {
    private final boolean isActive;
    private final String statusMessage;
}