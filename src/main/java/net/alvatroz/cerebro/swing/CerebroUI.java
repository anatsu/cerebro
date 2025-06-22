/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.swing;

import jakarta.annotation.PostConstruct;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.JFrame;
import lombok.extern.slf4j.Slf4j;
import net.alvatroz.cerebro.swing.service.GeneradorDeRecursos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author alvaro
 */
@Component
@Scope("prototype")
@Slf4j
public class CerebroUI extends JFrame {

    /**
     * Para recuperar imagenes.
     */
    @Autowired
    private GeneradorDeRecursos generadorDeRecursos;
    /**
     * Panel principal 
     */
    @Autowired
    private PanelPrincipal panelPrincipal;

    /**
     * Inicia con la carga de la pantalla.
     */
    @PostConstruct
    public void init() {
        setTitle("Cerebro");
        
        
        Image icono =  generadorDeRecursos.getImagen("cerebro-ico-alfa.png");
        if( icono != null){
            setIconImage(icono);
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        panelPrincipal.init(this);
        setContentPane(panelPrincipal);        
        setMinimumSize( new Dimension(800, 600));
    }
    

}
