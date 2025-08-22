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

/**
 * Contrôleur REST pour gérer les opérations sur les villes.
 * Expose des endpoints pour créer, lire, modifier et supprimer des villes.
 */
@RestController
@RequestMapping("/villes")
public class VilleControleur {

    private final VilleService villeService;
    private final DepartementDao departementDao;
    private final VilleValidator villeValidator;

    /**
     * Constructeur injectant les dépendances nécessaires.
     *
     * @param villeService     service gérant les opérations métiers sur les villes
     * @param departementDao   DAO pour accéder aux départements
     * @param villeValidator   validateur personnalisé pour les villes
     */
    public VilleControleur(VilleService villeService, DepartementDao departementDao, VilleValidator villeValidator) {
        this.villeService = villeService;
        this.departementDao = departementDao;
        this.villeValidator = villeValidator;
    }

    /**
     * Récupère la liste de toutes les villes.
     *
     * @return liste des villes sous forme de DTO
     */
    @GetMapping
    public List<VilleDto> getAllVilles() {
        List<Ville> villes = villeService.extractVilles();
        return villes.stream()
                .map(MapperUtil::toVilleDto)
                .toList();
    }

    /**
     * Récupère une ville par son identifiant.
     *
     * @param id identifiant de la ville
     * @return ville trouvée ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<VilleDto> getVilleParId(@PathVariable int id) {
        return villeService.extractVille(id)
                .map(MapperUtil::toVilleDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère une ville par son nom.
     *
     * @param nom nom de la ville
     * @return ville trouvée ou 404 si non trouvée
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<VilleDto> getVilleParNom(@PathVariable String nom) {
        return villeService.extractVille(nom)
                .map(MapperUtil::toVilleDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle ville.
     * Valide les données reçues et vérifie l'existence du département associé.
     *
     * @param villeDto      données de la ville au format DTO
     * @param bindingResult résultat de la validation
     * @return liste mise à jour des villes ou erreurs de validation
     */
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

        // Récupérer département (par code ou nom)
        Departement departement = departementDao.findByCodeIgnoreCase(villeDto.getCodeDpt().trim())
                .orElse(null);
        if (departement == null && villeDto.getNomDepartement() != null) {
            departement = departementDao.findByNomIgnoreCase(villeDto.getNomDepartement().trim())
                    .orElse(null);
        }

        Ville ville = MapperUtil.toVille(villeDto, departement);
        List<Ville> villes = villeService.insertVille(ville);

        List<VilleDto> dtos = villes.stream().map(MapperUtil::toVilleDto).toList();

        return ResponseEntity.ok(dtos);
    }


    /**
     * Modifie une ville identifiée par son nom.
     * Valide les données et vérifie que le département existe.
     *
     * @param nom           nom de la ville à modifier
     * @param villeDto      données modifiées de la ville au format DTO
     * @param bindingResult résultat de la validation
     * @return liste mise à jour des villes ou erreurs de validation
     */
    @PutMapping("/nom/{nom}")
    public ResponseEntity<?> updateVilleByNom(@PathVariable String nom,
                                         @Valid @RequestBody VilleDto villeDto,
                                         BindingResult bindingResult) {

        villeValidator.validate(villeDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        Departement departement = null;

        // Vérification via code département en priorité
        if (villeDto.getCodeDpt() != null && !villeDto.getCodeDpt().isBlank()) {
            departement = departementDao.findByCodeIgnoreCase(villeDto.getCodeDpt())
                    .orElse(null);
            if (departement == null) {
                return ResponseEntity.badRequest().body("Code département introuvable : " + villeDto.getCodeDpt());
            }
        }
        // Sinon on peut utiliser nom de département
        else if (villeDto.getNomDepartement() != null && !villeDto.getNomDepartement().isBlank()) {
            departement = departementDao.findByNomIgnoreCase(villeDto.getNomDepartement())
                    .orElse(null);
            if (departement == null) {
                return ResponseEntity.badRequest().body("Nom département introuvable : " + villeDto.getNomDepartement());
            }
        }

        Ville villeModifiee = MapperUtil.toVille(villeDto, departement);

        List<Ville> villes = villeService.modifierVilleParNom(nom, villeModifiee);

        List<VilleDto> dtos = villes.stream().map(MapperUtil::toVilleDto).toList();

        return ResponseEntity.ok(dtos);
    }
    /**
     * Modifie une ville identifiée par son identifiant.
     * Valide les données et vérifie que le département existe.
     *
     * @param id           nom de la ville à modifier
     * @param villeDto      données modifiées de la ville au format DTO
     * @param bindingResult résultat de la validation
     * @return liste mise à jour des villes ou erreurs de validation
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVille(@PathVariable Integer id,
                                         @Valid @RequestBody VilleDto villeDto,
                                         BindingResult bindingResult) {

        villeValidator.validate(villeDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        Departement departement = null;

        // Vérification via code département en priorité
        if (villeDto.getCodeDpt() != null && !villeDto.getCodeDpt().isBlank()) {
            departement = departementDao.findByCodeIgnoreCase(villeDto.getCodeDpt())
                    .orElse(null);
            if (departement == null) {
                return ResponseEntity.badRequest().body("Code département introuvable : " + villeDto.getCodeDpt());
            }
        }
        // Sinon on peut utiliser nom de département
        else if (villeDto.getNomDepartement() != null && !villeDto.getNomDepartement().isBlank()) {
            departement = departementDao.findByNomIgnoreCase(villeDto.getNomDepartement())
                    .orElse(null);
            if (departement == null) {
                return ResponseEntity.badRequest().body("Nom département introuvable : " + villeDto.getNomDepartement());
            }
        }

        Ville villeModifiee = MapperUtil.toVille(villeDto, departement);

        List<Ville> villes = villeService.modifierVille(id, villeModifiee);

        List<VilleDto> dtos = villes.stream().map(MapperUtil::toVilleDto).toList();

        return ResponseEntity.ok(dtos);
    }


    /**
     * Supprime une ville par son nom.
     *
     * @param nom nom de la ville à supprimer
     * @return liste mise à jour des villes
     */
    @DeleteMapping("/nom/{nom}")
    public ResponseEntity<List<VilleDto>> deleteVille(@PathVariable String nom) {
        List<Ville> villes = villeService.supprimerVilleParNom(nom);
        List<VilleDto> dtos = villes.stream().map(MapperUtil::toVilleDto).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Supprime une ville par son identifiant.
     *
     * @param id nom de la ville à supprimer
     * @return liste mise à jour des villes
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<List<VilleDto>> deleteVilleById(@PathVariable Integer id) {
        List<Ville> villes = villeService.supprimerVille(id);
        List<VilleDto> dtos = villes.stream().map(MapperUtil::toVilleDto).toList();
        return ResponseEntity.ok(dtos);
    }
}
