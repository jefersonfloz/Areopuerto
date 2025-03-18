package edu.unimag.sistemavuelo.repository;

import edu.unimag.sistemavuelo.TestcontainersConfiguration;
import edu.unimag.sistemavuelo.entities.Aerolinea;
import edu.unimag.sistemavuelo.entities.Reserva;
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
class VueloRepositoryTest {
    @Autowired
    private VueloRepository vueloRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    private Vuelo vuelo1;
    private Vuelo vuelo2;
    private Vuelo vuelo3;

    @BeforeEach
    public void setUp() {
        // Limpiar la base de datos antes de cada test
        vueloRepository.deleteAll();
        reservaRepository.deleteAll();

        // Crear algunos vuelos
        vuelo1 = Vuelo.builder().numeroVuelo(UUID.randomUUID()).origen("New York").destino("Los Angeles").build();
        vuelo2 = Vuelo.builder().numeroVuelo(UUID.randomUUID()).destino("Barcelona").build();
        vuelo3 = Vuelo.builder().numeroVuelo(UUID.randomUUID()).origen("Buenos Aires").destino("Lima").build();

        // Guardar vuelos en la base de datos
        vueloRepository.save(vuelo1);
        vueloRepository.save(vuelo2);
        vueloRepository.save(vuelo3);
    }

    @Test
    public void testFindByOrigenAndDestino() {
        List<Vuelo> vuelos = vueloRepository.findByOrigenAndDestino("New York", "Los Angeles");
        assertEquals(1, vuelos.size());
        assertEquals("New York", vuelos.get(0).getOrigen());
        assertEquals("Los Angeles", vuelos.get(0).getDestino());
    }

    @Test
    public void testFindByDestinoContaining() {
        List<Vuelo> vuelos = vueloRepository.findByDestinoContaining("Los");
        assertEquals(1, vuelos.size());
        assertEquals("Los Angeles", vuelos.get(0).getDestino());
    }

    @Test
    public void testFindByReservasIsNotEmpty() {
        // Crear una reserva para vuelo1
        Reserva reserva = new Reserva();
        reserva.setVuelo(vuelo1);
        reservaRepository.save(reserva);

        // Buscar vuelos con reservas
        List<Vuelo> vuelosConReservas = vueloRepository.findByReservasIsNotEmpty();
        assertEquals(1, vuelosConReservas.size());
        assertEquals(vuelo1.getNumeroVuelo(), vuelosConReservas.get(0).getNumeroVuelo());
    }

    @Test
    public void testFindByReservasIsEmpty() {
        // Asegurarse de que vuelo1 tiene reservas asociadas
        Reserva reserva = new Reserva();
        reserva.setVuelo(vuelo1);
        reservaRepository.save(reserva); // Guardar una reserva para vuelo1

        // Buscar vuelos sin reservas
        List<Vuelo> vuelosSinReservas = vueloRepository.findByReservasIsEmpty();

        // Comprobar que el vuelo1 no está en los resultados, ya que tiene una reserva
        assertEquals(2, vuelosSinReservas.size()); // Deberían quedar solo vuelo2 y vuelo3, ya que vuelo1 tiene una reserva
        assertTrue(vuelosSinReservas.contains(vuelo2));
        assertTrue(vuelosSinReservas.contains(vuelo3));
    }


    @Test
    public void testBuscarPorNumeroVuelo() {
        Optional<Vuelo> vueloEncontrado = vueloRepository.buscarPorNumeroVuelo(vuelo1.getNumeroVuelo());
        assertTrue(vueloEncontrado.isPresent(), "aqui"+vuelo1.getNumeroVuelo());
        assertEquals(vuelo1.getNumeroVuelo(), vueloEncontrado.get().getNumeroVuelo(),"fakkka");
    }

    @Test
    public void testBuscarConMinimoDeReservas() {
        // Crear una reserva para vuelo1
        Reserva reserva = new Reserva();
        reserva.setVuelo(vuelo1);
        reservaRepository.save(reserva);

        // Buscar vuelos con al menos 1 reserva
        List<Vuelo> vuelosConReservas = vueloRepository.buscarConMinimoDeReservas(1);
        assertEquals(1, vuelosConReservas.size());
        assertEquals(vuelo1.getNumeroVuelo(), vuelosConReservas.get(0).getNumeroVuelo());
    }

    @Test
    public void testListarPorOrigenOrdenadoPorDestino() {
        List<Vuelo> vuelos = vueloRepository.listarPorOrigenOrdenadoPorDestino("New York");
        assertEquals(1, vuelos.size());
        assertEquals("Los Angeles", vuelos.get(0).getDestino());
    }

    @Test
    public void testObtenerDestinosUnicos() {
        List<String> destinosUnicos = vueloRepository.obtenerDestinosUnicos();
        assertTrue(destinosUnicos.contains("Los Angeles"));
        assertTrue(destinosUnicos.contains("Barcelona"));
        assertTrue(destinosUnicos.contains("Lima"));
    }

    @Test
    public void testContarVuelosEntreCiudades() {
        long cantidad = vueloRepository.contarVuelosEntreCiudades("New York", "Los Angeles");
        assertEquals(1, cantidad);
    }

    @Test
    public void testFindByOrigenAndNumeroVuelo() {
        // Buscar el vuelo con origen "New York" y el número de vuelo de vuelo1
        List<Vuelo> vuelosEncontrados = vueloRepository.findByOrigenAndNumeroVuelo("New York", vuelo1.getNumeroVuelo());

        // Comprobar que solo se encontró vuelo1
        assertEquals(1, vuelosEncontrados.size());
        assertTrue(vuelosEncontrados.contains(vuelo1));
        assertFalse(vuelosEncontrados.contains(vuelo2));
        assertFalse(vuelosEncontrados.contains(vuelo3));
    }



}