package fr.digi.hello.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de service fournissant une chaîne de caractères de salutation.
 * Déclare un bean String nommé "salutations" retournant un message de bienvenue.
 */
@Configuration
public class HelloService {

    /**
     * Bean qui retourne un message de salutation.
     *
     * @return La chaîne de caractères de salutation.
     */
    @Bean
    public String salutations() {
        return "Je suis la classe de service et je vous dis Bonjour";
    }
}
