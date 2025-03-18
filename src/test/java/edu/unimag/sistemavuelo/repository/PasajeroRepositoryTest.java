package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.TestcontainersConfiguration;
import edu.unimag.sistemavuelo.entities.Pasajero;
import edu.unimag.sistemavuelo.entities.Pasaporte;
import edu.unimag.sistemavuelo.entities.Reserva;
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
class PasajeroRepositoryTest {

    @Autowired
    private PasajeroRepository pasajeroRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EntityManager entityManager; //permite interactuar con la base de datos, siempre usar cundo se ponga @Transactional

    private Pasajero pasajero;
    private final UUID codigoReserva = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Pasaporte pasaporte = Pasaporte.builder().numero("1111").build();
        pasajero = Pasajero.builder()
                .nombre("Juan")
                .apellido("Perez")
                .nid("123456")
                .pasaporte(pasaporte)
                .build();
        pasajeroRepository.save(pasajero);

        Reserva reserva = Reserva.builder().codigoReserva(codigoReserva).pasajero(pasajero).build();

        reservaRepository.save(reserva);
    }

    @Test
    void testFindByNid() {
        List<Pasajero> pasajeros = pasajeroRepository.findByNid("123456");
        assertFalse(pasajeros.isEmpty());
        assertEquals("Juan", pasajeros.getFirst().getNombre());
    }

    @Test
    void testFindByNombre() {
        List<Pasajero> pasajeros = pasajeroRepository.findByNombre("Juan");
        assertFalse(pasajeros.isEmpty());
    }

    @Test
    void testFindByNombreAndPasaporteId() {
        Optional<Pasajero> encontrado = pasajeroRepository.findByNombreAndPasaporteId("Juan", pasajero.getPasaporte().getId());
        assertTrue(encontrado.isPresent());
    }

    @Test
    void testUpdatePasajeroUid() {
        pasajeroRepository.updatePasajeroUid("123456", "Carlos", "Gomez");
        // Refrescar la entidad desde la base de datos
        entityManager.flush();
        entityManager.clear();
        List<Pasajero> pasajeros = pasajeroRepository.findByNid("123456");
        assertEquals("Carlos", pasajeros.getFirst().getNombre());
    }


    @Test
    void testUpdateApellidoPasajeroByNombreAndUid() {
        pasajeroRepository.updateApellidoPasajeroByNombreAndUid("Juan", pasajero.getId(), "Lopez");
        // Refrescar la entidad desde la base de datos
        entityManager.flush();
        entityManager.clear();
        List<Pasajero> pasajeros = pasajeroRepository.findByNombre("Juan");
        assertEquals("Lopez", pasajeros.getFirst().getApellido());
    }

    @Test
    void testFindPasajerosByCodigoReserva() {
        List<Pasajero> pasajeros = pasajeroRepository.findPasajerosByCodigoReserva(codigoReserva);
        assertFalse(pasajeros.isEmpty());
        assertEquals("Juan", pasajeros.getFirst().getNombre());
    }

    @Test
    @DisplayName("Debe contar cuántos pasajeros tienen un nombre específico")
    void countByNombreTest() {
        long cantidadAntes = pasajeroRepository.countByNombre("Juan");
        Pasaporte pasaporte = Pasaporte.builder().numero("2222").build();
        Pasajero nuevoPasajero = Pasajero.builder()
                .nombre("Juan")
                .apellido("Pretux")
                .nid("222222")
                .pasaporte(pasaporte)
                .build();
        pasajeroRepository.save(nuevoPasajero);
        long cantidadDespues = pasajeroRepository.countByNombre("Juan");
        assertEquals(cantidadAntes + 1, cantidadDespues, "El conteo de aerolíneas no se incrementó correctamente");
    }


    @Test
    void testFindByReservasIsEmpty() {
        // Crear y guardar pasajeros sin reservas
        Pasaporte pasaporte1 = Pasaporte.builder().numero("3333").build();
        Pasajero pasajero1 = Pasajero.builder()
                .nombre("Carlos")
                .apellido("Gomez")
                .nid("987654")
                .pasaporte(pasaporte1) // Asumiendo un constructor adecuado
                .reservas(Collections.emptyList())
                .build();

        Pasaporte pasaporte2 = Pasaporte.builder().numero("44444").build();
        Pasajero pasajero2 = Pasajero.builder()
                .nombre("Ana")
                .apellido("Perez")
                .nid("567890")
                .pasaporte(pasaporte2)
                .reservas(Collections.emptyList())
                .build();

        pasajeroRepository.save(pasajero1);
        pasajeroRepository.save(pasajero2);

        // Ejecutar la consulta
        List<Pasajero> pasajerosSinReservas = pasajeroRepository.findByReservasIsEmpty();

        // Validar resultados
        assertFalse(pasajerosSinReservas.isEmpty());
        assertTrue(pasajerosSinReservas.contains(pasajero1));
        assertTrue(pasajerosSinReservas.contains(pasajero2));
    }


    @Test
    void testDeleteReservaByNidAndCodigoReserva() {
        pasajeroRepository.deleteReservaByNidAndCodigoReserva("123456", codigoReserva);
        Optional<Reserva> deletedReserva = reservaRepository.findByCodigoReserva(codigoReserva);
        assertTrue(deletedReserva.isEmpty());
    }

    @Test
    void testFindPasajeroByNidAndCodigoReserva() {
        Optional<Pasajero> encontrado = pasajeroRepository.findPasajeroByNidAndCodigoReserva("123456", codigoReserva);
        assertTrue(encontrado.isPresent());
        assertEquals("Juan", encontrado.get().getNombre());
    }
}