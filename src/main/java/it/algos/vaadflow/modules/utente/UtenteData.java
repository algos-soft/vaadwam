package it.algos.vaadflow.modules.utente;

import it.algos.vaadflow.backend.data.ADataService;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.modules.utente.EAUtente;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.service.AAnnotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static it.algos.vaadflow.application.FlowCost.TAG_UTE;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 25-ott-2018
 * Time: 20:16
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_UTE)
@Slf4j
public class UtenteData extends ADataService {


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected RoleService role;

    /**
     * Costruttore @Autowired <br>
     * Regola nella superclasse il modello-dati specifico <br>
     */
    public UtenteData() {
        super();
        super.entityClass = Utente.class;
    }// end of Spring constructor


    /**
     * Crea una entity e la registra <br>
     *
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            Ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     * @param mail             posta elettronica (facoltativo)
     * @param locked           flag locked (facoltativo, di default false)
     *
     * @return la entity appena creata
     */
    public Utente crea(String userName, String passwordInChiaro, Set<Role> ruoli, String mail, boolean locked) {
        Utente entity = newEntity(userName, passwordInChiaro, ruoli, mail, locked);
        mongo.insert(entity, entityClass);
        return entity;
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param userName         userName o nickName (obbligatorio, unico)
     * @param passwordInChiaro password in chiaro (obbligatoria, non unica)
     *                         con inserimento automatico (prima del 'save') se è nulla
     * @param ruoli            Ruoli attribuiti a questo utente (lista di valori obbligatoria)
     *                         con inserimento del solo ruolo 'user' (prima del 'save') se la lista è nulla
     *                         lista modificabile solo da developer ed admin
     * @param mail             posta elettronica (facoltativo)
     * @param locked           flag locked (facoltativo, di default false)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Utente newEntity(String userName, String passwordInChiaro, Set<Role> ruoli, String mail, boolean locked) {
        Utente entity = Utente.builderUtente()
                .username(text.isValid(userName) ? userName : null)
                .password(text.isValid(passwordInChiaro) ? passwordInChiaro : null)
                .ruoli(ruoli != null ? ruoli : role.getUserRole())
                .mail(text.isValid(mail) ? mail : null)
                .enabled(locked)
                .build();
        entity.id = userName;

        return entity;
    }// end of method


    /**
     * Creazione della collezione
     */
    public int creaAll() {
        int num = 0;
        String userName;
        String passwordInChiaro;
        EARole ruolo;
        Set<Role> ruoli;
        String mail;

        for (EAUtente utente : EAUtente.values()) {
            userName = utente.getUsername();
            passwordInChiaro = utente.getPassword();
            ruolo = utente.getRuolo();
            ruoli = role.getRoles(ruolo);
            mail = utente.getMail();

            this.crea(userName, passwordInChiaro, ruoli, mail, false);
            num++;
        }// end of for cycle

        return num;
    }// end of method

}// end of class
