/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.swing;

import jakarta.annotation.PostConstruct;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import lombok.extern.slf4j.Slf4j;
import static net.alvatroz.cerebro.comun.ConstantesGenerales.PAGINAS_EN_PAGINADOR;
import static net.alvatroz.cerebro.comun.ConstantesGenerales.PAGINA_INICIAL;
import net.alvatroz.cerebro.dto.PaginasDto;
import net.alvatroz.cerebro.swing.service.GeneradorDeRecursos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author alvaro
 */
@Component
@Slf4j
public class PanelPaginacion extends JPanel {

    private PaginasDto paginasDto;

    /**
     * Para generar botones con iconos.
     */
    @Autowired
    private GeneradorDeRecursos generadorDeBotones;

    /**
     * Lista con todos los botones de numero de pagina a usar.
     */
    private final List<JButton> botonesNumericos = new ArrayList<>();

    /**
     * Fuente de boton resaltado.
     */
    private final Font FUENTE_BOTON_RESALTADO = new Font(UIManager.getFont("Button.font").getName(), Font.BOLD, UIManager.getFont("Button.font").getSize());

    @PostConstruct
    public void init() {        
        setLayout(new FlowLayout(FlowLayout.RIGHT));
    }

    /**
     * Cambia el contenido del panel de acuerdo al parametro enviado.
     *
     * @param paginasDto paginas a usar con el resultado de una busqueda
     * @param listenerBotonRecuerdo oyentes por la pulsacion de un boton
     * numerico
     */
    public void cambiaPaginas(final PaginasDto paginasDto, List<IListenerBotonRecuerdo> listenerBotonRecuerdo) {

        this.paginasDto = paginasDto;
        inicializa(listenerBotonRecuerdo);
    }

    /**
     * Inicializa los botones.
     *
     * @param listenerBotonRecuerdo
     */
    private void inicializa(List<IListenerBotonRecuerdo> listenerBotonRecuerdo) {
        this.removeAll();
        botonesNumericos.clear();

        JPanel panelNumeros = new JPanel();

        boolean mostrarControles = paginasDto.getTotalPaginas() > PAGINAS_EN_PAGINADOR;
        Point rangoVisible = new Point();
        rangoVisible.x = 0;
        rangoVisible.y = PAGINAS_EN_PAGINADOR - 1;

        if (mostrarControles) {

            JButton botonPaginaInicial = generadorDeBotones.generaBoton("start.png", "Pagina Inicial", false);
            botonPaginaInicial.addActionListener(getEventoBotonInicial(panelNumeros, rangoVisible));
            add(botonPaginaInicial);
            JButton botonPaginaAnterior = generadorDeBotones.generaBoton("back.png", "Anterior", false);
            botonPaginaAnterior.addActionListener(getEventoBotonAnterior(panelNumeros, rangoVisible));
            add(botonPaginaAnterior);

        }

        log.info("Paginas maximas: {}", PAGINAS_EN_PAGINADOR);
        for (int indice = 0; indice < paginasDto.getTotalPaginas(); indice++) {
            JButton btnPagina = new JButton((indice + 1) + "");
            btnPagina.addActionListener(getEventoClickBoton(indice, listenerBotonRecuerdo));
            this.botonesNumericos.add(btnPagina);
            if (indice < PAGINAS_EN_PAGINADOR) {
                log.info("Agregando el boton: {}", btnPagina.getName());
                panelNumeros.add(btnPagina);
            }
        }
        this.add(panelNumeros);

        if (mostrarControles) {

            JButton botonPaginaSiguiente = generadorDeBotones.generaBoton("next.png", "Siguiente", false);
            botonPaginaSiguiente.addActionListener(getEventoBotonSiguiente(panelNumeros, rangoVisible));
            add(botonPaginaSiguiente);

            JButton botonPaginaFinal = generadorDeBotones.generaBoton("finish.png", "Pag Final", false);
            botonPaginaFinal.addActionListener(getEventoBotonUltimo(panelNumeros, rangoVisible));
            add(botonPaginaFinal);

        }
        if (!botonesNumericos.isEmpty()) {
            JButton botonActivo = botonesNumericos.get(PAGINA_INICIAL);
            cambiaResaltado(botonActivo, true);

        }

    }

    private ActionListener getEventoBotonInicial(final JPanel panelNumeros, final Point rangoVisible) {
        return ev -> {
            int paginaOriginal = paginasDto.getNumeroPaginaActual();
            paginasDto.setNumeroPagina(PAGINA_INICIAL);
            if (paginaOriginal != PAGINA_INICIAL) {
                actualizaBotonesVisiblesXCambiosDePagina(paginaOriginal, PAGINA_INICIAL, rangoVisible, panelNumeros);
            }
        };
    }

    /**
     * Inicializa el evento del boton anterior
     *
     * @param panelNumeros
     * @param rangoVisible
     * @return
     */
    private ActionListener getEventoBotonAnterior(final JPanel panelNumeros, final Point rangoVisible) {
        return ev -> {

            int paginaOriginal = paginasDto.getNumeroPaginaActual();
            paginasDto.retrocedePagina();
            int nuevaPagina = paginasDto.getNumeroPaginaActual();

            actualizaBotonesVisiblesXCambiosDePagina(paginaOriginal, nuevaPagina, rangoVisible, panelNumeros);

        };
    }

    /**
     * Cambia los botones que deben visualizarse y resaltarse.
     *
     * @param paginaOriginal Pagina que se estaba viendo.
     * @param nuevaPagina Pagina que se debe ver ahora.
     * @param rangoVisible1 Rango de botones visibles.
     * @param panelNumeros Panel que almacena los números.
     */
    private void actualizaBotonesVisiblesXCambiosDePagina(int paginaOriginal, int nuevaPagina, final Point rangoVisible1, final JPanel panelNumeros) {
        log.info("(Pagina anterior, paginaNueva) = ({}, {})", paginaOriginal, nuevaPagina);
        if (paginaOriginal != nuevaPagina) {
            cambiaResaltado(botonesNumericos.get(paginaOriginal), false);
            cambiaResaltado(botonesNumericos.get(nuevaPagina), true);

            panelNumeros.removeAll();
            
            if (nuevaPagina < rangoVisible1.x) {
                log.info("La nueva pagina es más pequeña que el rango visible");
                rangoVisible1.x = nuevaPagina;
                rangoVisible1.y = nuevaPagina + (PAGINAS_EN_PAGINADOR - 1);
            }
            if (nuevaPagina > rangoVisible1.y) {
                log.info("La nueva pagina es más grande que el rango visible");
                rangoVisible1.x = nuevaPagina - (PAGINAS_EN_PAGINADOR-1);
                rangoVisible1.y = nuevaPagina;
            }
            
            for( int i = rangoVisible1.x ; i<= rangoVisible1.y; i++){
                panelNumeros.add(botonesNumericos.get(i));
            }

            botonesNumericos.get(nuevaPagina).doClick();
            
            panelNumeros.updateUI();
        }
    }

    /**
     * Inicializa el evento del boton siguiente
     *
     * @param panelNumeros Panel con los numeros.
     * @param rangoVisible Rango visible a mostrar.
     * @return Un ActionListener con la programación de lo que se debe hacer al pulsar el botón siguiente.
     */
    private ActionListener getEventoBotonSiguiente(final JPanel panelNumeros, final Point rangoVisible) {
        return ev -> {

            int paginaOriginal = paginasDto.getNumeroPaginaActual();
            paginasDto.avanzaPagina();
            int nuevaPagina = paginasDto.getNumeroPaginaActual();

            if (paginaOriginal != nuevaPagina) {
                actualizaBotonesVisiblesXCambiosDePagina(paginaOriginal, nuevaPagina, rangoVisible, panelNumeros);
            }
        };
    }

    /**
     * Inicializa el evento del boton siguiente
     *
     * @param panelNumeros
     * @param rangoVisible
     * @return
     */
    private ActionListener getEventoBotonUltimo(final JPanel panelNumeros, final Point rangoVisible) {
        return ev -> {

            int paginaOriginal = paginasDto.getNumeroPaginaActual();
            int nuevaPagina = paginasDto.getTotalPaginas() - 1;
            paginasDto.setNumeroPagina(nuevaPagina);

            if (paginaOriginal != nuevaPagina) {
                actualizaBotonesVisiblesXCambiosDePagina(paginaOriginal, nuevaPagina, rangoVisible, panelNumeros);
            }
        };
    }

    /**
     * Evento que se dispara cuando se da click en un boton de una pagina.
     *
     * @param numeroBoton Número de boton.
     * @param listenerBotonRecuerdo Listener para avisar que la lista de
     * recuerdos cambio.
     * @return Un action listener configurado para el boton.
     */
    private ActionListener getEventoClickBoton(int numeroBoton, List<IListenerBotonRecuerdo> listenerBotonRecuerdo) {
        return ev -> {
            log.info("Se ha pulsado en el boton: {}", numeroBoton);

            cambiaResaltado(botonesNumericos.get(paginasDto.getNumeroPaginaActual()), false);
            cambiaResaltado(botonesNumericos.get(numeroBoton), true);

            paginasDto.setNumeroPagina(numeroBoton);

            log.info("La pagina actual ahora tiene: {}", paginasDto.getPaginaActual());

            listenerBotonRecuerdo.stream().forEach(oyente -> oyente.actualizaXCambioRecuerdo());

        };
    }

    /**
     * Cambia el realtado de un boton.
     *
     * @param boton Boton.
     * @param resaltar Indica si se debe resaltar.
     */
    private void cambiaResaltado(JButton boton, boolean resaltar) {
        if (resaltar) {
            boton.setFont(FUENTE_BOTON_RESALTADO);
        } else {
            boton.setFont(UIManager.getFont("Button.font"));
        }

    }
}
