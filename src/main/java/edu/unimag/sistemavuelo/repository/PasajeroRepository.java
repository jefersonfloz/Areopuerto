package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.entities.Pasajero;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
    // Buscar un pasajero por su Nid exacto
    List<Pasajero> findByNid(String nid);

    //Buscar todos los pasajeros que tengan el mismo nombre
    List<Pasajero> findByNombre(String nombre);

    //buscar un pasajero por su nombre y pasaporte
    Optional<Pasajero> findByNombreAndPasaporteId(String nombre, Long pasaporteId);

    //añadir un pasajero
    Pasajero save(Pasajero pasajero);

    //eliminar un pasajero
    void delete(Pasajero pasajero);

    @Modifying//Siempre usar cuando se vaya a modificar(insert, update, delete) la DB
    @Transactional //Asegura que se revierte una transacción a su estado anterior si ocurre un error.    //buscar un pasajero por su uid y modificar su nombre y apellido
    @Query("UPDATE Pasajero p SET p.nombre = :nombre, p.apellido = :apellido WHERE p.nid = :nid")
    void updatePasajeroUid(@Param("nid") String nid, @Param("nombre") String nombre, @Param("apellido") String apellido);

    // obtener los pasajeros de una reserva dado un codigo de reserva
    @Query("SELECT p FROM Pasajero p JOIN p.reservas r WHERE r.codigoReserva = :codigoReserva")
    List<Pasajero> findPasajerosByCodigoReserva(@Param("codigoReserva") String codigoReserva);

    @Modifying
    @Transactional //Asegura que se revierte una transacción a su estado anterior si ocurre un error.
    //eliminar la reserva que haya hecho un pasajero dado el nid del pasajero y el UUID de la reserva
    @Query("DELETE FROM Reserva r WHERE r.codigoReserva = :codigoReserva AND r.pasajero.nid = :nid")
    void deleteReservaByNidAndCodigoReserva(@Param("nid") String nid, @Param("codigoReserva") String codigoReserva);

    //obtener un pasajero dado su nombre y uid y actualizar su apellido
    @Modifying
    @Transactional //Asegura que se revierte una transacción a su estado anterior si ocurre un error.
    @Query("UPDATE Pasajero p SET p.apellido = :apellido WHERE p.nombre = :nombre AND p.id = :id")
    void updateApellidoPasajeroByNombreAndUid(@Param("nombre") String nombre, @Param("id") Long id, @Param("apellido") String apellido);

    //buscar un pasajero de un reserva dado el nid del pasajero y el uuid de la reserva
    @Query("SELECT p FROM Pasajero p JOIN p.reservas r WHERE r.codigoReserva = :codigoReserva AND p.nid = :nid")
    Optional<Pasajero> findPasajeroByNidAndCodigoReserva(@Param("nid") String nid, @Param("codigoReserva") String codigoReserva);



}