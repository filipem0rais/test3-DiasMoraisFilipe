//
// Auteur : Filipe Dias Morais
// Projet : test3-DiasMoraisFilipe
// Date   : 04.04.2023
// 


package org.example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/camions")
public class CamionController {

    @Value("${jwt.secret}")
    private String SECRET;

    @Autowired
    private final CamionRepository camionRepository;
    private final ProduitRepository produitRepository;

    public CamionController(CamionRepository camionRepository, ProduitRepository produitRepository) {
        this.camionRepository = camionRepository;
        this.produitRepository = produitRepository;
    }
    private static final String SUBJECT = "logistique";


    // Commentaire perso : Tout fonctionne sauf l'authentification que je n'arrive pas à faire fonctionner avec postman
    @GetMapping("/{camionNum}")
    public ResponseEntity<Void> chargerProduit(@PathVariable int camionNum, @RequestParam int produit, HttpServletRequest request) throws CamionNotFoundException, ProduitNotFoundException, ProduitTropLourdException {
        if (!isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Récupération du camion
        Camion camion = camionRepository.findByNumero(camionNum)
                .orElseThrow(() -> new CamionNotFoundException("Camion '" + camionNum + "' pas trouvé"));

        // Récupération du produit
        Produit nProduit = produitRepository.findByNumero(String.valueOf(produit))
                .orElseThrow(() -> new ProduitNotFoundException("Produit '" + produit + "' pas trouvé"));

        // Vérification que le produit n'est pas déjà chargé dans un camion
        if (nProduit.getCamion() != null) {
            throw new ProduitNotFoundException("Produit '" + produit + "' déjà chargé dans un camion");
        }

        // Vérification que le camion a suffisamment de capacité pour charger le produit
        if (camion.getCharge() + nProduit.getPoids() > camion.getChargeMax()) {
            throw new ProduitTropLourdException("Produit trop lourd");
        }

        // Chargement du produit dans le camion
        nProduit.setCamion(camion);
        camion.setCharge(camion.getCharge() + nProduit.getPoids());

        // Sauvegarde en base de données
        produitRepository.save(nProduit);
        camionRepository.save(camion);

        return ResponseEntity.ok().build();
    }

    private boolean isAuthorized(HttpServletRequest request) {
        // Vérifier la présence de l'en-tête "Authorization"
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        // Extraire le jeton d'authentification de l'en-tête
        String token = authHeader.substring(7);

        try {
            // Vérifier le jeton d'authentification
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
