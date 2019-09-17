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
 * Time: 07:38
 */
@Entity
@Table(name = "utente")
@Access(AccessType.PROPERTY)
@Data
@EqualsAndHashCode(callSuper = false)
@ReadOnly
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UtenteAmb extends AmbEntity {

    private final static String DBNAME = "utente";

    private long id; //non usato

    private long version; //non usato

    private boolean account_expired;

    private boolean account_locked;

    private long croce_id;

    private boolean enabled;

    private long milite_id;

    private String pass;

    private String password;

    private boolean password_expired;

    private String username;

    private String nickname;


    public UtenteAmb findByID(long keyID) {
        return (UtenteAmb) super.findByID(DBNAME, keyID);
    }// end of method


    public UtenteAmb findByMilite(long militeID) {
        String whereText = " milite_id=" + militeID;
        return (UtenteAmb) super.find(DBNAME, whereText);
    }// end of method


    public List<UtenteAmb> findAll(int codeCroce) {
        return (List<UtenteAmb>) super.findAll(DBNAME, codeCroce);
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        UtenteAmb entity = null;

        try { // prova ad eseguire il codice
            entity = new UtenteAmb();

            entity.id = (rs.getLong("id"));
            entity.version = rs.getLong("version");
            entity.account_expired = rs.getBoolean("account_expired");
            entity.account_locked = rs.getBoolean("account_locked");
            entity.croce_id = rs.getLong("croce_id");
            entity.enabled = rs.getBoolean("enabled");
            entity.milite_id = rs.getLong("milite_id");
            entity.pass = rs.getString("pass");
            entity.password = rs.getString("password");
            entity.password_expired = rs.getBoolean("password_expired");
            entity.username = rs.getString("username");
            entity.nickname = rs.getString("nickname");

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method


}// end of class
