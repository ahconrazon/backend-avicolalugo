package com.avicolalugo.backend_avicolalugo.model; // ¡Asegúrate que esta línea sea correcta!

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;
    @Column(name = "tipo_operacion")
    private String tipoOperacion;
    @Column(name = "total_importe_pedido")
    private Double totalImportePedido;
    @Column(name = "a_cuenta")
    private Double aCuenta;
    @Column(name = "saldo_pendiente")
    private Double saldoPendiente;
    private String observaciones;
}