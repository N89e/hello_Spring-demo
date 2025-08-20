package fr.digi.hello.controleurs;

import fr.digi.hello.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST simple pour retourner un message de salutation.
 * Offre un endpoint "/hello" pour recevoir un message de bienvenue.
 */
@RestController
@RequestMapping("/hello")
public class HelloControleur {

    /**
     * Service injecté fournissant les messages de salutation.
     */
    @Autowired
    private HelloService helloService;

    /// Endpoint GET pour obtenir un message de salutation.
    ///
    /// @param 'helloService' Service injecté (corrigé : injecté via attribut, pas paramètres)
    /// @return La chaîne de caractères correspondant au message de salutation.
    @GetMapping
    public String direHello() {
        return helloService.salutations();
    }
}
