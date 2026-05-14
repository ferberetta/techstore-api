package cl.techstore.api.techstore_api.service;
import cl.techstore.api.techstore_api.model.Producto;
import cl.techstore.api.techstore_api.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    public ProductoRepository productoRepository;

    public List<Producto> listarTodos(){
        return productoRepository.findAll();
    }

    public Producto crear(Producto producto){
        return productoRepository.save(producto);
    }

    public Producto modificar(Long id, Producto detalle){
        Producto producto = productoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Producto no encontrado"));
        producto.setNombre(detalle.getNombre());
        producto.setCategoria(detalle.getCategoria());
        producto.setPrecio(detalle.getPrecio());
        producto.setStock(detalle.getStock());
        producto.setDescripcion(detalle.getDescripcion());

        return productoRepository.save(producto);

    }

    public void eliminar(Long id){
        Producto producto = productoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);

    }
}
