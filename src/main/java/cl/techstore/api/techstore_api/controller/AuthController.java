package cl.techstore.api.techstore_api.controller;
import cl.techstore.api.techstore_api.security.Jwtutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private Jwtutil jwtutil;

    @PostMapping("/login")
    public Map<String,String> Login (@RequestBody Map<String,String> user){
        String token = jwtutil.generateToken(user.get("username"));
        return Map.of("token", token);
    }


}
