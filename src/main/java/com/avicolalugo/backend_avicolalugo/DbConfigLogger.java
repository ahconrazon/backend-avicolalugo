package com.avicolalugo.backend_avicolalugo; // <-- Asegúrate que este sea el paquete correcto de tu aplicación
// Si lo pusiste en un subpaquete, por ejemplo, 'config', sería:
// package com.avicolalugo.backend_avicolalugo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // Esta anotación le dice a Spring que esta clase es un componente que debe gestionar.
public class DbConfigLogger implements CommandLineRunner {

    // Inyecta el valor de la propiedad 'spring.datasource.url' de tus configuraciones.
    @Value("${spring.datasource.url}")
    private String dbUrl;

    // Inyecta el valor de la propiedad 'spring.datasource.username'.
    @Value("${spring.datasource.username}")
    private String dbUsername;

    /**
     * ¡ADVERTENCIA DE SEGURIDAD!
     * Loggear contraseñas directamente en producción es una práctica MUY INSEGURA.
     * Esta línea es solo para propósitos de depuración TEMPORAL.
     * Asegúrate de COMENTARLA o ELIMINARLA antes de desplegar en un entorno de producción.
     */
    @Value("${spring.datasource.password}")
    private String dbPassword;

    /**
     * El método 'run' se ejecutará una vez que la aplicación Spring Boot haya arrancado.
     * Es ideal para realizar tareas al inicio, como esta depuración.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n"); // Para separar visualmente en los logs

        System.out.println("#############################################");
        System.out.println("### INICIANDO DEBUG DE CONFIGURACIÓN DB ###");
        System.out.println("#############################################");
        System.out.println(" ");

        System.out.println("URL de la Base de Datos (spring.datasource.url): " + dbUrl);
        System.out.println("Usuario de la Base de Datos (spring.datasource.username): " + dbUsername);
        System.out.println("Contraseña de la Base de Datos (spring.datasource.password): " + dbPassword); // ¡Recuerda la advertencia!

        System.out.println(" ");
        System.out.println("#############################################");
        System.out.println("### FIN DEBUG DE CONFIGURACIÓN DB ###");
        System.out.println("#############################################");
        System.out.println("\n");
    }
}