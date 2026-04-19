package seminars.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seminars.dto.AddSatelliteRequest;
import seminars.dto.MissionRequest;
import seminars.repository.ConstellationRepository;
import seminars.satellite.Satellite;
import seminars.satellite.factory.CommunicationSatelliteFactory;
import seminars.satellite.factory.ImagingSatelliteFactory;
import seminars.satellite.factory.SatelliteFactory;
import seminars.satellite.param.CommunicationSatelliteParam;
import seminars.satellite.param.ImagingSatelliteParam;
import seminars.service.ConstellationService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Space Operations", description = "Управление спутниковыми группировками")
public class SpaceOperationController {

    private final ConstellationService constellationService;
    private final ConstellationRepository constellationRepository;

    @PostMapping("/constellations")
    @Operation(summary = "Создать новую группировку")
    public ResponseEntity<Void> createConstellation(@RequestParam String name) {
        constellationService.createAndSaveConstellation(name);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-satellites")
    @Operation(summary = "Добавить спутник в группировку")
    public ResponseEntity<Void> addSatellite(@RequestBody AddSatelliteRequest request) {
        SatelliteFactory factory;
        if (request.getSatelliteParam() instanceof CommunicationSatelliteParam) {
            factory = new CommunicationSatelliteFactory();
        } else {
            factory = new ImagingSatelliteFactory();
        }

        Satellite satellite = factory.createSatelliteWithParameter(request.getSatelliteParam());
        constellationService.addSatelliteToConstellation(request.getConstellationName(), satellite);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/missions")
    @Operation(summary = "Выполнить миссию")
    public ResponseEntity<String> executeMission(@RequestBody MissionRequest request) {
        if (request.getTargetType() == MissionRequest.TargetType.CONSTELLATION) {
            constellationService.activateAllSatellites(request.getConstellationName());
            constellationService.executeConstellationMission(request.getConstellationName());
            return ResponseEntity.ok("Миссия для группировки " + request.getConstellationName() + " запущена");

        } else if (request.getTargetType() == MissionRequest.TargetType.SINGLE_SATELLITE) {
            // Реализация миссии для одиночного спутника
            var constellationOpt = constellationRepository.findByName(request.getConstellationName());

            if (constellationOpt.isPresent()) {
                Optional<Satellite> targetSatellite = constellationOpt.get().getSatellites().stream()
                        .filter(s -> s.getName().equals(request.getSatelliteName()))
                        .findFirst();

                if (targetSatellite.isPresent()) {
                    targetSatellite.get().activate();
                    targetSatellite.get().executeMission();
                    return ResponseEntity.ok("Миссия для одиночного спутника " + request.getSatelliteName() + " запущена");
                } else {
                    return ResponseEntity.badRequest().body("Спутник " + request.getSatelliteName() + " не найден в группировке");
                }
            } else {
                return ResponseEntity.badRequest().body("Группировка " + request.getConstellationName() + " не найдена");
            }
        }

        return ResponseEntity.badRequest().body("Неизвестный тип миссии");
    }

    @GetMapping("/overview")
    @Operation(summary = "Общая сводка системы")
    public ResponseEntity<String> getOverview() {
        return ResponseEntity.ok(constellationRepository.getAllConstellations().toString());
    }

    @DeleteMapping("/constellations/{constellationName}/satellites/{satelliteName}")
    @Operation(summary = "Вывод спутника из эксплуатации")
    public ResponseEntity<Void> decommissionSatellite(
            @PathVariable String constellationName,
            @PathVariable String satelliteName) {

        // Находим группировку и удаляем спутник с совпадающим именем прямо из списка
        constellationRepository.findByName(constellationName).ifPresent(constellation -> {
            constellation.getSatellites().removeIf(satellite -> satellite.getName().equals(satelliteName));
        });

        return ResponseEntity.noContent().build();
    }
}