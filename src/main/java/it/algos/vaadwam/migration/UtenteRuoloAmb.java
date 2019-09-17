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
 * Date: dom, 01-lug-2018
 * Time: 07:48
 */
@Entity
@Table(name = "utente_ruolo")
@Access(AccessType.PROPERTY)
@Data
@EqualsAndHashCode(callSuper = false)
@ReadOnly
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UtenteRuoloAmb extends AmbEntity {

    private final static String DBNAME = "utente_ruolo";

    private long ruolo_id;

    private long utente_id;


    public List<UtenteRuoloAmb> findAll() {
        return (List<UtenteRuoloAmb>) super.findAll(DBNAME);
    }// end of method


    public boolean isAdmin(UtenteAmb utenteOld) {
        int codRuoloAdmin = 3;
        boolean isAdmin = false;
        String whereTxt = "ruolo_id=" + codRuoloAdmin + " and utente_id=" + utenteOld.getId();
        List<UtenteRuoloAmb> listaAll = (List<UtenteRuoloAmb>) super.findAll(DBNAME, whereTxt);

        if (listaAll != null && listaAll.size() == 1) {
            isAdmin = true;
        }// end of if cycle

        return isAdmin;
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        UtenteRuoloAmb entity = null;

        try { // prova ad eseguire il codice
            entity = new UtenteRuoloAmb();

            entity.ruolo_id = rs.getLong("ruolo_id");
            entity.utente_id = rs.getLong("utente_id");

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method


}// end of class
