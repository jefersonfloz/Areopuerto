package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.entities.Aerolinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AerolineaRepository extends JpaRepository<Aerolinea, Long> {
    // Query Methods
    List<Aerolinea> findByNombre(String nombre);

    List<Aerolinea> findByNombreContaining(String nombre);

    List<Aerolinea> findByNombreOrderByNombreDesc(String nombre);

    List<Aerolinea> findByNombreEndingWith(String nombre); //

    List<Aerolinea> findByNombreIgnoreCase(String nombre); //


    //JPQL
    @Query("SELECT a FROM Aerolinea a WHERE a.nombre <> ?1")
    List<Aerolinea> buscarPorNombreDiferente(String nombre);


    @Query("SELECT a FROM Aerolinea a WHERE a.nombre LIKE ?1%")
    List<Aerolinea> buscarPorNombreQueEmpieceCon(String prefijo);


    @Query("SELECT a FROM Aerolinea a WHERE a.nombre LIKE %?1")
    List<Aerolinea> buscarPorNombreQueTermineCon(String sufijo);

    @Query("SELECT a FROM Aerolinea a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Aerolinea> buscarPorNombreQueContengaIgnorandoMayusculas(String palabraClave);

    @Query("SELECT a FROM Aerolinea a WHERE LENGTH(a.nombre) >= ?1")
    List<Aerolinea> buscarPorNombreConLongitudMinima(int longitudMinima);


}
