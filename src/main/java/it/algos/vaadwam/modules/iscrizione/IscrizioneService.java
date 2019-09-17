package it.algos.vaadwam.modules.iscrizione;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.turno.Turno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_ISC;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.22.05 <br>
 * <br>
 * Estende la classe astratta AService. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Service (ridondante) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_ISC)
@Slf4j
@AIScript(sovrascrivibile = false)
public class IscrizioneService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (come previsto dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private IscrizioneRepository repository;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public IscrizioneService(@Qualifier(TAG_ISC) MongoRepository repository) {
        super(repository);
        super.entityClass = Iscrizione.class;
//        this.repository = (IscrizioneRepository) repository;
    }// end of Spring constructor


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Iscrizione newEntity() {
        return newEntity((Funzione) null, (Milite) null, (LocalDateTime) null, 0, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     *
     * @param funzione per cui il milite/volontario/utente si iscrive (obbligatorio)
     * @param durata   effettiva del turno del milite/volontario di questa iscrizione (obbligatorio, proposta come dal servizio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Iscrizione newEntity(Funzione funzione, int durata) {
        return newEntity(funzione, (Milite) null, (LocalDateTime) null, durata, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     *
     * @param funzione     per cui il milite/volontario/utente si iscrive (obbligatorio)
     * @param milite       di riferimento (obbligatorio)
     * @param lastModifica data di creazione/ultima modifica (obbligatorio, inserito in automatico)
     * @param durata       effettiva del turno del milite/volontario di questa iscrizione (obbligatorio, proposta come dal servizio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Iscrizione newEntity(Funzione funzione, Milite milite, LocalDateTime lastModifica, int durata, boolean esisteProblema) {
        Iscrizione entity = Iscrizione.builderIscrizione()
                .funzione(funzione)
                .milite(milite)
                .lastModifica(lastModifica != null ? lastModifica : LocalDateTime.now())
                .durataEffettiva(durata)
                .esisteProblema(esisteProblema)
                .build();

        return entity;
    }// end of method


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     * <p>
     * Controlla se l'applicazione usa le company2 - flag  AlgosApp.USE_MULTI_COMPANY=true
     * Controlla se la collection (table) usa la company2: può essere
     * a)EACompanyRequired.nonUsata
     * b)EACompanyRequired.facoltativa
     * c)EACompanyRequired.obbligatoria
     *
     * @param entityBean da salvare
     *
     * @return the saved entity
     */
    @Override
    public AEntity save(AEntity entityBean) {
        ((Iscrizione) entityBean).setLastModifica(LocalDateTime.now());
        return super.save(entityBean);
    }// end of method


    public Iscrizione getByTurnoAndFunzione(Turno turno, Object funzione) {
        if (funzione != null && funzione instanceof Funzione) {
            return getByTurnoAndFunzione(turno, ((Funzione) funzione));
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    public Iscrizione getByTurnoAndFunzione(Turno turno, Funzione funzione) {
        Iscrizione iscrizione = null;
        List<Iscrizione> iscrizioniDelTurno = turno.iscrizioni;
        Funzione funz;

        if (array.isValid(iscrizioniDelTurno)) {
            for (Iscrizione iscrizioneTmp : iscrizioniDelTurno) {
                funz = iscrizioneTmp.getFunzione();
                if (funz != null) {
                    if (funz.code.equals(funzione.code)) {
                        iscrizione = iscrizioneTmp;
                    }// end of if cycle
                } else {
                    log.error("Manca la funzione nell'iscrizione del turno: " + turno);
                }// end of if/else cycle
            }// end of for cycle
        }// end of if cycle

        return iscrizione;
    }// end of method


    public boolean isValida(Iscrizione iscrizione) {
        boolean status = false;

        if (iscrizione.funzione.obbligatoria) {
            if (iscrizione.milite != null) {
                status = true;
            }// end of if cycle
        } else {
            status = true;
        }// end of if/else cycle

        return status;
    }// end of method


    public boolean isValida(Turno turno, Funzione funzione) {
        boolean status = false;
        Iscrizione iscrizione = getByTurnoAndFunzione(turno, funzione);

        if (iscrizione != null && iscrizione.milite != null) {
            status = true;
        }// end of if cycle

        return status;
    }// end of method

}// end of class