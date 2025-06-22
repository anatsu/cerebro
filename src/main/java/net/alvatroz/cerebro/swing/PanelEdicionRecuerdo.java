/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.swing;

import jakarta.annotation.PostConstruct;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.alvatroz.cerebro.dto.Recuerdo;
import net.alvatroz.cerebro.service.RecuerdoService;
import net.alvatroz.cerebro.swing.service.GeneradorDeRecursos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author alvaro
 */
@Component
@Slf4j
public class PanelEdicionRecuerdo extends JPanel {

    /**
     * Para recuperar recuerdos.
     */
    @Autowired
    private RecuerdoService recuerdoService;

    /**
     * Para generar botones con iconos.
     */
    @Autowired
    private GeneradorDeRecursos generadorDeBotones;

    /**
     * El area de texto con el recuerdo.
     */
    private JTextArea txtArea;

    /**
     * El campo para capturar las categorias.
     */
    private JTextField txtCategorias;

    /**
     * El recuerdo que se modifica o se dara de alta.
     */
    private Recuerdo recuerdo;

    /**
     * Ventana donde se encuentra este panel.
     */
    private JFrame padre;

    /**
     * Panel a donde se debe regresar la ejecución cuando este concluya sus
     * tareas.
     */
    private JPanel panelAnterior;

    private JScrollPane panelConScroll;

    /**
     * Inicializa los componentes graficos de este panel.
     */
    @PostConstruct
    public void init() {

        setLayout(new GridBagLayout());

        GridBagConstraints res = new GridBagConstraints();
        res.gridx = 0;
        res.gridy = 0;
        res.gridwidth = 3;
        res.gridheight = 1;
        res.weightx = 1;
        res.weighty = 1;
        res.fill = GridBagConstraints.BOTH;
        res.insets = new Insets(3, 3, 3, 3);
        res.anchor = GridBagConstraints.CENTER;
        txtArea = new JTextArea();
        panelConScroll = new JScrollPane(txtArea);
        add(panelConScroll, res);

        res.gridx = 0;
        res.gridy = 1;
        res.gridwidth = 1;
        res.gridheight = 1;
        res.weightx = 1;
        res.weighty = 0;
        res.fill = GridBagConstraints.NONE;
        res.insets = new Insets(3, 3, 3, 3);
        res.anchor = GridBagConstraints.WEST;
        add(new JLabel("Categorias separadas por comas: "), res);

        res.gridx = 0;
        res.gridy = 2;
        res.gridwidth = 3;
        res.gridheight = 1;
        res.weightx = 1;
        res.weighty = 0;
        res.fill = GridBagConstraints.HORIZONTAL;
        res.insets = new Insets(3, 3, 3, 3);
        res.anchor = GridBagConstraints.CENTER;
        txtCategorias = new JTextField("");
        add(txtCategorias, res);

        res.gridx = 1;
        res.gridy = 3;
        res.gridwidth = 1;
        res.gridheight = 1;
        res.weightx = 0;
        res.weighty = 0;
        res.fill = GridBagConstraints.NONE;
        res.insets = new Insets(3, 3, 3, 3);
        res.anchor = GridBagConstraints.CENTER;
        JButton btnGuardar = generadorDeBotones.generaBoton("gtk-save.png", "Guardar");

        add(btnGuardar, res);

        res.gridx = 2;
        res.gridy = 3;
        res.gridwidth = 1;
        res.gridheight = 1;
        res.weightx = 0;
        res.weighty = 0;
        res.fill = GridBagConstraints.NONE;
        res.insets = new Insets(3, 3, 3, 3);
        res.anchor = GridBagConstraints.CENTER;
        final JButton btnCancelar = generadorDeBotones.generaBoton("gtk-cancel.png", "Cancelar");
        add(btnCancelar, res);

        btnGuardar.addActionListener(a -> {
            try {
                String textCatego = StringUtils.trimToEmpty(txtCategorias.getText());
                String textRecuerdo = StringUtils.trimToEmpty(txtArea.getText());
                if (StringUtils.isBlank(textCatego) || StringUtils.isBlank(textRecuerdo)) {
                    JOptionPane.showMessageDialog(PanelEdicionRecuerdo.this, "Debe proporcionar al menos una categoria y un texto", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String textoCategorias = txtCategorias.getText().trim();
                    if (recuerdo.getIdRecuerdo() == null) {
                        recuerdoService.agregaRecuerdo(textoCategorias, txtArea.getText());
                        JOptionPane.showMessageDialog(getParent(), "Información agregada");
                    } else {
                        recuerdoService.modificaRecuerdo(textoCategorias, textRecuerdo, recuerdo.getIdRecuerdo());
                        JOptionPane.showMessageDialog(getParent(), "Información actualizada");
                    }

                    if (panelAnterior instanceof IRefrescaPanel iRefrescaPanel) {
                        iRefrescaPanel.refresca();
                    }
                    cambiaContenidoDeVentana(padre, panelAnterior);

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(getParent(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // al cancelar se pasa al panel anterior.
        btnCancelar.addActionListener(a -> {
            cambiaContenidoDeVentana(padre, panelAnterior);
        });

    }

    /**
     * Actualiza la información de este panel.
     *
     * @param recuerdo Objeto con el recuerdo.
     * @param padre Ventana padre.
     * @param panelAnterior Panel que se debe mostrar en el JFrame cuando se
     * pulse en cancelar o en agregar.
     */
    public void reset(final Recuerdo recuerdo, final JFrame padre, final JPanel panelAnterior) {

        log.info("Inicializando con el recuerdo: {}", recuerdo);

        this.recuerdo = recuerdo;
        this.padre = padre;
        this.panelAnterior = panelAnterior;

        txtArea.setText(StringUtils.defaultString(recuerdo.getTextoRecuerdo()));

        String textoCategorias = StringUtils.defaultString(recuerdo.getCategorias() == null || recuerdo.getCategorias().isEmpty()
                ? "" : recuerdo.getCategorias().stream().collect(Collectors.joining(",")));

        txtCategorias.setText(textoCategorias);

        cambiaContenidoDeVentana(padre, this);

    }

    /**
     * Cambia el contenido de un frame respetando el último tamaño colocado por
     * el usuaro..
     *
     * @param padre pantalla padre.
     * @param panel panel con el que se sustituira el contenido del jframe
     */
    public void cambiaContenidoDeVentana(final JFrame padre, JPanel panel) {
        
            SwingUtilities.invokeLater(() -> {
                
                
                
                log.info("Tamaño del scroll {} ", this.panelConScroll.getBounds().getSize());
                
                if (panel != padre.getContentPane()) {
                    padre.setContentPane(panel);
                }
                log.info("Tamaño del scroll final {} ", this.panelConScroll.getBounds().getSize());
                // el panel al crearse estaba chiquito como se esta rehusando al completo hay que pedir que se actualice.
                // se pide al panel actualizarse pues pudo haber cambiado el tamaño de la ventana por solicitud del usuario
                panel.updateUI();
                
                
                

            });
        

    }

}
