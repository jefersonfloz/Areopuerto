package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.entities.Aerolinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AerolineaRepository extends JpaRepository<Aerolinea, Long> {
    // Query Methods
    List<Aerolinea> findByNombreAerolinea(String nombre);

    List<Aerolinea> findByNombreAerolineaContaining(String nombre);

    List<Aerolinea> findByNombreAerolineaOrderByNombreAerolineaDesc(String nombre);

    List<Aerolinea> findByNombreAerolineaEndingWith(String nombre); //

    List<Aerolinea> findByNombreAerolineaIgnoreCase(String nombre); //


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
