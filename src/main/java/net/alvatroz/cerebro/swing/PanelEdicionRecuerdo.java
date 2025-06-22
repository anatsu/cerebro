/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.swing;

import jakarta.annotation.PostConstruct;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
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

        JScrollPane panelConScroll = new JScrollPane(txtArea);
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

        InputMap im = txtArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = txtArea.getActionMap();
        im.put(KeyStroke.getKeyStroke("control F"), "abrirBusqueda");
        am.put("abrirBusqueda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buscado = JOptionPane.showInputDialog(PanelEdicionRecuerdo.this, "Cadena a buscar");
                resaltaTexto(txtArea, buscado);
            }
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

            if (panel != padre.getContentPane()) {
                padre.setContentPane(panel);
            }

            // el panel al crearse estaba chiquito como se esta rehusando al completo hay que pedir que se actualice.
            // se pide al panel actualizarse pues pudo haber cambiado el tamaño de la ventana por solicitud del usuario
            panel.updateUI();

        });

    }

    /**
     * Busca un texto y lo resalta.
     *
     * @param textArea Area de texto donde se resaltará la información.
     * @param textoABuscar cadena a buscar no es una expresion regular.
     */
    public void resaltaTexto(JTextArea textArea, String textoABuscar) {
        

        log.info("Texto a buscar: {}", textoABuscar);

        Highlighter highlighter = textArea.getHighlighter();

        highlighter.removeAllHighlights();

        if (StringUtils.isNotBlank(textoABuscar)) {

            String text = textArea.getText();
            int longitudTextoABuscar = textoABuscar.length();
            int longitudTotalTexto = text.length();

            if (StringUtils.isNotBlank(text)) {

                log.info("Intentando resaltar texto");
                boolean encontroElTexto = 
                        resaltaTexto(text, textoABuscar, longitudTextoABuscar, longitudTotalTexto, highlighter);
                if( !encontroElTexto ){
                    JOptionPane.showMessageDialog(this, "No se encontro : "+ textoABuscar);
                }
            }

        }

    }

    /**
     * Resalta un texto buscandolo en una cadena
     * @param text Texto del JTextArea
     * @param textoABuscar texto a buscar que podria no encontrarse.
     * @param longitudTextoABuscar  tamaño del texto a buscar.
     * @param longitudTotalTexto longitud del texto a buscar
     * @param highlighter objeto para resaltar el texto.
     * @return  retorna true si encontro algun texto
     */
    private boolean resaltaTexto( final String text, 
                             final String textoABuscar,                               
                             final int longitudTextoABuscar, 
                             final int longitudTotalTexto,  
                             Highlighter highlighter) {
        int indicePrimerCoincidencia = -1;
        final int ITERACIONES_MAX = 1000;
        int iterador = 0;
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        int indice = StringUtils.indexOfIgnoreCase(text, textoABuscar);
        
        try {
            while (iterador < ITERACIONES_MAX && indice >= 0 && indice + longitudTextoABuscar < longitudTotalTexto) {
                
                if (indicePrimerCoincidencia == -1) {
                    indicePrimerCoincidencia = indice;
                }
                
                highlighter.addHighlight(indice, indice + longitudTextoABuscar, painter);
                indice = StringUtils.indexOfIgnoreCase(text, textoABuscar, indice + longitudTextoABuscar);
                
                iterador++;
            }
        } catch (BadLocationException e) {
            log.error("Fallo al intentar resaltar texto", e);
        }
        
        if (indicePrimerCoincidencia != -1) {
            txtArea.setCaretPosition(indicePrimerCoincidencia);
            try {
                Rectangle2D viewRect = txtArea.modelToView2D(indicePrimerCoincidencia);
                if (viewRect != null) {
                    txtArea.scrollRectToVisible(viewRect.getBounds());
                }
            } catch (Exception e) {
                log.error("No se pudo cambiar el cursor de lugar");
            }
        }
        return indicePrimerCoincidencia != -1;
    }
}
