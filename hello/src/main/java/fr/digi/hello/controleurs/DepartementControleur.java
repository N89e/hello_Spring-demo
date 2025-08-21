package fr.digi.hello.controleurs;

import fr.digi.hello.dto.DepartementDto;
import fr.digi.hello.mappers.MapperUtil;
import fr.digi.hello.items.Departement;
import fr.digi.hello.services.DepartementService;
import fr.digi.hello.validators.DepartementValidator;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departements")
@Validated
public class DepartementControleur {

    private final DepartementService departementService;
    private final DepartementValidator departementValidator;

    public DepartementControleur(DepartementService departementService, DepartementValidator departementValidator) {
        this.departementService = departementService;
        this.departementValidator = departementValidator;
    }

    @GetMapping
    public List<DepartementDto> getAllDepartements() {
        return departementService.extractDepartements().stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartementDto> getDepartementById(@PathVariable @Positive Integer id) {
        return departementService.extractDepartement(id)
                .map(MapperUtil::toDepartementDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<DepartementDto> getDepartementByNom(@PathVariable String nom) {
        return departementService.extractDepartement(nom)
                .map(MapperUtil::toDepartementDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createDepartement(@RequestBody DepartementDto departementDto, BindingResult bindingResult) {
        departementValidator.validate(departementDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }
        if (departementService.extractDepartement(departementDto.getNom()).isPresent()) {
            return ResponseEntity.badRequest().body("Un département avec ce nom existe déjà");
        }

        Departement departement = MapperUtil.toDepartement(departementDto);
        List<Departement> departements = departementService.insertDepartement(departement);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/nom/{nom}")
    public ResponseEntity<?> updateDepartementNom(@PathVariable String nom,
                                                  @RequestBody DepartementDto departementDto,
                                                  BindingResult bindingResult) {
        departementValidator.validate(departementDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        // Vérifie que le département à modifier existe bien
        if (departementService.extractDepartement(nom).isEmpty()) {
            return ResponseEntity.badRequest().body("Le département à modifier n'existe pas");
        }

        // Vérifie l'existence du nom proposé pour éviter doublon (à adapter selon besoin)
        if (!nom.equals(departementDto.getNom()) && departementService.extractDepartement(departementDto.getNom()).isPresent()) {
            return ResponseEntity.badRequest().body("Un département avec ce nom existe déjà");
        }

        Departement departement = MapperUtil.toDepartement(departementDto);
        List<Departement> departements = departementService.modifierDepartementParNom(nom, departement);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartement(@PathVariable @Positive Integer id,
                                               @RequestBody DepartementDto departementDto,
                                               BindingResult bindingResult) {
        departementValidator.validate(departementDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }
        if (departementService.extractDepartement(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (departementService.extractDepartement(departementDto.getNom()).isPresent()) {
            return ResponseEntity.badRequest().body("Un département avec ce nom existe déjà");
        }

        Departement departement = MapperUtil.toDepartement(departementDto);
        List<Departement> departements = departementService.modifierDepartement(id, departement);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartement(@PathVariable @Positive Integer id) {
        if (departementService.extractDepartement(id).isEmpty()) {
            return ResponseEntity.badRequest().body("Le département à supprimer n'existe pas !");
        }

        List<Departement> departements = departementService.supprimerDepartement(id);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/nom/{nom}")
    public ResponseEntity<?> deleteDepartementParNom(@PathVariable String nom) {
        if (departementService.extractDepartement(nom).isEmpty()) {
            return ResponseEntity.badRequest().body("Le département à supprimer n'existe pas !");
        }

        List<Departement> departements = departementService.supprimerDepartementParNom(nom);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
