package fr.digi.hello.controleurs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloControleur {
    @GetMapping
    public String direHello(){
        return "Salut \uD83D\uDD90\uFE0F";
    }
}
