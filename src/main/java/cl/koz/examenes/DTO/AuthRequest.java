package cl.koz.examenes.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
