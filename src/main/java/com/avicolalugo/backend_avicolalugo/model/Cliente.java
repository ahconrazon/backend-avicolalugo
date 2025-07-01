package com.avicolalugo.backend_avicolalugo.model; // ¡Asegúrate que esta línea sea correcta!

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String tipoCliente;
    private Double saldoInicial;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}