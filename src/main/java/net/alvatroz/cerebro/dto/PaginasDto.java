/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author alvaro
 */
@Getter
@Setter
@AllArgsConstructor
public class PaginasDto {
    /**
     * Número de pagina recuperado.
     */
    private int numeroPagina;
    /**
     * Lista de paginas recuperadas.
     */
    private List<List<Integer>> listaPaginas;
    
    public List<Integer> getPaginaActual(){
        return listaPaginas.get(numeroPagina);
    }
    
    /**
     * Recupera el número d epagina actual
     * @return el numero de la pagina actual
     */
    public int getNumeroPaginaActual(){
        return numeroPagina;
    }
    
    public void cambiaPagina(int numeroPagina){
        if( numeroPagina >= 0 && numeroPagina < listaPaginas.size()){
            this.numeroPagina = numeroPagina;            
        }
    }
    
    public void avanzaPagina(){
        if( numeroPagina < listaPaginas.size()-1){
            numeroPagina++;
        }
    }
    public void retrocedePagina(){
        if( numeroPagina > 0 ){
            numeroPagina--;
        }
    }
    /**
     * Total de paginas recuperadas.
     * @return un numero entero mayor o igual a cero.
     */
    public int getTotalPaginas(){
        return listaPaginas.size();
    }
}
