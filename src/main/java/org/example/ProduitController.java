//
// Auteur : Filipe Dias Morais
// Projet : test3-DiasMoraisFilipe
// Date   : 04.04.2023
// 


package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    @Autowired
    private ProduitRepository produitRepository;
    private final VilleRepository villeRepository;

    public ProduitController(ProduitRepository produitRepository, VilleRepository villeRepository) {
        this.produitRepository = produitRepository;
        this.villeRepository = villeRepository;
    }

    @PostMapping
    public ResponseEntity<Void> createProduit(@RequestBody ProduitDTO produitDTO) throws BadRequestException {
        Ville ville = null;
        try {
            ville = villeRepository.findByNom(produitDTO.getVille())
                    .orElseThrow(() -> new VilleNotFoundException("Ville '" + produitDTO.getVille() + "' pas trouvée"));
        } catch (VilleNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Vérifier que les attributs obligatoires sont fournis
        if (produitDTO.getDescription() == null || produitDTO.getPoids() == 0 || produitDTO.getVille() == null) {
            throw new BadRequestException("Tous les attributs obligatoires doivent être fournis : description, poids, ville.");
        }

        // Vérifier que la description ne dépasse pas 20 caractères
        if (produitDTO.getDescription().length() > 20) {
            throw new BadRequestException("La description ne doit pas dépasser 20 caractères.");
        }

        // Vérifier que le poids est compris entre 1 et 1000
        if (produitDTO.getPoids() < 1 || produitDTO.getPoids() > 1000) {
            throw new BadRequestException("Le poids doit être compris entre 1 et 1000.");
        }

        Produit produit = new Produit();
        produit.setDescription(produitDTO.getDescription());
        produit.setPoids(produitDTO.getPoids());
        produit.setVille(ville);
        produit = produitRepository.save(produit);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(produit.getNumero()).toUri())
                .build();
    }


    @GetMapping("/{numero}")
    public ResponseEntity<Produit> getProduitByNumero(@PathVariable("numero") String numero) {
        Optional<Produit> produit = produitRepository.findByNumero(numero);

        return produit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
