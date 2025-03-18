package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.TestcontainersConfiguration;
import edu.unimag.sistemavuelo.entities.Pasajero;
import edu.unimag.sistemavuelo.entities.Pasaporte;
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
class PasaporteRepositoryTest {

    @Autowired
    private PasaporteRepository pasaporteRepository;

    @Autowired
    private PasajeroRepository pasajeroRepository;

    @Autowired
    private EntityManager entityManager; //permite interactuar con la base de datos, siempre usar cundo se ponga @Transactional


    private Pasaporte pasaporte1;
    private Pasaporte pasaporte2;
    private Pasaporte pasaporte3;
    private Pasajero pasajero1;
    private Pasajero pasajero2;

    @BeforeEach
    void setUp() {
        pasaporte1 = Pasaporte.builder().numero("P001").build();
        pasaporte2 = Pasaporte.builder().numero("P002").build();
        pasaporte3 = Pasaporte.builder().numero("P003").build();

        pasaporteRepository.save(pasaporte1);
        pasaporteRepository.save(pasaporte2);
        pasaporteRepository.save(pasaporte3);

        pasajero1 = Pasajero.builder().nombre("Juan").apellido("Perez").nid("123456").pasaporte(pasaporte1).build();
        pasajero2 = Pasajero.builder().nombre("Maria").apellido("Gomez").nid("654321").pasaporte(pasaporte2).build();

        pasajeroRepository.save(pasajero1);
        pasajeroRepository.save(pasajero2);
    }

    @Test
    void testFindById() {
        Optional<Pasaporte> found = pasaporteRepository.findById(pasaporte1.getId());
        assertTrue(found.isPresent());
        assertEquals("P001", found.get().getNumero());
    }

    @Test
    void testFindByIdGreaterThan() {
        List<Pasaporte> pasaportes = pasaporteRepository.findByIdGreaterThan(pasaporte1.getId());
        assertFalse(pasaportes.isEmpty());
    }

    @Test
    void testFindByIdLessThan() {
        List<Pasaporte> pasaportes = pasaporteRepository.findByIdLessThan(pasaporte3.getId());
        assertEquals(2, pasaportes.size());
    }

    @Test
    void testFindByIdBetween() {
        List<Pasaporte> pasaportes = pasaporteRepository.findByIdBetween(pasaporte1.getId(), pasaporte2.getId());
        assertEquals(2, pasaportes.size());
    }

    @Test
    void testFindByIdOrderByIdDesc() {
        List<Pasaporte> pasaportes = pasaporteRepository.findByIdOrderByIdDesc(pasaporte2.getId());
        assertFalse(pasaportes.isEmpty());
        assertEquals(pasaporte2.getId(), pasaportes.getFirst().getId());
    }

    @Test
    public void testListarOrdenadosPorIdAsc() {
        // Obtener los pasaportes ordenados por ID ascendente
        List<Pasaporte> pasaportes = pasaporteRepository.listarOrdenadosPorIdAsc();

        // Comprobar que la lista no está vacía
        assertEquals(3, pasaportes.size());

        // Verificar que los pasaportes están ordenados por ID ascendente
        assertEquals("P001", pasaportes.get(0).getNumero());
        assertEquals("P002", pasaportes.get(1).getNumero());
        assertEquals("P003", pasaportes.get(2).getNumero());
    }

    @Test
    void testObtenerPasaportesSinPasajero() {
        List<Pasaporte> pasaportesSinPasajero = pasaporteRepository.obtenerPasaportesSinPasajero();
        assertEquals(1, pasaportesSinPasajero.size());
        assertEquals("P003", pasaportesSinPasajero.getFirst().getNumero());
    }


    @Test
    void testContarPasaportes() {
        long count = pasaporteRepository.contarPasaportes();
        assertEquals(3, count);
    }

    @Test
    void testObtenerTodosOrdenadosPorIdDesc() {
        List<Pasaporte> pasaportes = pasaporteRepository.obtenerTodosOrdenadosPorIdDesc();
        assertEquals(3, pasaportes.size());
        assertEquals("P003", pasaportes.getFirst().getNumero());
    }

    @Test
    void testUpdatePasaporteNumeroById() {
        pasaporteRepository.updatePasaporteNumeroById(pasaporte1.getId(), "UPDATED123");
        // Refrescar la entidad desde la base de datos
        entityManager.flush();
        entityManager.clear();
        Optional<Pasaporte> updated = pasaporteRepository.findById(pasaporte1.getId());
        assertTrue(updated.isPresent());
        assertEquals("UPDATED123", updated.get().getNumero());
    }

}