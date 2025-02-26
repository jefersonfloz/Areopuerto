package edu.unimag.sistemavuelo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID codigoReserva= UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pasajero_id")
    private Pasajero pasajero;

  @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vuelos_id")
    private Vuelo vuelo;

}
