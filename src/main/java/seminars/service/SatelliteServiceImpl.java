package seminars.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seminars.exception.SpaceOperationException;
import seminars.satellite.Satellite;
import seminars.satellite.factory.SatelliteFactory;
import seminars.satellite.param.SatelliteParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SatelliteServiceImpl implements SatelliteService {

    // Внедряем все бины, реализующие SatelliteFactory
    private final List<SatelliteFactory> factories;

    @Override
    public Satellite createSatellite(SatelliteParam param) {
        return factories.stream()
                .filter(factory -> factory.isSatelliteTypeSupported(param.getType()))
                .findFirst()
                .orElseThrow(() -> new SpaceOperationException("Фабрика для типа спутника " + param.getType() + " не найдена"))
                .createSatelliteWithParameter(param);
    }
}