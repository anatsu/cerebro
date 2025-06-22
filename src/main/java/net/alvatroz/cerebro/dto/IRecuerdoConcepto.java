/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.dto;

import java.time.LocalDate;

/**
 *
 * @author alvaro
 */
public interface IRecuerdoConcepto {
    /**
     * 
     * @return Recupera el id de recuerdo.
     */
    Integer getIdRecuerdo();
    /**
     * 
     * @return Recupera la descripción del recuerdo.
     */
    String getDescripcion();
    /**
     * 
     * @return Recupera la fecha en que se registro el recuerdo.
     */
    LocalDate getFecha();
    /**
     * 
     * @return Recupera los conceptos clave (sustantivos) del recuerdo.
     */
    String getSustantivos();    
}
