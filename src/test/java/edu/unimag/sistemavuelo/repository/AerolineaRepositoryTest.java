package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.TestcontainersConfiguration;
import edu.unimag.sistemavuelo.entities.Aerolinea;
import edu.unimag.sistemavuelo.entities.Vuelo;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AerolineaRepositoryTest {

    @Autowired
    private AerolineaRepository aerolineaRepository;

    List<Vuelo>  vuelos= new LinkedList<>();;
    Aerolinea aereolinea1;
    Aerolinea aereolinea2;

    @Autowired
    VueloRepository vueloRepository;

    @Autowired
    private EntityManager entityManager; //permite interactuar con la base de datos, siempre usar cundo se ponga @Transactional

    @BeforeEach
    void setUp() {
        aerolineaRepository.deleteAll();
        vueloRepository.deleteAll();

        Vuelo vuelo1 = Vuelo.builder().numeroVuelo(UUID.randomUUID()).origen("Santa Marta").destino("Bogotá").build();
        Vuelo vuelo2 = Vuelo.builder().numeroVuelo(UUID.randomUUID()).origen("Bogotá").destino("Santa Marta").build();
        vuelos.add(vuelo1);
        vuelos.add(vuelo2);
        vueloRepository.saveAll(vuelos);

        aereolinea1 = Aerolinea.builder().nombre("Jetsmart").vuelos(vuelos).build();
        aereolinea2 = Aerolinea.builder().nombre("Wingo Air").build();
        Aerolinea avianca = Aerolinea.builder().nombre("Avianca").build();
        Aerolinea latam = Aerolinea.builder().nombre("LATAM").build();

        aerolineaRepository.saveAll(List.of(aereolinea1, aereolinea2, avianca, latam));
    }

    @Test
    void testFindByNombre() {
        List<Aerolinea> aerolineas = aerolineaRepository.findByNombre("LATAM");
        assertFalse(aerolineas.isEmpty());
    }

    @Test
    void testFindById() {
        Aerolinea aerolinea = aerolineaRepository.findByNombre("Avianca").getFirst();
        Optional<Aerolinea> result = aerolineaRepository.findById(aerolinea.getId());
        assertTrue(result.isPresent(), "No se encontró la aerolínea por ID");
        assertEquals("Avianca", result.get().getNombre());
    }

    @Test
    @Transactional
    void testUpdateAerolineaNombreById() {
        Aerolinea aerolinea = aerolineaRepository.findByNombre("LATAM").getFirst();
        aerolineaRepository.updateAerolineaNombreById(aerolinea.getId(), "LATAM Airlines");

        // Refrescar la entidad desde la base de datos
        entityManager.flush(); // Fuerza que Hibernate sincronice los cambios en la BD.
        entityManager.clear();// Limpia el contexto de persistencia, eliminando la versión en caché de la entidad.

        Optional<Aerolinea> updatedAerolinea = aerolineaRepository.findById(aerolinea.getId());
        assertTrue(updatedAerolinea.isPresent());
        assertEquals("LATAM Airlines", updatedAerolinea.get().getNombre());
    }


    @Test
    @Transactional
    void testDeleteAerolineaById() {
        Aerolinea aerolinea = aerolineaRepository.findByNombre("Avianca").getFirst();
        aerolineaRepository.deleteAerolineaById(aerolinea.getId());

        // Refrescar la entidad desde la base de datos
        entityManager.flush();
        entityManager.clear();

        Optional<Aerolinea> deletedAerolinea = aerolineaRepository.findById(aerolinea.getId());
        assertFalse(deletedAerolinea.isPresent());
    }

    @Test
    @DisplayName("Buscar aerolíneas por nombre que empieza con una letra")
    void testFindByNombreStartingWith() {
        List<Aerolinea> aerolineas = aerolineaRepository.findByNombreStartingWith("J");
        assertFalse(aerolineas.isEmpty(), "No se encontraron aerolíneas que comiencen con 'J'");
        assertEquals("Jetsmart", aerolineas.getFirst().getNombre());
    }

    @Test
    @DisplayName("Contar aerolíneas por nombre")
    void testCountByNombre() {
        // Contar aerolíneas
        long cantidadAntes = aerolineaRepository.countByNombre("Avianca");

        // Crear y guardar una nueva aerolínea con el mismo nombre
        Aerolinea nuevaAvianca = Aerolinea.builder().nombre("Avianca").build();
        aerolineaRepository.save(nuevaAvianca);

        // Contar nuevamente después de agregar la nueva aerolínea
        long cantidadDespues = aerolineaRepository.countByNombre("Avianca");

        // Verificar que la cantidad haya aumentado en 1
        assertEquals(cantidadAntes + 1, cantidadDespues, "El conteo de aerolíneas no se incrementó correctamente");
    }


    @Test
    @DisplayName("Buscar aerolíneas por vuelos con un destino específico")
    void testFindAerolineasByVuelosDestino() {
        List<Aerolinea> aerolineas = aerolineaRepository.findAerolineasByVuelosDestino("Bogotá");
        assertTrue(aerolineas.isEmpty());
    }


    @Test
    @DisplayName("Buscar aerolíneas que contengan una palabra en su nombre")
    void testFindByNombreContaining() {
        List<Aerolinea> resultado = aerolineaRepository.findByNombreContaining("Air");
        assertFalse(resultado.isEmpty(), "No se encontraron aerolíneas que contengan 'Air'");
    }


    @Test
    @DisplayName("Buscar aerolíneas por vuelos desde un origen a un destino")
    void testFindAerolineasByVuelosOrigenAndDestino() {
        List<Aerolinea> aerolineas = aerolineaRepository.findAerolineasByVuelosOrigenAndDestino("Santa Marta", "Bogotá");
        assertTrue(aerolineas.isEmpty());
    }

    @Test
    @DisplayName("Buscar aerolíneas por número de vuelo")
    void testFindAerolineasByVuelosNumeroVuelo() {
        // Crear un vuelo con un UUID específico
        UUID numeroVuelo = UUID.fromString("a1b2c3d4-e5f6-47a1-8901-abcdef123456");
        Vuelo vuelo = Vuelo.builder()
                .origen("Medellín")
                .destino("Cartagena")
                .numeroVuelo(numeroVuelo)
                .build();
        vueloRepository.save(vuelo);

        // Crear una aerolínea
        Aerolinea aerolinea = Aerolinea.builder()
                .nombre("Avianca")
                .build();
        aerolineaRepository.save(aerolinea);

        // Establecer la relación bidireccional
        vuelo.getAerolineas().add(aerolinea);
        aerolinea.getVuelos().add(vuelo);

        // Guardar los cambios
        vueloRepository.save(vuelo);
        aerolineaRepository.save(aerolinea);

        entityManager.flush();
        entityManager.clear();

        // Realizar la consulta
        List<Aerolinea> aerolineas = aerolineaRepository.findAerolineasByVuelosNumeroVuelo(numeroVuelo);
        assertFalse(aerolineas.isEmpty(), "No se encontró aerolínea con el número de vuelo especificado");
        assertEquals("Avianca", aerolineas.getFirst().getNombre());
    }

}