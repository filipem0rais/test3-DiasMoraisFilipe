//
// Auteur : Filipe Dias Morais
// Projet : test3-DiasMoraisFilipe
// Date   : 04.04.2023
// 

package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    Optional<Produit> findByNumero(String numero);

}

