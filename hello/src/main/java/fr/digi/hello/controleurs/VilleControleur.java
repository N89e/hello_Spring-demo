package fr.digi.hello.controleurs;

import fr.digi.hello.dao.DepartementDao;
import fr.digi.hello.dto.VilleDto;
import fr.digi.hello.items.Departement;
import fr.digi.hello.items.Ville;
import fr.digi.hello.mappers.MapperUtil;
import fr.digi.hello.services.VilleService;
import fr.digi.hello.validators.VilleValidator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    private final VilleService villeService;
    private final DepartementDao departementDao;
    private final VilleValidator villeValidator;

    public VilleControleur(VilleService villeService, DepartementDao departementDao, VilleValidator villeValidator) {
        this.villeService = villeService;
        this.departementDao = departementDao;
        this.villeValidator = villeValidator;
    }

    @GetMapping
    public List<VilleDto> getAllVilles() {
        List<Ville> villes = villeService.extractVilles();
        return villes.stream()
                .map(MapperUtil::toVilleDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VilleDto> getVilleParId(@PathVariable int id) {
        return villeService.extractVille(id)
                .map(MapperUtil::toVilleDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<VilleDto> getVilleParNom(@PathVariable String nom) {
        return villeService.extractVille(nom)
                .map(MapperUtil::toVilleDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createVille(@Valid @RequestBody VilleDto villeDto, BindingResult bindingResult) {

        villeValidator.validate(villeDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        Departement departement = null;
        if (villeDto.getDepartement() != null && !villeDto.getDepartement().isEmpty()) {
            departement = departementDao.findByNomIgnoreCase(villeDto.getDepartement())
                    .orElseThrow(() -> new RuntimeException("Département introuvable"));
        }

        Ville ville = MapperUtil.toVille(villeDto, departement);

        List<Ville> villes = villeService.insertVille(ville);

        List<VilleDto> dtos = villes.stream().map(MapperUtil::toVilleDto).toList();

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{nom}")
    public ResponseEntity<?> updateVille(@PathVariable String nom, @Valid @RequestBody VilleDto villeDto, BindingResult bindingResult) {

        villeValidator.validate(villeDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        Departement departement = null;
        if (villeDto.getDepartement() != null && !villeDto.getDepartement().isEmpty()) {
            departement = departementDao.findByNomIgnoreCase(villeDto.getDepartement())
                    .orElse(null);

            if (departement == null) {
                return ResponseEntity.badRequest().body("Département introuvable");
            }
        }

        Ville villeModifiee = MapperUtil.toVille(villeDto, departement);

        List<Ville> villes = villeService.modifierVilleParNom(nom, villeModifiee);

        List<VilleDto> dtos = villes.stream().map(MapperUtil::toVilleDto).toList();

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{nom}")
    public ResponseEntity<List<VilleDto>> deleteVille(@PathVariable String nom) {
        List<Ville> villes = villeService.supprimerVilleParNom(nom);
        List<VilleDto> dtos = villes.stream().map(MapperUtil::toVilleDto).toList();
        return ResponseEntity.ok(dtos);
    }
}
