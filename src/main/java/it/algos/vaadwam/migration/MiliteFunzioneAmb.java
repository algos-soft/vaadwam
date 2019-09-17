package it.algos.vaadwam.migration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.annotations.ReadOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: mar, 27-feb-2018
 * Time: 20:39
 */
@Entity
@Table(name = "militefunzione")
@Access(AccessType.PROPERTY)
@Data
@EqualsAndHashCode(callSuper = false)
@ReadOnly
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class MiliteFunzioneAmb extends AmbEntity {

    private final static String DBNAME = "militefunzione";

    @Autowired
    private FunzioneAmb funzioneAmb;

    private long croce_id = 0;

    private long funzione_id = 0;

    private long milite_id = 0;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public MiliteFunzioneAmb() {
    }// end of constructor


    public List<MiliteFunzioneAmb> findAll() {
        return (List<MiliteFunzioneAmb>) super.findAll(DBNAME);
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        MiliteFunzioneAmb entity = null;

        try { // prova ad eseguire il codice
            entity = new MiliteFunzioneAmb();

            entity.croce_id = rs.getLong("croce_id");
            entity.funzione_id = rs.getLong("funzione_id");
            entity.milite_id = rs.getLong("milite_id");

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method


    public List<FunzioneAmb> findAllFunzioniByMilite(MiliteAmb militeOld) {
        List<FunzioneAmb> lista = new ArrayList<>();
        String whereTxt = "milite_id=" + militeOld.getId();
        String orderTxt = "funzione_id";
        List<MiliteFunzioneAmb> listaAll = (List<MiliteFunzioneAmb>) super.findAll(DBNAME, whereTxt, orderTxt);

        for (MiliteFunzioneAmb milFunz : listaAll) {
            lista.add(funzioneAmb.findByID(milFunz.funzione_id));
        }// end of for cycle

        return lista;
    }// end of method


//    public static List<MiliteFunzioneAmb> findAllByCroce(CroceAmb company, EntityManager manager) {
//        List<MiliteFunzioneAmb> lista = new ArrayList<>();
//        List<Object> resultlist = null;
//        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
//        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
//        Root<MiliteFunzioneAmb> from = criteriaQuery.from(MiliteFunzioneAmb.class);
//        criteriaQuery.where(criteriaBuilder.equal(from.get("croce"), company));
//        CriteriaQuery<Object> select = criteriaQuery.select(from);
//        TypedQuery<Object> typedQuery = manager.createQuery(select);
//        resultlist = typedQuery.getResultList();
//
//        for (Object entity : resultlist) {
//            lista.add((MiliteFunzioneAmb) entity);
//        }// end of for cycle
//
//        return lista;
//    }// end of method


//    public static List<MiliteFunzioneAmb> findAllByMilite(UtenteAmb milite, EntityManager manager) {
//        List<MiliteFunzioneAmb> lista = new ArrayList<>();
//        List<Object> resultlist = null;
//        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
//        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
//        Root<MiliteFunzioneAmb> from = criteriaQuery.from(MiliteFunzioneAmb.class);
//        criteriaQuery.where(criteriaBuilder.equal(from.get("milite_id"), milite.getId()));
//        CriteriaQuery<Object> select = criteriaQuery.select(from);
//        TypedQuery<Object> typedQuery = manager.createQuery(select);
//        resultlist = typedQuery.getResultList();
//
//        for (Object entity : resultlist) {
//            lista.add((MiliteFunzioneAmb) entity);
//        }// end of for cycle
//
//        return lista;
//    }// end of method
//
//
//    public static List<FunzioneAmb> findAllFunzioniByMilite(UtenteAmb milite, EntityManager manager) {
//        List<FunzioneAmb> lista = new ArrayList<>();
//        List<MiliteFunzioneAmb> listaAll = findAllByMilite(milite, manager);
//
//        for (MiliteFunzioneAmb milFunz : findAllByMilite(milite, manager)) {
//            lista.add(FunzioneAmb.find(milFunz.getFunzione_id(),manager));
//        }// end of for cycle
//
//        return lista;
//    }// end of method

}// end of entity class
