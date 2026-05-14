package cl.techstore.api.techstore_api.repository;
import cl.techstore.api.techstore_api.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
