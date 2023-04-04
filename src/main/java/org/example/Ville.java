//
// Auteur : Filipe Dias Morais
// Projet : test3-DiasMoraisFilipe
// Date   : 04.04.2023
// 


package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "villes")
public class Ville {

    @Id
    @Column(name = "vil_num")
    private Integer numero;

    @Column(name = "nom")
    private String nom;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
