package cl.techstore.api.techstore_api.controller;

import cl.techstore.api.techstore_api.model.Producto;
import cl.techstore.api.techstore_api.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private SqsClient sqsClient;

    private static final String QUEUE_NAME = "techstore-audit-queue";

    @GetMapping
    public List<Producto> listar(){
        return productoService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto){
        Producto nuevoProducto = productoService.crear(producto);
        enviarAuditoria("CREAR", nuevoProducto.getId(), nuevoProducto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> modificar(@PathVariable Long id, @RequestBody Producto producto){
        Producto productoModificado = productoService.modificar(id, producto);
        enviarAuditoria("MODIFICAR", id, productoModificado.getNombre());
        return ResponseEntity.ok(productoModificado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Producto> eliminar(@PathVariable Long id){
        String nombreProducto = "N/A";
        try {
            nombreProducto = productoService.listarTodos().stream()
                    .filter(p -> p.getId().equals(id))
                    .map(Producto::getNombre)
                    .findFirst().orElse("N/A");
            } catch (Exception e) {
        }
        productoService.eliminar(id);
        enviarAuditoria("ELIMINAR", id, nombreProducto);
        return ResponseEntity.noContent().build();
    }

    private void enviarAuditoria(String accion, Long productoId, String nombreProducto) {
        try {
            String usuarioEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            String fechaIso = Instant.now().toString();
            String jsonMensaje = String.format(
                    "{\"accion\": \"%s\", \"productold\": %d, \"nombre\": \"%s\", \"usuario\": \"%s\", \"fecha\": \"%s\"}",
                    accion, productoId, nombreProducto, usuarioEmail, fechaIso
            );
            String queueUrl = sqsClient.getQueueUrl(builder -> builder.queueName(QUEUE_NAME)).queueUrl();
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(jsonMensaje)
                    .build();
            sqsClient.sendMessage(sendMsgRequest);
            System.out.println("[SQS Producer] Mensaje de auditoría enviado con éxito: " + jsonMensaje);
        } catch (Exception e) {
            System.err.println("[SQS Error] No se pudo enviar el mensaje a SQS: " + e.getMessage());
        }
    }
}
