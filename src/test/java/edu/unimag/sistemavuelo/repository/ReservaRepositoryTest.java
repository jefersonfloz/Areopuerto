package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.TestcontainersConfiguration;
import edu.unimag.sistemavuelo.entities.Pasajero;
import edu.unimag.sistemavuelo.entities.Pasaporte;
import edu.unimag.sistemavuelo.entities.Reserva;
import edu.unimag.sistemavuelo.entities.Vuelo;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
class ReservaRepositoryTest {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PasajeroRepository pasajeroRepository;

    @Autowired
    private VueloRepository vueloRepository;

    @Autowired
    EntityManager entityManager;

    private Reserva reserva1;
    private Reserva reserva2;
    private Pasajero pasajero;
    private Vuelo vuelo;

    @BeforeEach
    public void setUp() {
        // Limpiar la base de datos antes de cada test
        reservaRepository.deleteAll();
        pasajeroRepository.deleteAll();
        vueloRepository.deleteAll();

        // Crear un pasajero y un vuelo
        Pasaporte pasaporte = Pasaporte.builder().numero("123456").build();
        pasajero= Pasajero.builder()
                .nombre("Juan")
                .apellido("Perez")
                .nid("222222")
                .pasaporte(pasaporte)
                .build();
        vuelo = Vuelo.builder().numeroVuelo(UUID.randomUUID()).origen("New York").destino("Los Angeles").build();

        // Guardar pasajero y vuelo en la base de datos
        pasajeroRepository.save(pasajero);
        vueloRepository.save(vuelo);

        // Crear y guardar las reservas
        reserva1 = Reserva.builder().codigoReserva(UUID.randomUUID()).pasajero(pasajero).vuelo(vuelo).build();
        reserva2 = Reserva.builder().codigoReserva(UUID.randomUUID()).pasajero(pasajero).vuelo(vuelo).build();
        reservaRepository.save(reserva1);
        reservaRepository.save(reserva2);
    }

    @Test
    public void testFindByCodigoReserva() {
        // Buscar reserva por codigoReserva
        Reserva reservaEncontrada = reservaRepository.findByCodigoReserva(reserva1.getCodigoReserva()).orElse(null);

        // Verificar que la reserva encontrada tiene el mismo código
        assertNotNull(reservaEncontrada);
        assertEquals(reserva1.getCodigoReserva(), reservaEncontrada.getCodigoReserva());
    }

    @Test
    public void testDeleteReserva() {
        // Verificar que la reserva existe antes de eliminarla
        assertNotNull(reservaRepository.findByCodigoReserva(reserva1.getCodigoReserva()).orElse(null));
        // Eliminar la reserva
        reservaRepository.delete(reserva1);
        // Verificar que la reserva ha sido eliminada
        assertNull(reservaRepository.findByCodigoReserva(reserva1.getCodigoReserva()).orElse(null));
    }


    @Test
    public void testFindByPasajero() {
        // Buscar todas las reservas de un pasajero
        List<Reserva> reservas = reservaRepository.findByPasajero(pasajero);

        // Verificar que se encontraron las reservas
        assertEquals(2, reservas.size());
        assertTrue(reservas.contains(reserva1));
        assertTrue(reservas.contains(reserva2));
    }

    @Test
    public void testFindByVuelo() {
        // Buscar todas las reservas de un vuelo
        List<Reserva> reservas = reservaRepository.findByVuelo(vuelo);

        // Verificar que se encontraron las reservas
        assertEquals(2, reservas.size());
        assertTrue(reservas.contains(reserva1));
        assertTrue(reservas.contains(reserva2));
    }

    @Test
    public void testFindAllPaged() {
        // Paginación de reservas (por ejemplo, obtener 1 por página)
        Page<Reserva> page = reservaRepository.findAll(PageRequest.of(0, 1));

        // Verificar que la página tiene solo 1 reserva
        assertEquals(1, page.getContent().size());
    }

    @Test
    public void testCountReservasByPasajero() {
        // Verificar que el pasajero tiene las reservas correctas
        long count = reservaRepository.countReservasByPasajero(pasajero);
        // Verificar que el número de reservas para este pasajero es 2
        assertEquals(2, count);
    }

    @Test
    public void testUpdateReserva() {
        // Crear un nuevo vuelo y actualizar la reserva
        Vuelo vueloNuevo = Vuelo.builder()
                .numeroVuelo(UUID.randomUUID()) // Genera un UUID único
                .origen("Madrid")
                .destino("Paris")
                .build();
        vueloRepository.save(vueloNuevo);

        // Actualizar reserva con nuevo vuelo
        reservaRepository.updateReserva(pasajero, vueloNuevo, reserva1.getCodigoReserva());
        entityManager.flush();
        entityManager.clear();

        // Verificar que la reserva se ha actualizado con el nuevo vuelo
        Reserva reservaActualizada = reservaRepository.findByCodigoReserva(reserva1.getCodigoReserva()).orElse(null);

        assertNotNull(reservaActualizada);

        // Verificar que la reserva tiene el nuevo vuelo comparando los atributos individuales
        assertEquals(vueloNuevo.getNumeroVuelo(), reservaActualizada.getVuelo().getNumeroVuelo());
        assertEquals(vueloNuevo.getOrigen(), reservaActualizada.getVuelo().getOrigen());
        assertEquals(vueloNuevo.getDestino(), reservaActualizada.getVuelo().getDestino());

    }


    @Test
    public void testUpdatePasajero() {
        // Crear un nuevo pasajero y actualizar la reserva
        Pasaporte pasaporte = Pasaporte.builder().numero("2222").build();
        Pasajero pasajeroNuevo = Pasajero.builder()
                .nombre("Juan")
                .apellido("Perez")
                .nid("222222")
                .pasaporte(pasaporte)
                .build();
        pasajeroRepository.save(pasajeroNuevo);

        // Actualizar reserva con nuevo pasajero
        reservaRepository.updatePasajero(pasajeroNuevo, reserva1.getCodigoReserva());
        entityManager.flush();
        entityManager.clear();
        // Verificar que la reserva se ha actualizado con el nuevo pasajero
        Reserva reservaActualizada = reservaRepository.findByCodigoReserva(reserva1.getCodigoReserva()).orElse(null);
        // Comparar los atributos de Pasajero
        assertEquals(pasajeroNuevo.getNombre(), reservaActualizada.getPasajero().getNombre());
        assertEquals(pasajeroNuevo.getApellido(), reservaActualizada.getPasajero().getApellido());
        assertEquals(pasajeroNuevo.getNid(), reservaActualizada.getPasajero().getNid());

    }

    @Test
    public void testUpdateVuelo() {
        // Crear un nuevo vuelo y actualizar la reserva
        Vuelo vueloNuevo = Vuelo.builder()
                .numeroVuelo(UUID.randomUUID()) // Genera un UUID único
                .origen("Tokyo")
                .destino("Los Angeles")
                .build();
        vueloRepository.save(vueloNuevo);

        // Actualizar reserva con el nuevo vuelo
        reservaRepository.updateVuelo(vueloNuevo, reserva1.getCodigoReserva());
        entityManager.flush();
        entityManager.clear();

        // Verificar que la reserva se ha actualizado con el nuevo vuelo
        Reserva reservaActualizada = reservaRepository.findByCodigoReserva(reserva1.getCodigoReserva()).orElse(null);
        assertNotNull(reservaActualizada);

        // Comparar solo los atributos de vuelo
        assertEquals(vueloNuevo.getNumeroVuelo(), reservaActualizada.getVuelo().getNumeroVuelo());
        assertEquals(vueloNuevo.getOrigen(), reservaActualizada.getVuelo().getOrigen());
        assertEquals(vueloNuevo.getDestino(), reservaActualizada.getVuelo().getDestino());
    }

    @Test
    public void testDeleteReservasByVuelo() {
        // Verificar que las reservas han sido creadas
        assertEquals(2, reservaRepository.findByVuelo(vuelo).size());
        // Eliminar las reservas asociadas al vuelo
        reservaRepository.deleteReservasbyVuelo(vuelo);
        // Verificar que las reservas han sido eliminadas
        assertEquals(0, reservaRepository.findByVuelo(vuelo).size());
    }

}