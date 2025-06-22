/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import static net.alvatroz.cerebro.comun.ConstantesGenerales.TAMANIO_PAGINA;
import net.alvatroz.cerebro.dto.PaginasDto;
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
@Slf4j
@Component
public class PanelPrincipal extends JPanel implements IRefrescaPanel {

    /**
     * Para consultar recuerdos.
     */
    @Autowired
    private RecuerdoService recuerdoService;

    /**
     * Para editar el recuerdo ya sea agregando uno nuevo o modificando el
     * existente.
     */
    @Autowired
    private PanelEdicionRecuerdo panelEdicionRecuerdo;

    /**
     * Para generar botones con iconos.
     */
    @Autowired
    private GeneradorDeRecursos generadorDeBotones;

    /**
     * Total de paginas recuperadas por la busqueda.
     */
    private PaginasDto paginas;

    /**
     * Accion al buscar.
     */
    private ActionListener actionBuscar;

    @Autowired
    private PanelPaginacion panelPaginacion;

    /**
     * Inicializa el panel principal. El metodo deberia ejecutarse solo una vez.
     *
     * @param pantallaPadre Frame donde se encuentra la pantalla.
     */
    public void init(final JFrame pantallaPadre) {

        setLayout(new GridBagLayout());

        GridBagConstraints res = new GridBagConstraints();

        res.gridx = 0;
        res.gridy = 0;
        res.gridwidth = 1;
        res.gridheight = 1;
        res.weightx = 1;
        res.weighty = 0.009;
        res.fill = GridBagConstraints.BOTH;
        res.insets = new Insets(3, 3, 3, 3);

        JTextField txtBusqueda = new JTextField();
        add(txtBusqueda, res);

        // boton buscar
        res.gridx = 1;
        res.gridy = 0;
        res.gridwidth = 1;
        res.gridheight = 1;
        res.weightx = 0;
        res.weighty = 0;
        res.fill = GridBagConstraints.NONE;
        res.insets = new Insets(3, 3, 3, 3);
        final JButton btnBuscar = generadorDeBotones.generaBoton("find.png", "Buscar anotaciones");
        add(btnBuscar, res);

        res.gridx = 2;
        res.gridy = 0;
        res.gridwidth = 1;
        res.gridheight = 1;
        res.weightx = 0;
        res.weighty = 0;
        res.fill = GridBagConstraints.NONE;
        res.insets = new Insets(3, 8, 3, 3);
        final JButton btnAgregar = generadorDeBotones.generaBoton("add.png", "Anotacion");
        add(btnAgregar, res);

        res.gridx = 0;
        res.gridy = 1;
        res.gridwidth = 3;
        res.gridheight = 1;
        res.weightx = 1;
        res.weighty = 1;
        res.fill = GridBagConstraints.BOTH;
        res.insets = new Insets(3, 3, 3, 3);
        final RecuerdoTableModel recuerdoTableModel = new RecuerdoTableModel(Collections.emptyList(), pantallaPadre, panelEdicionRecuerdo, generadorDeBotones);
        JTable table = new JTable(recuerdoTableModel);
        table.getColumnModel().getColumn(0).setMaxWidth(25);
        table.getColumnModel().getColumn(2).setMaxWidth(100);
        table.getColumnModel().getColumn(4).setMaxWidth(150);
        table.getColumnModel().getColumn(4).setCellRenderer((JTable table1, Object value, boolean isSelected, boolean hasFocus, int row, int column) -> {
            return (JButton) value;
        });
        table.getColumnModel().getColumn(4).setCellEditor(new BotonCellEditor());
        table.setRowHeight(20);

        JScrollPane scrollPanel = new JScrollPane(table);
        scrollPanel.getViewport().setBackground(Color.WHITE);
        add(scrollPanel, res);
        
        res.gridx = 0;
        res.gridy = 2;
        res.gridwidth = 3;
        res.gridheight = 1;
        res.weightx = 0;
        res.weighty = 0;
        res.fill = GridBagConstraints.HORIZONTAL;
        res.insets = new Insets(3, 3, 3, 3);
        add(panelPaginacion, res);
        
        actionBuscar = e -> {
            String searchText = txtBusqueda.getText();
            log.info("Se lanza busqueda: {}", searchText);
            if (StringUtils.isBlank(searchText)) {
                log.info("No hay nada que buscar se omite busqueda");
                recuerdoTableModel.setRecuerdos(Collections.emptyList());
            } else {
                log.info("Invocando al servicio");
                List<Integer> recuerdos = recuerdoService.getIdsRecuerdos(searchText);

                List<List<Integer>> listas = new ArrayList<>();
                List<Integer> paginaActual = new ArrayList<>(TAMANIO_PAGINA);
                listas.add(paginaActual);

                
                for (Integer rec : recuerdos) {                    
                    if (paginaActual.size() == TAMANIO_PAGINA) {
                        paginaActual = new ArrayList<>(TAMANIO_PAGINA);
                        listas.add(paginaActual);                        
                    }
                    paginaActual.add(rec);                    
                }
                paginas = new PaginasDto(0, listas);

                log.info("Recuerdos recuperados: {}", recuerdos);
                log.info("Total paginas: {}", paginas.getTotalPaginas());

                IListenerBotonRecuerdo listener = () -> {
                    
                    List<Recuerdo> lstRecuerdosAMostrar = recuerdoService.getRecuerdos(paginas.getPaginaActual());
                    
                    recuerdoTableModel.setRecuerdos(lstRecuerdosAMostrar);
                    SwingUtilities.invokeLater(() -> {
                        scrollPanel.updateUI();                        
                        recuerdoTableModel.fireTableDataChanged();
                    });
                };

                panelPaginacion.cambiaPaginas(paginas, List.of(listener));
                

                listener.actualizaXCambioRecuerdo();
            }

            
        };

        txtBusqueda.addActionListener(actionBuscar);
        btnBuscar.addActionListener(actionBuscar);

        btnAgregar.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                
                panelEdicionRecuerdo.reset(new Recuerdo(), pantallaPadre, this);
                
                
            });
        });
    }

    @Override
    public void refresca() {
        /**
         * Lanza la busqueda
         */
        actionBuscar.actionPerformed(null);
    }

}
