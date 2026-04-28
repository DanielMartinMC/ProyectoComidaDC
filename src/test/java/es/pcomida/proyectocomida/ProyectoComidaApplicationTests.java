package es.pcomida.proyectocomida;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // <-- Importante añadir esto

@SpringBootTest
class ProyectoComidaApplicationTests {

    @Test
    void contextLoads() {
    }

    // 👇 PEGA ESTE BLOQUE JUSTO AQUÍ 👇
    @Test
    void generarHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Escribe aquí la contraseña que quieres encriptar
        String miContrasenaLimpia = "password123456";

        String hashCifrado = encoder.encode(miContrasenaLimpia);

        System.out.println("=========================================================");
        System.out.println("Tu contraseña encriptada es: " + hashCifrado);
        System.out.println("=========================================================");
    }
}
