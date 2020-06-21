package it.algos.vaadwam.migration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.ADateService;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by gac on 11 ott 2016.
 * <p>
 * Entity per un turno
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "turno")
@Access(AccessType.PROPERTY)
@Data
@EqualsAndHashCode(callSuper = false)
@ReadOnly
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class TurnoAmb extends AmbEntity {

    private final static String DBNAME = "turno";

    @Autowired
    private ADateService date;

    private long id; //non usato

    private long version; //non usato

    private boolean assegnato = false;

    private long croce_id;

    private Timestamp fine;

    private long funzione1_id;

    private long funzione2_id;

    private long funzione3_id;

    private long funzione4_id;

    private Timestamp giorno;

    private Timestamp inizio;

    private String località_extra;

    private long milite_funzione1_id;

    private long milite_funzione2_id;

    private long milite_funzione3_id;

    private long milite_funzione4_id;

    private Timestamp modifica_funzione1;

    private Timestamp modifica_funzione2;

    private Timestamp modifica_funzione3;

    private Timestamp modifica_funzione4;

    private String note;

    private int ore_milite1;

    private int ore_milite2;

    private int ore_milite3;

    private int ore_milite4;

    private boolean problemi_funzione1;

    private boolean problemi_funzione2;

    private boolean problemi_funzione3;

    private boolean problemi_funzione4;

    private long tipo_turno_id;

    private String titolo_extra;


    public TurnoAmb findByID(long keyID) {
        return (TurnoAmb) super.findByID(DBNAME, keyID);
    }// end of method


    public List<TurnoAmb> findAll(int codeCroce) {
        return (List<TurnoAmb>) super.findAll(DBNAME, codeCroce);
    }// end of method


    //    public List<TurnoAmb> findAll(int codeCroce, int anno) {
//        long max = 10000000000L;
//        long giornoIni = anno * max;
//        long giornoFine = (anno + 1) * max;
//        String where = " croce_id=" + codeCroce;
//        where += " and giorno>" + giornoIni;
//        where += " and giorno<" + giornoFine;
//
//        return (List<TurnoAmb>) super.findAll(DBNAME, codeCroce, where);
//    }// end of method
    public List<TurnoAmb> findAll(int codeCroce, int anno) {
        return findAll(codeCroce, date.primoGennaio(anno), date.trentunDicembre(anno));
    }// end of method


    public List<TurnoAmb> findAll(int codeCroce, LocalDate inzio, LocalDate fine) {
        String where = " croce_id=" + codeCroce;
        where += " and giorno>=" + SEP + inzio + SEP;
        where += " and giorno<=" + SEP + fine + SEP;

        return (List<TurnoAmb>) super.findAll(DBNAME, where);
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        TurnoAmb entity = null;

        try { // prova ad eseguire il codice
            entity = new TurnoAmb();

            entity.id = (rs.getLong("id"));
            entity.version = rs.getLong("version");
//            entity.assegnato = rs.getBoolean("assegnato");
            entity.croce_id = rs.getLong("croce_id");
            entity.fine = rs.getTimestamp("fine");
            entity.funzione1_id = rs.getLong("funzione1_id");
            entity.funzione2_id = rs.getLong("funzione2_id");
            entity.funzione3_id = rs.getLong("funzione3_id");
            entity.funzione4_id = rs.getLong("funzione4_id");
            entity.giorno = rs.getTimestamp("giorno");
            entity.inizio = rs.getTimestamp("inizio");
            entity.località_extra = rs.getString("località_extra");
            entity.milite_funzione1_id = rs.getLong("milite_funzione1_id");
            entity.milite_funzione2_id = rs.getLong("milite_funzione2_id");
            entity.milite_funzione3_id = rs.getLong("milite_funzione3_id");
            entity.milite_funzione4_id = rs.getLong("milite_funzione4_id");
            entity.modifica_funzione1 = rs.getTimestamp("modifica_funzione1");
            entity.modifica_funzione2 = rs.getTimestamp("modifica_funzione2");
            entity.modifica_funzione3 = rs.getTimestamp("modifica_funzione3");
            entity.modifica_funzione4 = rs.getTimestamp("modifica_funzione4");
            entity.note = rs.getString("note");
            entity.ore_milite1 = rs.getInt("ore_milite1");
            entity.ore_milite2 = rs.getInt("ore_milite2");
            entity.ore_milite3 = rs.getInt("ore_milite3");
            entity.ore_milite4 = rs.getInt("ore_milite4");
            entity.problemi_funzione1 = rs.getBoolean("problemi_funzione1");
            entity.problemi_funzione2 = rs.getBoolean("problemi_funzione2");
            entity.problemi_funzione3 = rs.getBoolean("problemi_funzione3");
            entity.problemi_funzione4 = rs.getBoolean("problemi_funzione4");
            entity.tipo_turno_id = rs.getLong("tipo_turno_id");
            entity.titolo_extra = rs.getString("titolo_extra");

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method


}// end of entity class
