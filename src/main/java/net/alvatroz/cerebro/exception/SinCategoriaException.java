/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.exception;

/**
 *
 * @author alvaro
 */
public class SinCategoriaException extends RuntimeException{

    public SinCategoriaException() {
        super("No se indicaron categorias");
    }
 
    
}
