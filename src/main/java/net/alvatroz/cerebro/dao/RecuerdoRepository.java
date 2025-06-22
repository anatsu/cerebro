/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.alvatroz.cerebro.dao;

import java.util.List;
import java.util.Set;
import net.alvatroz.cerebro.dto.IRecuerdoConcepto;
import net.alvatroz.cerebro.entity.RecuerdoEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author alvaro
 */
public interface RecuerdoRepository extends Repository<RecuerdoEntity, Integer> {

    /**
     * Recupera un conjunto de recuerdos agregando orden en base a los más exactos.
     * @param sustantivos Sustativos a usar para buscar los recuerdos
     * @return Una lista no vacia de recuerdos.
     */
    @Query(value = "WITH PARCIAL AS("
            + "    select distinct recuerdo.ID_RECUERDO"
            + "         , recuerdo.DESCRIPCION"
            + "         , recuerdo.FECHA                  "
            + "         , sum(1) over( partition by recuerdo.ID_RECUERDO) TOTAL_COINCIDENCIAS"
            + "      from TARECUERDO recuerdo"
            + "      join TACONCEPTORECUERDO con"
            + "        on con.ID_RECUERDO = recuerdo.ID_RECUERDO"
            + "     where con.SUSTANTIVO in ( :sustantivos )"
            + " ) SELECT par.ID_RECUERDO IDRECUERDO"
            + "        , par.DESCRIPCION"
            + "        , par.FECHA"
            + "        , LISTAGG(con.sustantivo, ',') SUSTANTIVOS"
            + "     FROM PARCIAL par"
            + "     JOIN TACONCEPTORECUERDO con"
            + "       ON con.ID_RECUERDO = par.ID_RECUERDO "
            + " group by par.ID_RECUERDO "
            + "        , par.DESCRIPCION"
            + "        , par.FECHA"
            + " order by par.TOTAL_COINCIDENCIAS DESC"
            + "        , par.FECHA DESC", nativeQuery = true)
    List<IRecuerdoConcepto> getRecuerdos(@Param("sustantivos") List<String> sustantivos);
    
    /**
     * Recupera una lista de recuerdos sin comprometer un orden en base a los identificadores de un recuerdo.
     * @param idsRecuerdo Identificadores de recuerdos
     * @return Una lista de recuerdos ligados a los identificadores
     */
    @Query(value = "select recuerdo.ID_RECUERDO IDRECUERDO"
            + "         , recuerdo.DESCRIPCION"
            + "         , recuerdo.FECHA                  "
            + "         , LISTAGG(con.sustantivo, ',') SUSTANTIVOS"
            + "      from TARECUERDO recuerdo"
            + "      join TACONCEPTORECUERDO con"
            + "        on con.ID_RECUERDO = recuerdo.ID_RECUERDO"
            + "     where recuerdo.ID_RECUERDO IN ( :idsRecuerdo )"
            + "  group by recuerdo.ID_RECUERDO "
            + "         , recuerdo.DESCRIPCION"
            + "         , recuerdo.FECHA", nativeQuery = true)
    List<IRecuerdoConcepto> getRecuerdosXIdRecuerdo(@Param("idsRecuerdo") List<Integer> idsRecuerdo);
    /**
     * Consulta solo los ids de recuerdo
     * @param sustantivos Sustativos a utilizar para recuperar los recuerdos.
     * @return Una lista no nula de recuerdos
     */
    @Query(value = "WITH PARCIAL AS("
            + "    select distinct recuerdo.ID_RECUERDO"
            + "         , recuerdo.DESCRIPCION"
            + "         , recuerdo.FECHA                  "
            + "         , sum(1) over( partition by recuerdo.ID_RECUERDO) TOTAL_COINCIDENCIAS"
            + "      from TARECUERDO recuerdo"
            + "      join TACONCEPTORECUERDO con"
            + "        on con.ID_RECUERDO = recuerdo.ID_RECUERDO"
            + "     where con.SUSTANTIVO in ( :sustantivos )"
            + " ) SELECT par.ID_RECUERDO IDRECUERDO                "
            + "     FROM PARCIAL par      "
            + " order by par.TOTAL_COINCIDENCIAS DESC"
            + "        , par.FECHA DESC", nativeQuery = true)
    List<Integer> getIdsRecuerdos(@Param("sustantivos") List<String> sustantivos);
    
    
    

    @Query(value = "SELECT MAX(ID_RECUERDO) + 1 FROM TARECUERDO", nativeQuery = true)
    Integer getNextIdRecuerdo();

    @Query(value = " insert into TARECUERDO(ID_RECUERDO,DESCRIPCION,FECHA) VALUES ( :idRecuerdo, :descripcion, current_date() )",
            nativeQuery = true)
    @Modifying
    void saveRecuerdo(@Param("idRecuerdo") Integer idRecuerdo,
            @Param("descripcion") String descripcion);

    @Query(value = " update TARECUERDO set DESCRIPCION = :descripcion ,FECHA = current_date() WHERE ID_RECUERDO = :idRecuerdo",
            nativeQuery = true)
    @Modifying
    void updateRecuerdo(@Param("idRecuerdo") Integer idRecuerdo,
            @Param("descripcion") String descripcion);

    @Query(value = " INSERT INTO TACONCEPTORECUERDO (ID_RECUERDO,SUSTANTIVO) VALUES (:idRecuerdo, :sustantivo) ", nativeQuery = true)
    @Modifying
    void saveConceptoRecuerdo(@Param("idRecuerdo") Integer idRecuerdo,
            @Param("sustantivo") String sustantivo);

    @Query(value = "select PALABRA "
            + "    from TATEXTO where PALABRA in (:textos) ", nativeQuery = true)
    Set<String> getTextosNoUsables(@Param("textos") List<String> textos);

    @Query(value = "DELETE FROM TACONCEPTORECUERDO where ID_RECUERDO = :idRecuerdo", nativeQuery = true)
    @Modifying
    void borraConceptos(@Param("idRecuerdo") Integer idRecuerdo);
}
