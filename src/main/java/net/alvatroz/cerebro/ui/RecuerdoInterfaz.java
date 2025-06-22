/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.alvatroz.cerebro.dto.PaginasDto;
import net.alvatroz.cerebro.dto.Recuerdo;
import net.alvatroz.cerebro.service.RecuerdoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author alvaro
 */
@Slf4j
@Component
public class RecuerdoInterfaz {

    @Autowired
    private RecuerdoService recuerdoService;

    public void menu() {
        String cadenaOpcion;
        Scanner escaner = new Scanner(System.in);
        do {
            log.info(" 1. Agregar recuerdo");
            log.info(" 2. Buscar recuerdo");
            log.info(" 3. Salir");
            log.info(" Opcion: ");
            cadenaOpcion = escaner.nextLine();

            switch (cadenaOpcion) {
                case "1" ->
                    agregaRecuerdo();
                case "2" ->
                    buscaRecuerdo();
                default -> {
                }
            }

        } while (!cadenaOpcion.equals("3"));
    }

    public void agregaRecuerdo() {
        Scanner escaner = new Scanner(System.in);
        log.info("Texto de recuerdo: ");
        String descripcion = escaner.nextLine();
        log.info("Conceptos: ");
        String conceptos = escaner.nextLine();        
        recuerdoService.agregaRecuerdo(conceptos, descripcion);
    }

    public void buscaRecuerdo() {
        Scanner escaner = new Scanner(System.in);

        do {
            log.info("¿que quieres recordar (s-salir)?: ");
            String frase = escaner.nextLine();

            if (!StringUtils.equalsIgnoreCase("s", frase)) {
                List<Integer> recuerdos = recuerdoService.getIdsRecuerdos(frase);

                if (recuerdos.isEmpty()) {
                    log.info("No hubo resultados");
                } else {

                    int tamanioPagina = 10;
                    List<List<Integer>> listas = new ArrayList<>();
                    List<Integer> paginaActual = new ArrayList<>(tamanioPagina);
                    listas.add(paginaActual);

                    int contador = 0;
                    for (Integer rec : recuerdos) {
                        contador++;
                        if (contador == tamanioPagina) {
                            paginaActual = new ArrayList<>(tamanioPagina);
                            listas.add(paginaActual);
                            contador = 0;
                        }
                        paginaActual.add(rec);
                    }
                    PaginasDto paginas = new PaginasDto(0, listas);
                    paginaResultado(paginas);

                }

            }
        } while (StringUtils.equalsIgnoreCase("s", "frase"));

    }

    public void paginaResultado(PaginasDto paginas) {

        List<Integer> idsRecuerdos = paginas.getListaPaginas().get(paginas.getNumeroPagina());
        List<Recuerdo> paginaActual = recuerdoService.getRecuerdos(idsRecuerdos);
        
        String opc;
        Map<String, Recuerdo> mapaContador = imprimeResumen(paginaActual);
        log.info(" ");
        imprimeControl(paginas);
        Scanner escaner = new Scanner(System.in);
        opc = escaner.nextLine();
        do {
            if (mapaContador.containsKey(opc)) {
                log.info("");
                log.info("{} ", mapaContador.get(opc).getTextoRecuerdo());
                log.info("");
                log.info("{} ", mapaContador.get(opc).getCategorias().stream().collect(Collectors.joining(",")));
                log.info("");
                imprimeControlConOpcionActualizacion(paginas);
                String opc2 = escaner.nextLine();
                if( StringUtils.equalsIgnoreCase("a", opc2)){
                    actualizaRecuerdo( mapaContador.get(opc));
                }else{
                    opc = opc2;
                }
            }
            if (StringUtils.equalsIgnoreCase("<", opc) && paginas.getNumeroPagina() != 0) {
                paginas.setNumeroPagina(paginas.getNumeroPagina() - 1);
                List<Integer> idsPagAct = paginas.getListaPaginas().get(paginas.getNumeroPagina());
                paginaActual = recuerdoService.getRecuerdos(idsPagAct);                
                mapaContador = imprimeResumen(paginaActual);
                imprimeControl(paginas);
                opc = escaner.nextLine();
            }
            if (StringUtils.equalsIgnoreCase(">", opc) && paginas.getNumeroPagina() < paginas.getListaPaginas().size() - 1) {
                paginas.setNumeroPagina(paginas.getNumeroPagina() + 1);
                List<Integer> idsPagAct = paginas.getListaPaginas().get(paginas.getNumeroPagina());
                paginaActual = recuerdoService.getRecuerdos(idsPagAct);
                mapaContador = imprimeResumen(paginaActual);
                imprimeControl(paginas);
                opc = escaner.nextLine();
            }
            
                        

        }while( !StringUtils.equalsIgnoreCase("s", opc));
    }

    private void imprimeControl(PaginasDto paginas) {
        log.info("{} {}  s-salir", paginas.getNumeroPagina() == 0 ? " " : "<", paginas.getNumeroPagina() == paginas.getListaPaginas().size() - 1 ? " " : ">");
    }
    private void imprimeControlConOpcionActualizacion(PaginasDto paginas) {
        log.info("{} {}  s-salir a-actualizaar", paginas.getNumeroPagina() == 0 ? " " : "<", paginas.getNumeroPagina() == paginas.getListaPaginas().size() - 1 ? " " : ">");
    }

    private Map<String, Recuerdo> imprimeResumen(List<Recuerdo> paginaActual) {
        int contador = 1;
        Map<String, Recuerdo> mapaContador = new HashMap<>();
        for (Recuerdo rec : paginaActual) {
            log.info("{}: {}", contador, StringUtils.substring(rec.getTextoRecuerdo(), 0, 50));
            mapaContador.put(contador + "", rec);
            contador++;

        }
        return mapaContador;
    }

    public void actualizaRecuerdo(Recuerdo recuerdo) {

    }
}
