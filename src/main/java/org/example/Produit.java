//
// Auteur : Filipe Dias Morais
// Projet : test3-DiasMoraisFilipe
// Date   : 04.04.2023
// 


package org.example;

import jakarta.persistence.*;


@Entity
@Table(name = "produits")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prd_num")
    private Long numero;

    @Column(name = "description")
    private String description;

    @Column(name = "poids")
    private Integer poids;

    @ManyToOne
    @JoinColumn(name = "vil_num")
    private Ville ville;

    @ManyToOne
    @JoinColumn(name = "cmn_num")
    private Camion camion;

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPoids() {
        return poids;
    }

    public void setPoids(Integer poids) {
        this.poids = poids;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Camion getCamion() {
        return camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }

}

