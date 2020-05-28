package it.algos.vaadwam.modules.log;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.service.AService;
import it.algos.vaadwam.enumeration.EAWamLogType;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.wam.WamLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.KEY_CONTEXT;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.TAG_WAM_LOG;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 27-mag-2020
 * Time: 07:32
 */
@SpringComponent
@Qualifier(TAG_WAM_LOG)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovrascrivibile = false)
public class WamLogService extends AService {


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public WamLogService(@Qualifier(TAG_WAM_LOG) MongoRepository repository) {
        super(repository);
        super.entityClass = WamLog.class;
        this.repository = (WamLogRepository) repository;
    }// end of Spring constructor


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     * La data dell'evento (obbligatoria, non modificabile) viene aggiunta direttamente
     *
     * @param croce       (obbligatoria, non modificabile)
     * @param type        raggruppamento logico dei log per type di eventi (obbligatorio, non modificabile)
     * @param milite      (facoltativo, se manca è un log di sistema, non modificabile)
     * @param descrizione (facoltativa, modificabile)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public WamLog newEntity(Croce croce, EAWamLogType type, Milite milite, String descrizione) {
        return WamLog.builderWamlog()

                .croce(croce)

                .type(type)

                .milite(milite)

                .evento(LocalDateTime.now())

                .descrizione(descrizione)

                .build();
    }


    /**
     * Costruisce una lista di nomi delle properties della Grid nell'ordine:
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica
     * todo ancora da sviluppare
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getGridPropertyNamesList(AContext context) {
        List<String> lista = null;

        if (context.getLogin() != null && context.getLogin().isDeveloper()) {
            lista = array.getList("id,croce,milite,type,evento,descrizione");
        } else {
            if (context.getLogin().isAdmin()) {
                lista = array.getList("milite,type,evento,descrizione");
            }
        }

        return lista;
    }// end of method


    public void nuovoMilite() {
        log(EAWamLogType.nuovoMilite);
    }


    public void modificaMilite() {
        log(EAWamLogType.modificaMilite);
    }


    public void modificaMilite(String message) {
        log(EAWamLogType.modificaMilite, message);
    }


    public void log(EAWamLogType type) {
        log(type, VUOTA);
    }


    public void log(EAWamLogType type, String message) {
        WamLog wamLog = null;
        Croce croce = getCroce();
        Milite militeLoggato = getMiliteLoggato();

        wamLog = newEntity(croce, type, militeLoggato, message);
        wamLog.id = croce.code + System.currentTimeMillis();
        mongo.update(wamLog, WamLog.class);
    }


    public WamLogin getWamLogin() {
        WamLogin wamLogin = null;
        AContext context = null;
        VaadinSession vaadSession = UI.getCurrent().getSession();

        if (vaadSession != null) {
            context = (AContext) vaadSession.getAttribute(KEY_CONTEXT);
        }// end of if cycle

        if (context != null && context.getLogin() != null) {
            wamLogin = (WamLogin) context.getLogin();
        }// end of if cycle

        return wamLogin;
    }


    public Milite getMiliteLoggato() {
        Milite militeLoggato = null;
        WamLogin wamLogin = getWamLogin();

        if (wamLogin != null) {
            militeLoggato = wamLogin.getMilite();
        }

        return militeLoggato;
    }


    public Croce getCroce() {
        Croce croce = null;
        WamLogin wamLogin = getWamLogin();

        if (wamLogin != null) {
            croce = wamLogin.getCroce();
        }

        return croce;
    }

}
