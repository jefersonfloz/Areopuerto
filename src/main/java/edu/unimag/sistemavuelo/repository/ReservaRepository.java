package edu.unimag.sistemavuelo.repository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import edu.unimag.sistemavuelo.entities.Pasajero;
import edu.unimag.sistemavuelo.entities.Reserva;
import edu.unimag.sistemavuelo.entities.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    //buscar una reserva por su codigoReserva
    Optional<Reserva> findByCodigoReserva(UUID codigoReserva);

    //eliminar una reserva
    void delete(Reserva reserva);

    //buscar todas las reservas de un pasajero
    List<Reserva> findByPasajero(Pasajero pasajero);

    //buscar todas las reservas de un vuelo
    List<Reserva> findByVuelo(Vuelo vuelo);

    //buscar todas las reservas paginadas de 50 en 50
    Page<Reserva> findAll(Pageable pageable);



    // Contar las reservas de un pasajero
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.pasajero = ?1")
    long countReservasByPasajero(Pasajero pasajero);


    //modificar una reserva dado su codigoReserva
    @Modifying
    @Transactional
    @Query("UPDATE Reserva r SET r.pasajero = ?1, r.vuelo = ?2 WHERE r.codigoReserva = ?3")
    void updateReserva(Pasajero pasajero, Vuelo vuelo, UUID codigoReserva);


    //modificar el pasajero de una reserva dado el codigoReserva
    @Modifying
    @Transactional
    @Query("UPDATE Reserva r SET r.pasajero = ?1 WHERE r.codigoReserva = ?2")
    void updatePasajero(Pasajero pasajero, UUID codigoReserva);

    //modificar el vuelo de una reserva dado el codigoReserva
    @Modifying
    @Transactional
    @Query("UPDATE Reserva r SET r.vuelo = ?1 WHERE r.codigoReserva = ?2")
    void updateVuelo(Vuelo vuelo, UUID codigoReserva);


//Eliminar todas las reservas asociadas a un vuelo
    @Modifying
    @Transactional
    @Query("DELETE FROM Reserva r WHERE r.vuelo = ?1")
    void deleteReservasbyVuelo(Vuelo vuelo);


}