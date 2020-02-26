package it.algos.vaadwam.modules.servizio;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 10-ott-2019 21.14.46 <br>
 * <br>
 * Business class. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * NOT annotated with @VaadinSessionScope (sbagliato, perché SpringBoot va in loop iniziale) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_SER)
@Slf4j
@AIScript(sovrascrivibile = false)
public class ServizioService extends WamService {


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (come previsto dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private ServizioRepository repository;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public ServizioService(@Qualifier(TAG_SER) MongoRepository repository) {
        super(repository);
        super.entityClass = Servizio.class;
        this.repository = (ServizioRepository) repository;
    }// end of Spring constructor


    /**
     * Le preferenze standard
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.lastImport = LAST_IMPORT_SERVIZI;
        super.durataLastImport = DURATA_IMPORT_SERVIZI;
        super.eaTempoTypeImport = EATempo.secondi;
    }// end of method


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param croce          di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param code           codice di riferimento (obbligatorio)
     * @param descrizione    (facoltativa, non unica)
     * @param orarioDefinito (obbligatorio, avis, centralino ed extra non ce l'hanno)
     * @param inizio         orario previsto (ore e minuti) di inizio turno (obbligatorio, se orarioDefinito è true)
     * @param fine           orario previsto (ore e minuti) di fine turno (obbligatorio, se orarioDefinito è true)
     * @param visibile       nel tabellone (facoltativo, default true) può essere disabilitato per servizi deprecati
     * @param ripetibile     nella stessa giornata (facoltativo, default false)
     * @param obbligatorie   funzioni obbligatorie del servizio (parametro obbligatorio)
     * @param facoltative    funzioni facoltative del servizio (parametro obbligatorio)
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(
            Croce croce,
            String code,
            String descrizione,
            boolean orarioDefinito,
            LocalTime inizio,
            LocalTime fine,
            boolean visibile,
            boolean ripetibile,
            Set<Funzione> obbligatorie,
            Set<Funzione> facoltative) {
        boolean creata = false;

        if (isMancaByKeyUnica(croce, code)) {
            AEntity entity = save(newEntity(croce, code, descrizione, orarioDefinito, inizio, fine, visibile, ripetibile, obbligatorie,facoltative));
            creata = entity != null;
        }// end of if cycle

        return creata;
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Servizio newEntity() {
        return newEntity((Croce) null, 0, "", "", true, (LocalTime) null, (LocalTime) null, true, false, (Set<Funzione>) null, (Set<Funzione>) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     *
     * @param croce          di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param code           codice di riferimento (obbligatorio)
     * @param descrizione    (facoltativa, non unica)
     * @param orarioDefinito (obbligatorio, avis, centralino ed extra non ce l'hanno)
     * @param inizio         orario previsto (ore e minuti) di inizio turno (obbligatorio, se orarioDefinito è true)
     * @param fine           orario previsto (ore e minuti) di fine turno (obbligatorio, se orarioDefinito è true)
     * @param visibile       nel tabellone (facoltativo, default true) può essere disabilitato per servizi deprecati
     * @param ripetibile     nella stessa giornata (facoltativo, default false)
     * @param obbligatorie   funzioni obbligatorie del servizio (parametro obbligatorio)
     * @param facoltative    funzioni facoltative del servizio (parametro obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Servizio newEntity(
            Croce croce,
            String code,
            String descrizione,
            boolean orarioDefinito,
            LocalTime inizio,
            LocalTime fine,
            boolean visibile,
            boolean ripetibile,
            Set<Funzione> obbligatorie,
            Set<Funzione> facoltative) {
        return newEntity(croce, 0, code, descrizione, orarioDefinito, inizio, fine, visibile, ripetibile, obbligatorie, facoltative);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param croce          di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param ordine         di presentazione nelle liste (obbligatorio, unico nella croce,
     *                       con controllo automatico se è zero,  modificabile da developer ed admin)
     * @param code           codice di riferimento (obbligatorio)
     * @param descrizione    (facoltativa, non unica)
     * @param orarioDefinito (obbligatorio, avis, centralino ed extra non ce l'hanno)
     * @param inizio         orario previsto (ore e minuti) di inizio turno (obbligatorio, se orarioDefinito è true)
     * @param fine           orario previsto (ore e minuti) di fine turno (obbligatorio, se orarioDefinito è true)
     * @param visibile       nel tabellone (facoltativo, default true) può essere disabilitato per servizi deprecati
     * @param ripetibile     nella stessa giornata (facoltativo, default false)
     * @param obbligatorie   funzioni obbligatorie del servizio (parametro obbligatorio)
     * @param facoltative    funzioni facoltative del servizio (parametro obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Servizio newEntity(
            Croce croce,
            int ordine,
            String code,
            String descrizione,
            boolean orarioDefinito,
            LocalTime inizio,
            LocalTime fine,
            boolean visibile,
            boolean ripetibile,
            Set<Funzione> obbligatorie,
            Set<Funzione> facoltative) {
        Servizio entity = Servizio.builderServizio()
                .ordine(ordine != 0 ? ordine : this.getNewOrdine(croce))
                .code(text.isValid(code) ? code : null)
                .descrizione(text.isValid(descrizione) ? descrizione : null)
                .orarioDefinito(orarioDefinito)
                .inizio(inizio)
                .fine(fine)
                .visibile(visibile)
                .ripetibile(ripetibile)
                .obbligatorie(obbligatorie)
                .facoltative(facoltative)
                .build();

        return (Servizio) super.addCroce(entity, croce);
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, EDIT)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        Servizio entity = (Servizio) super.beforeSave(entityBean, operation);

        if (text.isValid(entity.descrizione)) {
            entity.descrizione = text.primaMaiuscola(entity.descrizione);
        }// end of if cycle

        if (text.isEmpty(entity.code)) {
            entity = null;
        }// end of if cycle

//        //--property (ridondante) calcolata
//        entity.durataTeorica = getDurata(entity);

        //--elimina informazioni inutili dalla lista (embedded) di funzioni dipendenti
        if (entity.funzioni != null) {
            for (Funzione funz : entity.funzioni) {
                funz.croce = null;
                funz.dipendenti = null;
                funz.descrizione = null;
            }// end of for cycle
        }// end of if cycle

        if (entity.getCroce() == null) {
            log.warn("Non sono riuscito a registrare il servizio " + entity.code + " perché manca la croce");
            entity = null;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Servizio findById(String id) {
        return (Servizio) super.findById(id);
    }// end of method


    /**
     * Opportunità di controllare (per le nuove schede) che una entity con la keyUnica indicata non esista già <br>
     * Invocato appena prima del save(), solo per una nuova entity <br>
     *
     * @param croce di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param code  di riferimento (obbligatoria ed unica)
     *
     * @return true se la entity con la keyUnica indicata non esiste
     */
    public boolean isMancaByKeyUnica(Croce croce, String code) {
        return repository.findByCroceAndCode(croce, code) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Servizio findByKeyUnica(String code) {
        return repository.findByCroceAndCode(getCroce(), code);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param croce di appartenenza (obbligatoria)
     * @param code  di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Servizio findByKeyUnica(Croce croce, String code) {
        return repository.findByCroceAndCode(croce, code);
    }// end of method


    /**
     * Property unica (se esiste).
     */
    public String getPropertyUnica(AEntity entityBean) {
        return ((Servizio) entityBean).getCode();
    }// end of method


    /**
     * Importazione di dati <br>
     *
     * @return informazioni sul risultato
     */
    @Override
    public void importa(Croce croce) {
        long inizio = System.currentTimeMillis();
        migration.importServizi(croce);
        setLastImport(croce, inizio);
    }// end of method


    /**
     * Returns instances of the company <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Servizio> findAll() {
        List<Servizio> items = null;
        Croce croce = getCroce();
        wamLogin = getWamLogin();

        if (croce != null) {
            items = findAllByCroce(croce);
        } else {
            if (wamLogin != null && wamLogin.isDeveloper()) {
                items = findAllCroci();
            }// end of if cycle
        }// end of if/else cycle

        return items;
    }// end of method


    /**
     * Returns instances of the company <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Servizio> findAllCroci() {
        return repository.findAll();
    }// end of method


    /**
     * Returns instances of the company <br>
     * Lista ordinata <br>
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Servizio> findAllByCroce(Croce croce) {
        return repository.findAllByCroceOrderByOrdineAsc(croce);
    }// end of method


    /**
     * Returns the number of entities available for the current company
     *
     * @param croce di appartenenza (obbligatoria)
     *
     * @return the number of entities
     */
    public int countByCroce(Croce croce) {
        return repository.countByCroce(croce);
    }// end of method


//    private AEntity riordinaFunzioni(AEntity entityBean) {
//        Servizio servizio = (Servizio) entityBean;
//        List<Funzione> funzioni = servizio.getFunzioni();
//        int pos = 0;
//
//        if (funzioni != null) {
//            for (Funzione funz : funzioni) {
//                pos++;
//                funz.setOrdine(pos);
//            }// end of for cycle
//        }// end of if cycle
//
//        return servizio;
//    }// end of method


    /**
     * Durata del servizio in ore <br>
     */
    public int getDurata(Servizio entityBean) {
        LocalTime inizio = entityBean.getInizio();
        LocalTime fine = entityBean.getFine();

        return date.differenza(fine, inizio);
    }// end of method


//    /**
//     * Orario di inizio del servizio <br>
//     */
//    public String getOrarioInizio(Servizio entityBean) {
//        return entityBean.oraInizio + ":" + text.format2(entityBean.minutiInizio);
//    }// end of method
//
//
//    /**
//     * Orario di fine del servizio <br>
//     */
//    public String getOrarioFine(Servizio entityBean) {
//        return entityBean.oraFine + ":" + text.format2(entityBean.minutiFine);
//    }// end of method


    /**
     * Descrizione del servizio <br>
     * Va a capo ogni parola <br>
     */
    @Deprecated
    public String getSigla(Servizio entityBean) {
        String testo = entityBean.getDescrizione();

        testo = testo.replaceAll(" ", "\n");

        return testo;
    }// end of method


    /**
     * Descrizione del servizio <br>
     * Lista di righe: una per ogni singola parola <br>
     */
    @Deprecated
    public List<String> getRigheSigla(Servizio entityBean) {
        ArrayList<String> lista = new ArrayList<>();
        String[] righe;
        String testo = entityBean.getDescrizione();

        righe = testo.split(" ");

        return Arrays.asList(righe);
    }// end of method


    /**
     * Orario completo del servizio (inizio e fine) <br>
     * Nella forma '8:00 - 12:30' <br>
     */
    public String getOrario(Servizio entityBean) {
        String orario = "";
        String tagVuoto = "...";
        int oraInizio = entityBean.inizio != null ? entityBean.inizio.getHour() : 0;
        int oraFine = entityBean.fine != null ? entityBean.fine.getHour() : 0;

        if (oraInizio > 0 || oraFine > 0) {
            orario = oraInizio + " - " + oraFine;
        } else {
            orario = tagVuoto;
        }// end of if/else cycle

        return orario;
    }// end of method


//    /**
//     * Orario completo del servizio (inizio e fine) <br>
//     * Nella forma 'ore 8:00 - 12:30' <br>
//     */
//    public String getOre(Servizio entityBean) {
//        return getOrarioInizio(entityBean) + "-" + getOrarioFine(entityBean);
//    }// end of method
//
//
//    /**
//     * Orario completo del servizio (inizio e fine) <br>
//     * Nella forma 'Dalle 8:00 alle 12:30' <br>
//     */
//    public String getIntervallo(Servizio entityBean) {
//        return "Dalle " + getOrarioInizio(entityBean) + " alle " + getOrarioFine(entityBean);
//    }// end of method


    /**
     * Lista di sigle delle funzioni di questo servizio <br>
     */
    @Deprecated
    public List<String> getSigleFunzioni(Servizio entityBean) {
        List<String> sigleFunzioni = new ArrayList<>();
        List<Funzione> funzioni = entityBean.getFunzioni();

        for (Funzione funz : funzioni) {
            sigleFunzioni.add(funz.getCode());
        }// end of for cycle

        return sigleFunzioni;
    }// end of method


    /**
     * Durata di un periodo in ore <br>
     */
    public String durata(AEntity entityBean) {
        return "" + date.differenza(((Servizio) entityBean).fine, ((Servizio) entityBean).inizio);
    }// end of method

}// end of class