package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.entities.Pasajero;
import edu.unimag.sistemavuelo.entities.Reserva;
import edu.unimag.sistemavuelo.entities.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // ====================== @Query Methods ======================

    // Buscar una reserva por su código único
    @Query("SELECT r FROM Reserva r WHERE r.codigoReserva = ?1")
    Optional<Reserva> buscarPorCodigo(UUID codigoReserva);

    // Obtener todas las reservas de un pasajero específico
    @Query("SELECT r FROM Reserva r WHERE r.pasajero.id = ?1")
    List<Reserva> obtenerReservasPorPasajero(Long pasajeroId);

    // Contar cuántas reservas están asociadas a un vuelo específico
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.vuelo.id = ?1")
    long contarReservasPorVuelo(Long vueloId);

    // Buscar todas las reservas de un pasajero, ordenadas por ID descendente
    @Query("SELECT r FROM Reserva r WHERE r.pasajero.id = ?1 ORDER BY r.id DESC")
    List<Reserva> obtenerReservasPasajeroOrdenadas(Long pasajeroId);

    // Verificar si un pasajero tiene una reserva en un vuelo específico
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Reserva r WHERE r.pasajero.id = ?1 AND r.vuelo.id = ?2")
    boolean existeReservaPorPasajeroYVuelo(Long pasajeroId, Long vueloId);

    // ====================== Query Methods ======================

    // Buscar una reserva por su código único
    Optional<Reserva> findByCodigoReserva(UUID codigoReserva);

    // Obtener todas las reservas de un pasajero
    List<Reserva> findByPasajero(Pasajero pasajero);

    // Contar cuántas reservas tiene un vuelo
    long countByVuelo(Vuelo vuelo);

    // Obtener todas las reservas de un vuelo
    List<Reserva> findByVuelo(Vuelo vuelo);

    // Buscar reservas por pasajero y vuelo específicos
    List<Reserva> findByPasajeroAndVuelo(Pasajero pasajero, Vuelo vuelo);
}
