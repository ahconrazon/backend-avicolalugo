// Define el paquete al que pertenece esta clase.
// ¡Asegúrate que esta línea sea correcta y coincida con la estructura de tu proyecto en NetBeans!
package com.avicolalugo.backend_avicolalugo.controller;

// Importaciones necesarias para las clases y anotaciones de Spring y Java
import com.avicolalugo.backend_avicolalugo.model.Pedido; // Importa el modelo Pedido
import com.avicolalugo.backend_avicolalugo.model.Cliente; // Importa el modelo Cliente
import com.avicolalugo.backend_avicolalugo.repository.PedidoRepository; // Importa el repositorio de Pedido
import com.avicolalugo.backend_avicolalugo.repository.ClienteRepository; // Importa el repositorio de Cliente
import org.springframework.beans.factory.annotation.Autowired; // Para la inyección de dependencias
import org.springframework.http.HttpStatus; // Para los códigos de estado HTTP (ej. 200 OK, 201 Created, 404 Not Found)
import org.springframework.http.ResponseEntity; // Para construir respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones de Spring Web (RestController, RequestMapping, GetMapping, PostMapping, PutMapping, DeleteMapping, RequestBody, PathVariable, CrossOrigin)

import java.time.LocalDate; // Para manejar solo la fecha (sin hora)
import java.util.List; // Para manejar listas de objetos
import java.util.Optional; // Para manejar valores que pueden estar presentes o ausentes

// @RestController: Combina @Controller y @ResponseBody. Indica que esta clase es un controlador REST
// y que los métodos devuelven directamente los datos (JSON/XML) en el cuerpo de la respuesta.
@RestController
// @RequestMapping: Define la URL base para todos los endpoints en este controlador.
// Todas las peticiones a /api/pedidos serán manejadas por esta clase.
@RequestMapping("/api/pedidos")
// @CrossOrigin: Permite que solicitudes de otros dominios (como tu aplicación móvil) accedan a esta API.
// "*" significa que permite cualquier origen. En producción, es mejor especificar dominios específicos.
@CrossOrigin(origins = "*")
public class PedidoController {

    // @Autowired: Inyecta automáticamente una instancia de PedidoRepository.
    // Spring se encarga de crear y gestionar esta instancia.
    @Autowired
    private PedidoRepository pedidoRepository;

    // @Autowired: Inyecta automáticamente una instancia de ClienteRepository.
    // Necesario para buscar clientes por su ID al crear o actualizar pedidos.
    @Autowired
    private ClienteRepository clienteRepository;

    // @GetMapping: Mapea las peticiones HTTP GET a este método.
    // URL: GET /api/pedidos
    // Devuelve una lista de todos los pedidos.
    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll(); // Usa el repositorio para obtener todos los pedidos de la BD.
    }

    // @GetMapping("/{id}": Mapea las peticiones HTTP GET con un ID en la URL.
    // URL: GET /api/pedidos/{id} (ej. /api/pedidos/1)
    // @PathVariable: Extrae el ID de la URL y lo pasa como parámetro al método.
    // Devuelve un pedido específico por su ID.
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        // Busca el pedido por ID. Optional se usa porque el pedido podría no existir.
        return pedidoRepository.findById(id)
                // Si el pedido existe, devuelve una respuesta HTTP 200 OK con el pedido en el cuerpo.
                .map(pedido -> new ResponseEntity<>(pedido, HttpStatus.OK))
                // Si el pedido no se encuentra, devuelve una respuesta HTTP 404 Not Found.
                // CORRECCIÓN FINAL: Especificamos explícitamente el tipo <Pedido> para ResponseEntity.
                .orElse(new ResponseEntity<Pedido>(HttpStatus.NOT_FOUND)); // <-- ¡CORRECCIÓN AQUÍ!
    }

    // @PostMapping: Mapea las peticiones HTTP POST a este método.
    // URL: POST /api/pedidos
    // @RequestBody: Convierte el cuerpo de la petición HTTP (JSON) en un objeto Pedido.
    // Crea un nuevo pedido en la base de datos.
    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        // Validación: Asegura que el cliente asociado al pedido no sea nulo y tenga un ID.
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            // Si falta el ID del cliente, devuelve un error 400 Bad Request.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Busca el cliente en la base de datos para asegurarse de que existe.
        Optional<Cliente> clienteOptional = clienteRepository.findById(pedido.getCliente().getId());
        if (clienteOptional.isEmpty()) {
            // Si el cliente no se encuentra, devuelve un error 404 Not Found.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Asigna el objeto Cliente completo al pedido (importante para la relación JPA).
        pedido.setCliente(clienteOptional.get());
        // Establece la fecha del pedido a la fecha actual del sistema.
        pedido.setFechaPedido(LocalDate.now());

        // Guarda el nuevo pedido en la base de datos.
        Pedido savedPedido = pedidoRepository.save(pedido);
        // Devuelve una respuesta HTTP 201 Created con el pedido guardado.
        return new ResponseEntity<>(savedPedido, HttpStatus.CREATED);
    }

    // @PutMapping("/{id}": Mapea las peticiones HTTP PUT con un ID en la URL.
    // URL: PUT /api/pedidos/{id}
    // Actualiza un pedido existente en la base de datos.
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Long id, @RequestBody Pedido pedidoDetails) {
        // Busca el pedido existente por su ID.
        return (ResponseEntity<Pedido>) pedidoRepository.findById(id)
                // Si el pedido existe, procede a actualizarlo.
                .map(pedido -> {
                    // Actualiza los campos del pedido con los nuevos detalles.
                    // Usa un operador ternario para no sobrescribir la fecha si no se proporciona en los detalles.
                    pedido.setFechaPedido(pedidoDetails.getFechaPedido() != null ? pedidoDetails.getFechaPedido() : pedido.getFechaPedido());
                    pedido.setTipoOperacion(pedidoDetails.getTipoOperacion());
                    pedido.setTotalImportePedido(pedidoDetails.getTotalImportePedido());
                    pedido.setACuenta(pedidoDetails.getACuenta());
                    pedido.setSaldoPendiente(pedidoDetails.getSaldoPendiente());
                    pedido.setObservaciones(pedidoDetails.getObservaciones());

                    // Si los detalles incluyen un cliente actualizado, verifica que ese cliente exista.
                    if (pedidoDetails.getCliente() != null && pedidoDetails.getCliente().getId() != null) {
                        Optional<Cliente> clienteOptional = clienteRepository.findById(pedidoDetails.getCliente().getId());
                        if (clienteOptional.isPresent()) {
                            pedido.setCliente(clienteOptional.get()); // Asigna el nuevo cliente si existe.
                        } else {
                            // Si el nuevo cliente no se encuentra, devuelve un error 404 Not Found.
                            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                        }
                    }

                    // Guarda el pedido actualizado en la base de datos.
                    Pedido updatedPedido = pedidoRepository.save(pedido);
                    // Devuelve una respuesta HTTP 200 OK con el pedido actualizado.
                    return new ResponseEntity<>(updatedPedido, HttpStatus.OK);
                })
                // Si el pedido no se encuentra, devuelve un error 404 Not Found.
                // CORRECCIÓN FINAL: Especificamos explícitamente el tipo <Pedido> para ResponseEntity.
                .orElse(new ResponseEntity<Pedido>(HttpStatus.NOT_FOUND)); // <-- ¡CORRECCIÓN AQUÍ!
    }

    // @DeleteMapping("/{id}": Mapea las peticiones HTTP DELETE con un ID en la URL.
    // URL: DELETE /api/pedidos/{id}
    // Elimina un pedido de la base de datos por su ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePedido(@PathVariable Long id) {
        pedidoRepository.deleteById(id); // Elimina el pedido.
        // Devuelve una respuesta HTTP 204 No Content para indicar que la eliminación fue exitosa.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
