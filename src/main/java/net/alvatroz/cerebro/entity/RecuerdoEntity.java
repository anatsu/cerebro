/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author alvaro
 */
@Entity
@Table(name = "TARECUERDO")
@Getter
@Setter
public class RecuerdoEntity {
    
    /**
     * Identificador del recuerdo.
     */
    @Id
    @Column(name = "ID_RECUERDO")
    private Integer idRecuerdo;
    
    /**
     * Texto del recuerdo.
     */
    @Column(name = "DESCRIPCION")
    private String descripción;
    
    
    /**
     * Fecha del recuerdo.
     */
    @Column(name = "FECHA")
    private LocalDate fecha;
 
    
}
