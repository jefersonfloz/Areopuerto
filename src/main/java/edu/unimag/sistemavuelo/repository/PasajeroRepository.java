package edu.unimag.sistemavuelo.repository;
import edu.unimag.sistemavuelo.entities.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;import java.util.Optional;

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {

    // Buscar un pasajero por su número de identificación (nid)
    @Query("SELECT p FROM Pasajero p WHERE p.nid = ?1")
    Optional<Pasajero> buscarPorNid(String nid);

    // Obtener todos los pasajeros que tengan al menos una reserva
    @Query("SELECT p FROM Pasajero p WHERE SIZE(p.reservas) > 0")
    List<Pasajero> obtenerPasajerosConReservas();

    // Buscar pasajeros cuyo nombre empiece con una letra específica
    @Query("SELECT p FROM Pasajero p WHERE p.nombre LIKE CONCAT(?1, '%')")
    List<Pasajero> buscarPorLetraInicial(String letra);

    // Contar cuántos pasajeros tienen pasaporte registrado
    @Query("SELECT COUNT(p) FROM Pasajero p WHERE p.pasaporte IS NOT NULL")
    long contarPasajerosConPasaporte();

    // Obtener los nombres y sus identificadores de todos los pasajeros, ordenados por nombre
    @Query("SELECT p.id, p.nombre FROM Pasajero p ORDER BY p.nombre ASC")
    List<Object[]> obtenerIdYNombreOrdenados();

    // Buscar un pasajero por su nombre exacto
    List<Pasajero> findByNombre(String nombre);

    // Buscar un pasajero por su número de identificación (nid)
    Optional<Pasajero> findByNid(String nid);

    // Buscar pasajeros que tengan al menos una reserva
    List<Pasajero> findByReservasIsNotEmpty();

    // Buscar pasajeros cuyo nombre contenga una palabra clave (ignorando mayúsculas y minúsculas)
    List<Pasajero> findByNombreContainingIgnoreCase(String keyword);

    // Buscar pasajeros que no tienen pasaporte asignado
    List<Pasajero> findByPasaporteIsNull();
}