package seminars.satellite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteState {
    @Column(name = "is_active")
    private boolean active;

    @Column(name = "state_message")
    private String message;
}