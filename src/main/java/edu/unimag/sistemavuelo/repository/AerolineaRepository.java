package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.entities.Aerolinea;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AerolineaRepository extends JpaRepository<Aerolinea, Long> {

    //buscar una aerolinea por su nombre
    List<Aerolinea> findByNombre(String nombre);

    //buscar una aerolinea por su id
    Optional<Aerolinea> findById(Long id);

    // buscar aerolíneas cuyo nombre contenga la palabra dada
    List<Aerolinea> findByNombreContaining(String nombre);

    //obener las errolineas que inicien por una letra su nombre
    List<Aerolinea> findByNombreStartingWith(String letra);

    // contar las aerolíneas por nombre
    long countByNombre(String nombre);


    // aerolineas que hayan hecho un vuelo a un destino x
    @Query("SELECT a FROM Aerolinea a JOIN a.vuelos v WHERE v.destino = :destino")
    List<Aerolinea> findAerolineasByVuelosDestino(String destino);

    //obtener las aerolineas que hayan hecho un vuelo desde un origen x y a un destino y
    @Query("SELECT a FROM Aerolinea a JOIN a.vuelos v WHERE v.origen = :origen AND v.destino = :destino")
    List<Aerolinea> findAerolineasByVuelosOrigenAndDestino(String origen, String destino);

    //obtener las aerolineas que hayan hecho un vuelo con un numero de vuelo x
    @Query("SELECT a FROM Aerolinea a JOIN a.vuelos v WHERE v.numeroVuelo = :numeroVuelo")
    List<Aerolinea> findAerolineasByVuelosNumeroVuelo(UUID numeroVuelo);

    //cambiar el nombre de una aerolinea dado su id
    @Modifying
    @Transactional
    @Query("UPDATE Aerolinea a SET a.nombre = :nombre WHERE a.id = :id")
    void updateAerolineaNombreById(Long id, String nombre);

    //eliminar una aerolinea dado su id
    @Modifying
    @Transactional
    @Query("DELETE FROM Aerolinea a WHERE a.id = :id")
    void deleteAerolineaById(Long id);

}