package seminars.satellite.factory;

import org.springframework.stereotype.Component;
import seminars.exception.SpaceOperationException;
import seminars.satellite.CommunicationSatellite;
import seminars.satellite.Satellite;
import seminars.satellite.SatelliteType;
import seminars.satellite.param.CommunicationSatelliteParam;
import seminars.satellite.param.SatelliteParam;

@Component
public class CommunicationSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam param) {
        if (!(param instanceof CommunicationSatelliteParam commParam)) {
            throw new SpaceOperationException("Передан неверный тип параметров для CommunicationSatelliteFactory");
        }
        return new CommunicationSatellite(commParam.getName(), commParam.getBatteryLevel(), commParam.getBandwidth());
    }

    @Override
    public boolean isSatelliteTypeSupported(SatelliteType type) {
        return type == SatelliteType.COMMUNICATION;
    }
}