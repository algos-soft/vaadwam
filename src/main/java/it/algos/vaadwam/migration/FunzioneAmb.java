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
import java.util.List;


/**
 * Created by gac on 27 ago 2016.
 * <p>
 * Entity per una funzione
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "Funzione")
@Access(AccessType.PROPERTY)
@Data
@EqualsAndHashCode(callSuper = false)
@ReadOnly
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class FunzioneAmb extends AmbEntity {

    private final static String DBNAME = "funzione";

    //--croce di riferimento
    @Autowired
    private CroceAmb croceService;

    private CroceAmb croce;

    private long id; //non usato

    private long version; //non usato

    private long croce_id;

    private String descrizione;

    private String funzioni_dipendenti;

    private int ordine;

    private String sigla;

    private String sigla_visibile;


    public FunzioneAmb findByID(long keyID) {
        return (FunzioneAmb) super.findByID(DBNAME, keyID);
    }// end of method


    public List<FunzioneAmb> findAll(Integer codeCroce) {
        List<FunzioneAmb> lista = (List<FunzioneAmb>) super.findAll(DBNAME, codeCroce);
        croce = (CroceAmb) croceService.findByID("croce", new Long(codeCroce));

        if (croce != null) {
            for (FunzioneAmb funz : lista) {
                funz.croce = croce;
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        FunzioneAmb entity = null;

        try { // prova ad eseguire il codice
            entity = new FunzioneAmb();

            entity.id = (rs.getLong("id"));
            entity.version = rs.getLong("version");
            entity.croce_id = rs.getInt("croce_id");
            entity.descrizione = rs.getString("descrizione");
            entity.funzioni_dipendenti = rs.getString("funzioni_dipendenti");
            entity.ordine = rs.getInt("ordine");
            entity.sigla = rs.getString("sigla");
            entity.sigla_visibile = rs.getString("sigla_visibile");

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method


}// end of entity class
