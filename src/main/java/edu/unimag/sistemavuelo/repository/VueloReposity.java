package edu.unimag.sistemavuelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.unimag.sistemavuelo.entities.Vuelo;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VueloReposity extends JpaRepository<Vuelo, Long> {
    // Query Methods
    List<Vuelo> findByOrigenAndDestino(String origen, String destino);

    List<Vuelo> findByDestinoContaining(String palabraClave);

    // Buscar vuelos con más de una reserva
    List<Vuelo> findByReservasIsNotEmpty();

    // Buscar vuelos sin reservas
    List<Vuelo> findByReservasIsEmpty();


    //JPQL
    @Query("SELECT v FROM Vuelo v WHERE v.numeroVuelo = ?1")
    Optional<Vuelo> buscarPorNumeroVuelo(UUID numeroVuelo);

    // Obtener vuelos con un número mínimo de reservas
    @Query("SELECT v FROM Vuelo v WHERE SIZE(v.reservas) >= ?1")
    List<Vuelo> buscarConMinimoDeReservas(int cantidad);

    // Listar vuelos con origen específico, ordenados por destino
    @Query("SELECT v FROM Vuelo v WHERE v.origen = ?1 ORDER BY v.destino ASC")
    List<Vuelo> listarPorOrigenOrdenadoPorDestino(String origen);

    // Obtener los destinos únicos de todos los vuelos
    @Query("SELECT DISTINCT v.destino FROM Vuelo v")
    List<String> obtenerDestinosUnicos();

    // Contar cuántos vuelos hay entre dos ciudades
    @Query("SELECT COUNT(v) FROM Vuelo v WHERE v.origen = ?1 AND v.destino = ?2")
    long contarVuelosEntreCiudades(String origen, String destino);
}
