/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.swing.service;

import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 *
 * @author alvaro
 */
@Service
@Slf4j
public class GeneradorDeRecursos {
    
    /**
     * Para recuperar recursos.
     */
    @Autowired
    private ResourceLoader resourceLoader;
    
    /**
     * Recupera un icono. 
     * @param nombreImagen Nombre del archivo en la carpeta icons. Debe contener extension.
     * @return Icon o null si no pudo recuperarse la imagen.
     */
    public Image getImagen(final String nombreImagen){
        Resource resource = resourceLoader.getResource("images/icons/"+nombreImagen);
        Image icono = null;
        try{
            icono = new ImageIcon(resource.getURL()).getImage();
        }catch( IOException e){
            log.error("No fue posible recuperar la imagen", e);
        }
        return icono;
    }
    
    /**
     * Intenta generar un boton con una imagen. Si la imagen no se encuentra solo se usara el texto alternativo
     * @param nombreImagen Imagen que debe estar ubicada en src/resources/images/icons el nombre de la imagen debe incluir extension
     * @param textoAlternativo Texto alternativo que ira en la imagen.
     * @return Un boton con la imagen.
     */
    public JButton generaBoton(final String nombreImagen, final String textoAlternativo){
        return generaBoton(nombreImagen, textoAlternativo, true);
    }
    /**
     * Intenta generar un boton con una imagen. Si la imagen no se encuentra solo se usara el texto alternativo
     * @param nombreImagen Imagen que debe estar ubicada en src/resources/images/icons el nombre de la imagen debe incluir extension
     * @param textoAlternativo Texto alternativo que ira en la imagen.
     * @param mostrarTextoSiSeControImagen Indica si se debe mostrar el texto si se encontro la imagen
     * @return Un boton con la imagen.
     */
    public JButton generaBoton(final String nombreImagen, final String textoAlternativo, boolean mostrarTextoSiSeControImagen){
        Resource resource = resourceLoader.getResource("images/icons/"+nombreImagen);
        JButton boton;
        try {
            
            ImageIcon icono = new ImageIcon(resource.getURL());
            if( mostrarTextoSiSeControImagen){
                boton = new JButton( textoAlternativo, icono);
            }else{
                boton = new JButton(icono);
            }
            
        } catch (IOException e) {
            log.error("Fallo la carga de la imagen {}", nombreImagen, e);
            boton = new JButton(textoAlternativo);
        }
        return boton;
    }
}
