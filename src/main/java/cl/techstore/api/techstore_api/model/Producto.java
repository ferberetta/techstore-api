package cl.techstore.api.techstore_api.model;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 250)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(nullable = false)
    private Boolean activo = true;

}
