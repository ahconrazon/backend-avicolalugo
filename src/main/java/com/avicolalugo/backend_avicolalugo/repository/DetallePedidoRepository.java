package com.avicolalugo.backend_avicolalugo.repository; // ¡Asegúrate que esta línea sea correcta!

import com.avicolalugo.backend_avicolalugo.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}