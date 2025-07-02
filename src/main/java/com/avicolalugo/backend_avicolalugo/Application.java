package com.avicolalugo.backend_avicolalugo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // --- INICIO DE DEPURACIÓN MANUAL DE VARIABLES DE ENTORNO ---
        System.out.println("#############################################");
        System.out.println("### INICIANDO DEBUG DE VARIABLES DE ENTORNO ###");
        System.out.println("#############################################");

        String databaseUrl = System.getenv("DATABASE_URL");
        String port = System.getenv("PORT");

        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            System.out.println("RENDER_DATABASE_URL detectada: " + databaseUrl);
        } else {
            System.out.println("RENDER_DATABASE_URL NO detectada.");
        }

        if (port != null && !port.isEmpty()) {
            System.out.println("RENDER_PORT detectado: " + port);
        } else {
            System.out.println("RENDER_PORT NO detectado (usando 8080 si no hay override).");
        }

        System.out.println("#############################################");
        System.out.println("### FIN DEBUG DE VARIABLES DE ENTORNO ###");
        System.out.println("#############################################");
        // --- FIN DE DEPURACIÓN MANUAL ---

        SpringApplication.run(Application.class, args);
    }

}