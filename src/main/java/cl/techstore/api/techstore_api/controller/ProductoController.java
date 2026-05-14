package cl.techstore.api.techstore_api.controller;

import cl.techstore.api.techstore_api.model.Producto;
import cl.techstore.api.techstore_api.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listar(){
        return productoService.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> modificar(@PathVariable Long id, @RequestBody Producto producto){
        return ResponseEntity.ok(productoService.modificar(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Producto> eliminar(@PathVariable Long id){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
