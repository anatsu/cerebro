/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.swing;

import java.awt.Dimension;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import lombok.extern.slf4j.Slf4j;
import net.alvatroz.cerebro.dto.Recuerdo;
import net.alvatroz.cerebro.swing.service.GeneradorDeRecursos;

/**
 *
 * @author alvaro
 */
@Slf4j
public class RecuerdoTableModel extends AbstractTableModel {

    /**
     * Lista de recuerdos a mostrar.
     */
    private List<Recuerdo> recuerdos;

    /**
     * Ventana padre.
     */
    private final JFrame framePadre;
    /**
     * Para editar el recuerdo.
     */
    private final PanelEdicionRecuerdo panelEdicionRecuerdo;

    /**
     * Generador de botones.
     */
    private GeneradorDeRecursos generadorDeBotones;

    /**
     * Crea un table model con recuerdos.
     *
     * @param recuerdos Lista de recuerdos
     * @param framePadre Ventana padre.
     * @param per Panel para editar el recuerdo.
     * @param generadorBotones Generador de botones
     */
    public RecuerdoTableModel(final List<Recuerdo> recuerdos, final JFrame framePadre, final PanelEdicionRecuerdo per, GeneradorDeRecursos generadorBotones) {
        this.generadorDeBotones = generadorBotones;
        this.recuerdos = recuerdos;
        this.framePadre = framePadre;
        this.panelEdicionRecuerdo = per;
    }

    public void setRecuerdos(List<Recuerdo> recuerdos) {
        this.recuerdos = recuerdos;
    }

    @Override
    public int getRowCount() {
        return recuerdos.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 ->
                "#";
            case 1 ->
                "Anotación";
            case 2 ->
                "Fecha";
            case 3 ->
                "Categorias";
            case 4 ->
                "";
            default ->
                "";
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 ->
                Integer.class;
            case 1 ->
                String.class;
            case 2 ->
                LocalDate.class;
            case 3 ->
                String.class;
            case 4 ->
                JButton.class;
            default ->
                null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        JButton btnModificar = generadorDeBotones.generaBoton("kedit.png", "Editar", false);
        btnModificar.addActionListener(a -> {

            Recuerdo recuerdo = recuerdos.get(rowIndex);
            log.info("Ejecutando boton de edición");
            panelEdicionRecuerdo.reset(recuerdo, framePadre, (JPanel) framePadre.getContentPane());

        });

        Recuerdo recuerdo = recuerdos.get(rowIndex);

        Object res = switch (columnIndex) {
            case 0 ->
                rowIndex + 1;
            case 1 ->
                recuerdo.getTextoRecuerdo();
            case 2 ->
                recuerdo.getFechaDelRecuerdo();
            case 3 ->
                recuerdo.getCategorias().stream().collect(Collectors.joining(","));
            case 4 ->
                btnModificar;
            default ->
                null;
        };
        return res;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        log.info("Agregando {} en ({},{})", aValue, rowIndex, columnIndex);
    }

}
