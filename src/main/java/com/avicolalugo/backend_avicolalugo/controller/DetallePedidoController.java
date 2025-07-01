package com.avicolalugo.backend_avicolalugo.controller; // ¡Asegúrate que esta línea sea correcta!

import com.avicolalugo.backend_avicolalugo.model.DetallePedido;
import com.avicolalugo.backend_avicolalugo.model.Pedido;
import com.avicolalugo.backend_avicolalugo.model.Producto;
import com.avicolalugo.backend_avicolalugo.repository.DetallePedidoRepository;
import com.avicolalugo.backend_avicolalugo.repository.PedidoRepository;
import com.avicolalugo.backend_avicolalugo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalle-pedidos")
@CrossOrigin(origins = "*")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<DetallePedido> getAllDetallePedidos() {
        return detallePedidoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> getDetallePedidoById(@PathVariable Long id) {
        return detallePedidoRepository.findById(id)
                .map(detalle -> new ResponseEntity<>(detalle, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<DetallePedido> createDetallePedido(@RequestBody DetallePedido detallePedido) {
        if (detallePedido.getPedido() == null || detallePedido.getPedido().getId() == null ||
            detallePedido.getProducto() == null || detallePedido.getProducto().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Pedido> pedidoOptional = pedidoRepository.findById(detallePedido.getPedido().getId());
        Optional<Producto> productoOptional = productoRepository.findById(detallePedido.getProducto().getId());

        if (pedidoOptional.isEmpty() || productoOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        detallePedido.setPedido(pedidoOptional.get());
        detallePedido.setProducto(productoOptional.get());

        DetallePedido savedDetalle = detallePedidoRepository.save(detallePedido);
        return new ResponseEntity<>(savedDetalle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedido> updateDetallePedido(@PathVariable Long id, @RequestBody DetallePedido detalleDetails) {
        return detallePedidoRepository.findById(id)
                .map(detalle -> {
                    detalle.setCantidadUnidades(detalleDetails.getCantidadUnidades());
                    detalle.setPesoNetoKg(detalleDetails.getPesoNetoKg());
                    detalle.setPesoBrutoKg(detalleDetails.getPesoBrutoKg());
                    detalle.setPrecioUnitarioVenta(detalleDetails.getPrecioUnitarioVenta());
                    detalle.setImporteLinea(detalleDetails.getImporteLinea());

                    if (detalleDetails.getPedido() != null && detalleDetails.getPedido().getId() != null) {
                        Optional<Pedido> pedidoOptional = pedidoRepository.findById(detalleDetails.getPedido().getId());
                        pedidoOptional.ifPresent(detalle::setPedido);
                    }
                    if (detalleDetails.getProducto() != null && detalleDetails.getProducto().getId() != null) {
                        Optional<Producto> productoOptional = productoRepository.findById(detalleDetails.getProducto().getId());
                        productoOptional.ifPresent(detalle::setProducto);
                    }

                    DetallePedido updatedDetalle = detallePedidoRepository.save(detalle);
                    return new ResponseEntity<>(updatedDetalle, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDetallePedido(@PathVariable Long id) {
        detallePedidoRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}