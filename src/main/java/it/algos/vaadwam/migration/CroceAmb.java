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
@Table(name = "Croce")
@Access(AccessType.PROPERTY)
@Data
@EqualsAndHashCode(callSuper = false)
@ReadOnly
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class CroceAmb extends AmbEntity {

    private long id; //non usato

    private long version; //non usato

    private String descrizione;

    private String email;

    private String indirizzo;

    private String note;

    private String riferimento;

    private long settings_id;

    private String sigla;

    private String telefono;

    private String amministratori;

    private String custode;

    private String presidente;

    private String organizzazione;


    public CroceAmb findBySigla(String sigla) {
        CroceAmb croce = null;
        List<CroceAmb> resultList = findAll();

        if (resultList != null && resultList.size() > 0) {
            for (CroceAmb croceTmp : resultList) {
                if (croceTmp.getSigla().equals(sigla)) {
                    croce = croceTmp;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return croce;
    }// end of method


    public List<CroceAmb> findAll() {
        return (List<CroceAmb>) super.findAll("croce");
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        CroceAmb entity = null;

        try { // prova ad eseguire il codice
            entity = new CroceAmb();

            entity.id = (rs.getLong("id"));
            entity.version = rs.getLong("version");
            entity.descrizione = rs.getString("descrizione");
            entity.email = rs.getString("email");
            entity.indirizzo = rs.getString("indirizzo");
            entity.note = rs.getString("note");
            entity.riferimento = rs.getString("riferimento");
            entity.settings_id = rs.getLong("settings_id");
            entity.sigla = rs.getString("sigla");
            entity.telefono = rs.getString("telefono");
            entity.amministratori = rs.getString("amministratori");
            entity.custode = rs.getString("custode");
            entity.presidente = rs.getString("presidente");
            entity.organizzazione = rs.getString("organizzazione");

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method

}// end of entity class
