package com.avicolalugo.backend_avicolalugo.model; // ¡Asegúrate que esta línea sea correcta!

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;
    @Column(name = "unidad_medida", nullable = false)
    private String unidadMedida;
    private String descripcion;
}