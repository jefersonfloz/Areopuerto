package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.entities.Pasaporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PasaporteRepository extends JpaRepository<Pasaporte, Long> {

    // Query Methods
    Optional<Pasaporte> findById(Long id);

    List<Pasaporte> findByIdGreaterThan(Long id);

    List<Pasaporte> findByIdLessThan(Long id);

    List<Pasaporte> findByIdBetween(Long start, Long end);

    List<Pasaporte> findByIdOrderByIdDesc(Long id);

    // JPQL
    @Query("SELECT p FROM Pasaporte p WHERE p.pasajero IS NULL")
    List<Pasaporte> obtenerPasaportesSinPasajero();

    @Query("SELECT p FROM Pasaporte p ORDER BY p.id ASC")
    List<Pasaporte> listarOrdenadosPorIdAsc();

    @Query("SELECT p FROM Pasaporte p WHERE p.id < ?1")
    List<Pasaporte> buscarPorIdMenorQue(Long id);

    @Query("SELECT COUNT(p) FROM Pasaporte p")
    long contarPasaportes();

    @Query("SELECT p FROM Pasaporte p ORDER BY p.id DESC")
    List<Pasaporte> obtenerTodosOrdenadosPorIdDesc();

}
