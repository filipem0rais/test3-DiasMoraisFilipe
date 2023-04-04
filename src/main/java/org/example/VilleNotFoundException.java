//
// Auteur : Filipe Dias Morais
// Projet : test3-DiasMoraisFilipe
// Date   : 04.04.2023
// 


package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VilleNotFoundException extends Exception {
    public VilleNotFoundException(String s) {
        super(s);

    }
}
