//
// Auteur : Filipe Dias Morais
// Projet : test3-DiasMoraisFilipe
// Date   : 04.04.2023
// 


package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "camions")
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmn_num")
    private Long numero;

    @Column(name = "charge")
    private Integer charge;

    @Column(name = "charge_max")
    private Integer chargeMax;

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public Integer getChargeMax() {
        return chargeMax;
    }

    public void setChargeMax(Integer chargeMax) {
        this.chargeMax = chargeMax;
    }

}

