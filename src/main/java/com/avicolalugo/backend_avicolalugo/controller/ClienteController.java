package com.avicolalugo.backend_avicolalugo.controller; // ¡Asegúrate que esta línea sea correcta!

import com.avicolalugo.backend_avicolalugo.model.Cliente;
import com.avicolalugo.backend_avicolalugo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> new ResponseEntity<>(cliente, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        cliente.setFechaRegistro(LocalDateTime.now());
        Cliente savedCliente = clienteRepository.save(cliente);
        return new ResponseEntity<>(savedCliente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(clienteDetails.getNombre());
                    cliente.setDireccion(clienteDetails.getDireccion());
                    cliente.setTelefono(clienteDetails.getTelefono());
                    cliente.setEmail(clienteDetails.getEmail());
                    cliente.setTipoCliente(clienteDetails.getTipoCliente());
                    cliente.setSaldoInicial(clienteDetails.getSaldoInicial());
                    Cliente updatedCliente = clienteRepository.save(cliente);
                    return new ResponseEntity<>(updatedCliente, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}