package fr.digi.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Spring Boot.
 * Point d'entrée pour démarrer le contexte et lancer l'application.
 */
@SpringBootApplication
public class HelloApplication {

    /**
     * Point d'entrée principal de l'application.
     *
     * @param args Arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }
}
