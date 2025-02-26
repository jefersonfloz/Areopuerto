package edu.unimag.sistemavuelo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pasaportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pasaporte {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "pasaporte")
    private Pasajero pasajero;



}
