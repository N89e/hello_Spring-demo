package fr.digi.hello.controleurs;

import fr.digi.hello.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloControleur {

    private HelloService helloService;
    @GetMapping
    public String direHello(HelloService helloService){
        return helloService.salutations();
    }
}
