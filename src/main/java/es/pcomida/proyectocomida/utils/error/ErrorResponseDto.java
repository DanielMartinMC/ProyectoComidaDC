package es.pcomida.proyectocomida.utils.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponseDto {
    private String message;
    private int status;
    private HttpStatus error;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, String> errors; // Para errores de validación
}
