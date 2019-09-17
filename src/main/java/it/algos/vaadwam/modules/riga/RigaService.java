package it.algos.vaadwam.modules.riga;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.service.AService;
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
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_RIG;

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
@Qualifier(TAG_RIG)
@Slf4j
@AIScript(sovrascrivibile = false)
public class RigaService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public RigaRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public RigaService(@Qualifier(TAG_RIG) MongoRepository repository) {
        super(repository);
        super.entityClass = Riga.class;
        this.repository = (RigaRepository) repository;
    }// end of Spring constructor


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @param context della sessione
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Riga newEntity(AContext context) {
        return newEntity((LocalDate) null, (Servizio) null, (List<Turno>) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param giornoIniziale di riferimento (obbligatorio)
     * @param servizio       di riferimento (obbligatorio)
     * @param turni          previsti/effettuati (facoltativi, di norma 7)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Riga newEntity(LocalDate giornoIniziale, Servizio servizio, List<Turno> turni) {
        Riga  entity = Riga.builderRiga()
                .giornoIniziale(giornoIniziale != null ? giornoIniziale : LocalDate.now())
                .servizio(servizio)
                .turni(turni)
                .build();

        return entity;
    }// end of method


    public Turno getTurno(Riga riga, LocalDate giorno) {
        Turno turno = null;

        if (riga != null && array.isValid(riga.turni)) {
            for (Turno turnoTmp : riga.turni) {
                if (turnoTmp.giorno.equals(giorno)) {
                    turno = turnoTmp;
                }// end of if cycle
            }// end of for cycle

        }// end of if cycle

        return turno;
    }// end of method

}// end of class