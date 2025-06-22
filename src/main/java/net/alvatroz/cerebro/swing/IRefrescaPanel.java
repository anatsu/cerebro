/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.swing;

/**
 * Quien implemente este panel debera exponer un metodo para refrescar.
 * @author alvaro
 */
@FunctionalInterface
public interface IRefrescaPanel {
    
    /**
     * Refresca un panel.
     */
    void refresca();
    
}
