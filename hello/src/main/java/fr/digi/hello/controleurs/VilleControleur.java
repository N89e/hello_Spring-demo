package fr.digi.hello.controleurs;

import fr.digi.hello.services.Ville;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {
    @GetMapping
    public List<Ville> getVilles() {
        List<Ville> villes = new ArrayList<>();
        villes.add(new Ville("Paris", 2148000));
        villes.add(new Ville("Marseille", 2100000));
        villes.add(new Ville("Nantes", 21400));
        return villes;
    }

}
