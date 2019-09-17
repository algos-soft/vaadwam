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
 * Time: 09:16
 */
@Entity
@Table(name = "ruolo")
@Access(AccessType.PROPERTY)
@Data
@EqualsAndHashCode(callSuper = false)
@ReadOnly
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class RuoloAmb extends AmbEntity {

    private final static String DBNAME = "ruolo";

    private long id; //non usato

    private long version; //non usato

    private String authority;


    public List<RuoloAmb> findAll() {
        return (List<RuoloAmb>) super.findAll(DBNAME);
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        RuoloAmb entity = null;

        try { // prova ad eseguire il codice
            entity = new RuoloAmb();

            entity.id = (rs.getLong("id"));
            entity.version = rs.getLong("version");
            entity.authority = rs.getString("authority");

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method


}// end of class
