/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa un recuerdo de "algo"
 * @author alvaro
 */
@Getter
@Setter
@ToString
public class Recuerdo {
    
    /**
     * Identificador del recuerdo.
     */
    private Integer idRecuerdo;
    /**
     * Texto del recuerdo.
     */
    private String textoRecuerdo;
    
    /**
     * Fecha en que se registro el recuerdo.
     */
    private LocalDate fechaDelRecuerdo;
    
    /**
     * Categorias para las que aplica el recuerdo.
     */
    private List<String> categorias;
    
    
}
