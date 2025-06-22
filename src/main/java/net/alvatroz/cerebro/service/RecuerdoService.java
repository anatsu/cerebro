/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.service;

import java.util.List;
import net.alvatroz.cerebro.dto.Recuerdo;

/**
 *
 * @author alvaro
 */
public interface RecuerdoService {
    
    /**
     * Busca aquellos recuerdos que tengan que ver con la frase.
     * @param frase. Una cadena de texto cualquiera. Puede ser: 'requerimeinto levantado ayer sobre inspeccion vehicular'
     * @return Una lista con los recuerdos ordenados del más exacto al menos exacto. 
     */
    List<Recuerdo> getRecuerdos(String frase);
    
    /**
     * Recupera una lista con identificadores de recuerdos.
     * @param frase frase a buscar.
     * @return Una lista de enteros posiblemente vacia con los identificadores de recuerdos ordenados del más exacto al menos exacto.
     */
    List<Integer> getIdsRecuerdos(String frase);
    
    
    /**
     * Recupera una lista de recuerdos que viene en el mismo orden de los identificadores que se pasan por parametro.
     * @param listaIdsRecuerdos Lista no nula con ids de recuerdos
     * @return Una lista no nula con recuerdos.
     */
    List<Recuerdo> getRecuerdos(List<Integer> listaIdsRecuerdos);
    
    /**
     * Agrega un recuerdo.
     * @param categorias Cadena con las categorias
     * @param textoRecuerdo Texto del recuerdo. No importa la longitud.
     */
    void agregaRecuerdo(String categorias, String textoRecuerdo);
    
    
    /**
     * Modifica un recuerdo.
     * @param categorias Cadena con las categorias
     * @param textoRecuerdo 
     * @param idRecuerdo 
     */
    void modificaRecuerdo(String categorias, String textoRecuerdo, Integer idRecuerdo );
}
