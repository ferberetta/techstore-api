package cl.techstore.api.techstore_api.dto;
import lombok.Data;

@Data
public class LoginRequest {

    private String usarmane;
    private String password;
}
