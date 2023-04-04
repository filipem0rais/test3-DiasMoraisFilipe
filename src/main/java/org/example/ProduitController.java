//
// Auteur : Filipe Dias Morais
// Projet : test3-DiasMoraisFilipe
// Date   : 04.04.2023
// 


package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    @Autowired
    private ProduitRepository produitRepository;

    @GetMapping("/{numero}")
    public ResponseEntity<Produit> getProduitByNumero(@PathVariable("numero") String numero) {
        Optional<Produit> produit = produitRepository.findByNumero(numero);

        return produit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
