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
import java.util.Date;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 30-giu-2018
 * Time: 19:36
 */
@Entity
@Table(name = "milite")
@Access(AccessType.PROPERTY)
@Data
@EqualsAndHashCode(callSuper = false)
@ReadOnly
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class MiliteAmb extends AmbEntity {

    private final static String DBNAME = "milite";

    private long id; //non usato

    private long version; //non usato

    private boolean attivo;

    private String cognome;

    private long croce_id;

    private Date data_nascita;

    private boolean dipendente;

    private String email;

    private String nome;

    private String note;

    private int ore_anno;

    private Date scadenzablsd;

    private Date scadenza_non_trauma;

    private Date scadenza_trauma;

    private String telefono_cellulare;

    private String telefono_fisso;

    private int turni_anno;

    private int ore_extra;


    public MiliteAmb findByID(long keyID) {
        return (MiliteAmb) super.findByID(DBNAME, keyID);
    }// end of method


    public List<MiliteAmb> findAll(int codeCroce) {
        return (List<MiliteAmb>) super.findAll(DBNAME, codeCroce, "cognome");
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        MiliteAmb entity = null;

        try { // prova ad eseguire il codice
            entity = new MiliteAmb();

            entity.id = (rs.getLong("id"));
            entity.version = rs.getLong("version");
//                entity.attivo = rs.getBoolean("attivo");
            entity.cognome = rs.getString("cognome");
            entity.croce_id = rs.getLong("croce_id");
            entity.data_nascita = rs.getDate("data_nascita");
//            entity.dipendente = rs.getBoolean("dipendente");
            entity.email = rs.getString("email");
            entity.nome = rs.getString("nome");
            entity.note = rs.getString("note");
            entity.ore_anno = rs.getInt("ore_anno");
            entity.scadenzablsd = rs.getDate("scadenzablsd");
            entity.scadenza_non_trauma = rs.getDate("scadenza_non_trauma");
            entity.scadenza_trauma = rs.getDate("scadenza_trauma");
            entity.telefono_cellulare = rs.getString("telefono_cellulare");
            entity.telefono_fisso = rs.getString("telefono_fisso");
            entity.turni_anno = rs.getInt("turni_anno");
            entity.ore_extra = rs.getInt("ore_extra");

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method

}// end of class
