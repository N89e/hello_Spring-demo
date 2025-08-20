package fr.digi.hello.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloService {
    @Bean
    public String salutations(){
        return "Je suis la classe de service et je vous dis Bonjour";
    }
}
