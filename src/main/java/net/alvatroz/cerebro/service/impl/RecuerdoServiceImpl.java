/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.service.impl;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.alvatroz.cerebro.dao.RecuerdoRepository;
import net.alvatroz.cerebro.dto.IRecuerdoConcepto;
import net.alvatroz.cerebro.dto.Recuerdo;
import net.alvatroz.cerebro.exception.FraseNoValidaException;
import net.alvatroz.cerebro.exception.IdRecuerdoNecesarioException;
import net.alvatroz.cerebro.exception.SinCategoriaException;
import net.alvatroz.cerebro.exception.SinCategoriaValidosException;
import net.alvatroz.cerebro.service.RecuerdoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author alvaro
 */
@Slf4j
@Service
public class RecuerdoServiceImpl implements RecuerdoService {

    @Autowired
    private RecuerdoRepository recuerdoRepository;

    @Override
    public List<Recuerdo> getRecuerdos(String frase) {
        if (StringUtils.isBlank(frase)) {
            throw new FraseNoValidaException(frase);
        }

        List<String> textos = getTextoBusquedaLimpio(frase);
        for (String texto : textos) {
            log.info("Texto: {}", texto);
        }

        List<Recuerdo> resultado = new LinkedList<>();

        recuerdoRepository.getRecuerdos(textos).forEach(rec -> {

            Recuerdo recuerdo = new Recuerdo();
            recuerdo.setCategorias(new LinkedList<>());
            recuerdo.setFechaDelRecuerdo(rec.getFecha());
            recuerdo.setIdRecuerdo(rec.getIdRecuerdo());
            recuerdo.setTextoRecuerdo(rec.getDescripcion());
            recuerdo.setCategorias(Arrays.stream(rec.getSustantivos().split(",", -1)).collect(Collectors.toList()));

            resultado.add(recuerdo);

        });

        return resultado;

    }

    private List<String> getCategorias(String categorias) {

        List<String> categos
                = List.of(categorias.split(",", -1))
                        .stream()
                        .map(c -> StringUtils.trim(c))
                        .map(c -> StringUtils.upperCase(c))
                        .collect(Collectors.toList());

        if (categorias.isEmpty()) {

            throw new SinCategoriaException();
        } else {
            Set<String> textosNoUsables = recuerdoRepository.getTextosNoUsables(categos);
            categos.removeAll(textosNoUsables);

            if (categos.isEmpty()) {
                throw new SinCategoriaValidosException();
            }
        }

        return categos;
    }

    @Override
    @Transactional
    public void agregaRecuerdo(String categorias, String textoRecuerdo) {

        log.info("Agregando recuerdo: {} con categorias: {}", textoRecuerdo, categorias);

        List<String> categos = getCategorias(categorias);

        log.info("Categorias finales: {}", categos);
        
        Integer idRecuerdoTmp = recuerdoRepository.getNextIdRecuerdo();
        if (idRecuerdoTmp == null) {
            idRecuerdoTmp = 0;
        }
        final Integer idRecuerdo = idRecuerdoTmp;
        recuerdoRepository.saveRecuerdo(idRecuerdo, textoRecuerdo);
        categos.forEach(cat -> {
            recuerdoRepository.saveConceptoRecuerdo(idRecuerdo, cat);
        });
        log.info("Recuerdo agregado con el id: {}", idRecuerdo);

    }

    @Override
    @Transactional
    public void modificaRecuerdo(String categorias, String textoRecuerdo, Integer idRecuerdo) {

        if (idRecuerdo == null) {
            throw new IdRecuerdoNecesarioException();
        }

        List<String> categos = getCategorias(categorias);

        recuerdoRepository.borraConceptos(idRecuerdo);
        recuerdoRepository.updateRecuerdo(idRecuerdo, textoRecuerdo);

        categos.forEach(cat -> {
            recuerdoRepository.saveConceptoRecuerdo(idRecuerdo, cat);
        });
    }

    /**
     * Recupera los tokens de busqueda de una frase.
     * @param frase Frase con textos limpios
     * @return Una lista con las cadenas.
     */
    private List<String> getTextoBusquedaLimpio(String frase){
        return Arrays
                .asList(frase.split("\\s", -1))
                .stream()
                .map(s -> s.trim())
                .filter(s -> StringUtils.isNotBlank(s))
                .map(s -> s.toUpperCase())                
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Integer> getIdsRecuerdos(String frase) {
        log.info("Se buscan los ids recuerdos de la frase: {}", frase);
        if (StringUtils.isBlank(frase)) {
            throw new FraseNoValidaException(frase);
        }
        log.info("Cadenas : {}", Arrays
                .asList(frase.split("\\s", -1)));
        List<String> textos = getTextoBusquedaLimpio(frase);

        log.info("Se buscaran los textos: {} {}", textos, textos.size());

        List<Integer> resultados;
        if( textos.isEmpty()){
            log.info("La frase {} no contiene textos validos a buscar", frase);
            resultados = Collections.emptyList();
        }else{
            resultados = recuerdoRepository.getIdsRecuerdos(textos);
        }
        
        return resultados;

    }

    @Override
    public List<Recuerdo> getRecuerdos(List<Integer> listaIdsRecuerdos) {

        List<IRecuerdoConcepto> lstRecuerdos = recuerdoRepository.getRecuerdosXIdRecuerdo(listaIdsRecuerdos);

        Map<Integer, Integer> mapaOrdenIds = new HashMap<>();
        int contador = 0;
        for (Integer idRecuerdo : listaIdsRecuerdos) {
            mapaOrdenIds.put(idRecuerdo, contador);
            contador++;
        }

        Comparator<Recuerdo> comparador = (Recuerdo o1, Recuerdo o2) -> {
            Integer orden1 = mapaOrdenIds.get(o1.getIdRecuerdo());
            Integer orden2 = mapaOrdenIds.get(o2.getIdRecuerdo());

            orden1 = orden1 == null ? 0 : orden1;
            orden2 = orden2 == null ? 0 : orden2;

            return orden1.compareTo(orden2);
        };

        List<Recuerdo> resultado = new ArrayList<>(lstRecuerdos.size());
        lstRecuerdos.forEach(rec -> {

            Recuerdo recuerdo = new Recuerdo();
            recuerdo.setCategorias(new LinkedList<>());
            recuerdo.setFechaDelRecuerdo(rec.getFecha());
            recuerdo.setIdRecuerdo(rec.getIdRecuerdo());
            recuerdo.setTextoRecuerdo(rec.getDescripcion());
            recuerdo.setCategorias(Arrays.stream(rec.getSustantivos().split(",", -1)).collect(Collectors.toList()));

            resultado.add(recuerdo);

        });

        resultado.sort(comparador);

        return resultado;
    }

}
