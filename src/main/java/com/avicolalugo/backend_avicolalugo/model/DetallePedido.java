package com.avicolalugo.backend_avicolalugo.model; // ¡Asegúrate que esta línea sea correcta!

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalle_pedido")
@Data
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad_unidades")
    private Integer cantidadUnidades;
    @Column(name = "peso_neto_kg")
    private Double pesoNetoKg;
    @Column(name = "peso_bruto_kg")
    private Double pesoBrutoKg;
    @Column(name = "precio_unitario_venta")
    private Double precioUnitarioVenta;
    @Column(name = "importe_linea")
    private Double importeLinea;
}