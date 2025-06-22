/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.exception;

/**
 *
 * @author alvaro
 */
public class SinCategoriaValidosException extends RuntimeException{

    public SinCategoriaValidosException() {
        super("Las categorias proporcionadas no son validas");
    }
    
}
