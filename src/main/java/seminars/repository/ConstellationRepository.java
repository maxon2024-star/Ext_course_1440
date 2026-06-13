package seminars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seminars.constellation.SatelliteConstellation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface ConstellationRepository extends JpaRepository<SatelliteConstellation, Long> {

    Optional<SatelliteConstellation> findByConstellationName(String name);
    boolean existsByConstellationName(String name);
    void deleteByConstellationName(String name);

    // =========================================================
    // Блок совместимости со старым кодом (чтобы не ломать сервисы)
    // =========================================================

    default Optional<SatelliteConstellation> findByName(String name) {
        return findByConstellationName(name);
    }

    default Map<String, SatelliteConstellation> getAllConstellations() {
        return findAll().stream()
                .collect(Collectors.toMap(SatelliteConstellation::getConstellationName, c -> c));
    }

    default boolean existsByName(String name) {
        return existsByConstellationName(name);
    }

    default void deleteByName(String name) {
        deleteByConstellationName(name);
    }

    default void clear() {
        deleteAll();
    }

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO satellite_constellation (constellation_name) VALUES (:name) ON CONFLICT (constellation_name) DO NOTHING", nativeQuery = true)
    void insertConstellationIfNotExists(@Param("name") String name);
}