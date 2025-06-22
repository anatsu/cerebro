/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.exception;

/**
 *
 * @author alvaro
 */
public class FraseNoValidaException extends RuntimeException{

    private final String frase;
    
    public FraseNoValidaException(String frase) {
        this.frase = frase;
    }

    @Override
    public String getLocalizedMessage() {
        return "La frase "+frase+" no es valida";
    }
    
    
}
