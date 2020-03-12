package it.algos.vaadwam.migration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.annotations.ReadOnly;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.ResultSet;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: gio, 31-mag-2018
 * Time: 13:57
 * Entity per un servizio
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "tipo_turno")
@Access(AccessType.PROPERTY)
@Data
@EqualsAndHashCode(callSuper = false)
@ReadOnly
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ServizioAmb extends AmbEntity {


    private final static String DBNAME = "tipo_turno";

    private long id; //non usato

    private long version; //non usato

    private long croce_id;

    private String descrizione;

    private int durata;

    private boolean fine_giorno_successivo;

    private long funzione1_id;

    private long funzione2_id;

    private long funzione3_id;

    private long funzione4_id;

    private int funzioni_obbligatorie;

    private int minuti_fine;

    private int minuti_inizio;

    private boolean multiplo;

    private int ora_fine;

    private int ora_inizio;

    private boolean orario;

    private int ordine;

    private String sigla;

    private boolean visibile;

    private boolean primo;


    public ServizioAmb findByID(long keyID) {
        return (ServizioAmb) super.findByID(DBNAME, keyID);
    }// end of method


    public List<ServizioAmb> findAll(int codeCroce) {
        return (List<ServizioAmb>) super.findAll(DBNAME, codeCroce,"ordine");
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        ServizioAmb entity = null;

        try { // prova ad eseguire il codice
            entity = new ServizioAmb();

            entity.id = (rs.getLong("id"));
            entity.version = rs.getLong("version");
            entity.croce_id = rs.getLong("croce_id");
            entity.descrizione = rs.getString("descrizione");
            entity.durata = rs.getInt("durata");
            entity.fine_giorno_successivo = rs.getBoolean("fine_giorno_successivo");
            entity.funzione1_id = rs.getLong("funzione1_id");
            entity.funzione2_id = rs.getLong("funzione2_id");
            entity.funzione3_id = rs.getLong("funzione3_id");
            entity.funzione4_id = rs.getLong("funzione4_id");
            entity.funzioni_obbligatorie = rs.getInt("funzioni_obbligatorie");
            entity.minuti_fine = rs.getInt("minuti_fine");
            entity.minuti_inizio = rs.getInt("minuti_inizio");
            entity.multiplo = rs.getBoolean("multiplo");
            entity.ora_fine = rs.getInt("ora_fine");
            entity.ora_inizio = rs.getInt("ora_inizio");
            entity.orario = rs.getBoolean("orario");
            entity.ordine = rs.getInt("ordine");
            entity.sigla = rs.getString("sigla");
            entity.visibile = rs.getBoolean("visibile");
            entity.primo = rs.getBoolean("primo");

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method

}// end of entity class
