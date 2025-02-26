package edu.unimag.sistemavuelo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pasajeros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Pasajero {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String nombre;
    private String nid;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "pasaportes_id", referencedColumnName = "id")
    private Pasaporte pasaporte;


    @OneToMany(mappedBy = "pasajero", fetch = FetchType.EAGER)
    private List<Reserva> reservas;

}
