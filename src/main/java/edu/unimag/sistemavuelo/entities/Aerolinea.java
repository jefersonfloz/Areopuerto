package edu.unimag.sistemavuelo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "aerolineas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Aerolinea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    @ManyToMany(mappedBy = "aerolineas")
    private List<Vuelo> vuelos= new LinkedList<>();
}
