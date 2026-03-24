package seminars.satellite.factory;

import org.springframework.stereotype.Component;
import seminars.exception.SpaceOperationException;
import seminars.satellite.ImagingSatellite;
import seminars.satellite.Satellite;
import seminars.satellite.SatelliteType;
import seminars.satellite.param.ImagingSatelliteParam;
import seminars.satellite.param.SatelliteParam;

@Component
public class ImagingSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam param) {
        if (!(param instanceof ImagingSatelliteParam imgParam)) {
            throw new SpaceOperationException("Передан неверный тип параметров для ImagingSatelliteFactory");
        }
        return new ImagingSatellite(imgParam.getName(), imgParam.getBatteryLevel(), imgParam.getResolution());
    }

    @Override
    public boolean isSatelliteTypeSupported(SatelliteType type) {
        return type == SatelliteType.IMAGE;
    }
}