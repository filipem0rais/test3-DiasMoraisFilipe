//
// Auteur : Filipe Dias Morais
// Projet : test3-DiasMoraisFilipe
// Date   : 04.04.2023
// 


package org.example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/camions")
public class CamionController {

    private final CamionRepository camionRepository;
    private final ProduitRepository produitRepository;

    public CamionController(CamionRepository camionRepository, ProduitRepository produitRepository) {
        this.camionRepository = camionRepository;
        this.produitRepository = produitRepository;
    }
    private static final String SUBJECT = "logistique";
    private static final String SECRET = "mapetiteentreprisededistributionneconnaitpaslacrise";

    @PostMapping("/{camionNum}")
    public ResponseEntity<Void> chargerProduit(@PathVariable int camionNum, @RequestParam int produitNum, @RequestHeader("Authorization") String token) throws CamionNotFoundException, ProduitNotFoundException, ProduitTropLourdException {
        // Vérification de l'authentification
        if (!isAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Récupération du camion
        Camion camion = camionRepository.findByNumero(camionNum)
                .orElseThrow(() -> new CamionNotFoundException("Camion '" + camionNum + "' pas trouvé"));

        // Récupération du produit
        Produit produit = produitRepository.findByNumero(String.valueOf(produitNum))
                .orElseThrow(() -> new ProduitNotFoundException("Produit '" + produitNum + "' pas trouvé"));

        // Vérification que le produit n'est pas déjà chargé dans un camion
        if (produit.getCamion() != null) {
            throw new ProduitNotFoundException("Produit '" + produitNum + "' déjà chargé dans un camion");
        }

        // Vérification que le camion a suffisamment de capacité pour charger le produit
        if (camion.getCharge() + produit.getPoids() > camion.getChargeMax()) {
            throw new ProduitTropLourdException("Produit trop lourd");
        }

        // Chargement du produit dans le camion
        produit.setCamion(camion);
        camion.setCharge(camion.getCharge() + produit.getPoids());

        // Sauvegarde en base de données
        produitRepository.save(produit);
        camionRepository.save(camion);

        return ResponseEntity.ok().build();
    }

    private boolean isAuthorized(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return SUBJECT.equals(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


}
