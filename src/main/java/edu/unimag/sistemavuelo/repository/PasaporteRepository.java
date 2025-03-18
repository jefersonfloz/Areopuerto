package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.entities.Pasaporte;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PasaporteRepository extends JpaRepository<Pasaporte, Long> {

    // Buscar un pasaporte por su ID
    Optional<Pasaporte> findById(Long id);

    // Buscar pasaportes con ID mayor a un valor dado
    List<Pasaporte> findByIdGreaterThan(Long id);

    // Buscar pasaportes con ID menor a un valor dado
    List<Pasaporte> findByIdLessThan(Long id);

    // Buscar pasaportes dentro de un rango de ID
    List<Pasaporte> findByIdBetween(Long start, Long end);

    // Buscar pasaportes ordenados por ID descendente
    List<Pasaporte> findByIdOrderByIdDesc(Long id);

    // Listar pasaportes ordenados por ID ascendente
    @Query("SELECT p FROM Pasaporte p ORDER BY p.id ASC")
    List<Pasaporte> listarOrdenadosPorIdAsc();

    // Obtener pasaportes sin pasajero asignado
    @Query("SELECT p FROM Pasaporte p WHERE p.pasajero IS NULL")
    List<Pasaporte> obtenerPasaportesSinPasajero();


    // Contar la cantidad total de pasaportes
    @Query("SELECT COUNT(p) FROM Pasaporte p")
    long contarPasaportes();

    // Obtener todos los pasaportes ordenados por ID descendente
    @Query("SELECT p FROM Pasaporte p ORDER BY p.id DESC")
    List<Pasaporte> obtenerTodosOrdenadosPorIdDesc();

    // Actualizar el número de un pasaporte dado su ID
    @Modifying
    @Transactional
    @Query("UPDATE Pasaporte p SET p.numero = :numero WHERE p.id = :id")
    void updatePasaporteNumeroById(Long id, String numero);

}

