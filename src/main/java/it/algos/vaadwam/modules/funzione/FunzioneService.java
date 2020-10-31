package it.algos.vaadwam.modules.funzione;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.wam.WamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.File;
import java.util.*;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 10-ott-2019 21.14.36 <br>
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
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_FUN)
@Slf4j
@AIScript(sovrascrivibile = false)
public class FunzioneService extends WamService {


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (come previsto dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private FunzioneRepository repository;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public FunzioneService(@Qualifier(TAG_FUN) MongoRepository repository) {
        super(repository);
        super.entityClass = Funzione.class;
        this.repository = (FunzioneRepository) repository;
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

        super.lastImport = LAST_IMPORT_FUNZIONI;
        super.durataLastImport = DURATA_IMPORT_FUNZIONI;
        super.eaTempoTypeImport = EATempo.secondi;
    }// end of method


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param croce       di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param code        di codifica interna specifica per ogni croce (obbligatorio, unico nella croce)
     * @param sigla       di codifica visibile (obbligatoria, non unica)
     * @param descrizione completa (obbligatoria, non unica)
     * @param icona       icona di tipo VaadinIcons (facoltativa)
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(Croce croce, String code, String sigla, String descrizione, VaadinIcon icona) throws Exception {
        boolean creata = false;

        if (isMancaByKeyUnica(croce, code)) {
            AEntity entity = save(newEntity(croce, code, sigla, descrizione, icona));
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
    public Funzione newEntity() {
        return newEntity((Croce) null, 0, "", "", "", (VaadinIcon) null, (Set<Funzione>) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     *
     * @param croce       di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param code        di codifica interna specifica per ogni croce (obbligatorio, unico nella croce)
     * @param sigla       di codifica visibile (obbligatoria, non unica)
     * @param descrizione completa (obbligatoria, non unica)
     * @param icona       icona di tipo VaadinIcons (facoltativa)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Funzione newEntity(Croce croce, String code, String sigla, String descrizione, VaadinIcon icona) throws Exception {
        return newEntity(croce, 0, code, sigla, descrizione, icona, (Set<Funzione>) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param croce       di appartenenza (obbligatoria, se manca viene recuperata dal login)
     * @param ordine      di presentazione nelle liste (obbligatorio, unico nella croce,
     *                    con controllo automatico se è zero,  modificabile da developer ed admin)
     * @param code        di codifica interna specifica per ogni croce (obbligatorio, unico nella croce)
     * @param sigla       di codifica visibile (obbligatoria, non unica)
     * @param descrizione completa (obbligatoria, non unica)
     * @param icona       icona di tipo VaadinIcons (facoltativa)
     * @param dipendenti  funzioni dipendenti che vengono automaticamente abilitate quando il militi è abilitato per questa funzione
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Funzione newEntity(Croce croce, int ordine, String code, String sigla, String descrizione, VaadinIcon icona, Set<Funzione> dipendenti) {
        Funzione entity = Funzione.builderFunzione().ordine(ordine != 0 ? ordine : this.getNewOrdine(croce)).code(text.isValid(code) ? code : null).sigla(text.isValid(sigla) ? sigla : null).descrizione(text.isValid(descrizione) ? descrizione : null).icona(icona).dipendenti(dipendenti).build();

        return (Funzione) super.addCroce(entity, croce);

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
        Funzione entity = (Funzione) super.beforeSave(entityBean, operation);

        if (text.isValid(entity.descrizione)) {
            entity.descrizione = text.primaMaiuscola(entity.descrizione);
        }// end of if cycle

        if (text.isEmpty(entity.code)) {
            if (text.isValid(entity.sigla)) {
                entity.code = entity.sigla;
            } else {
                entity = null;
            }
        }// end of if cycle

        if (entity.getCroce() == null) {
            log.warn("Non sono riuscito a registrare la funzione " + entity.sigla + " perché manca la croce");
            entity = null;
        }// end of if cycle

        //--elimina informazioni inutili dalla lista (embedded) di funzioni dipendenti
        if (entity.dipendenti != null) {
            for (Funzione funz : entity.dipendenti) {
                funz.croce = null;
                funz.dipendenti = null;
            }// end of for cycle
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
    public Funzione findById(String id) {
        return (Funzione) super.findById(id);
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
    public Funzione findByKeyUnica(String code) {
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
    public Funzione findByKeyUnica(Croce croce, String code) {
        return repository.findByCroceAndCode(croce, code);
    }// end of method


    /**
     * Property unica (se esiste).
     */
    public String getPropertyUnica(AEntity entityBean) {
        return ((Funzione) entityBean).getCode();
    }// end of method


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
        List<String> lista;

        if (context.getLogin() != null && context.getLogin().isDeveloper()) {
            lista = array.getList("id,ordine,code,icona,sigla,descrizione,dipendenti");
        } else {
            lista = array.getList("sigla,descrizione,dipendenti");
        }

        return lista;
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Form nell'ordine:
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     * todo ancora da sviluppare
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getFormPropertyNamesList(AContext context) {
        List<String> lista;

        if (context.getLogin() != null && context.getLogin().isDeveloper()) {
            lista = array.getList("id,ordine,code,icona,sigla,descrizione,dipendenti");
        } else {
            lista = array.getList("sigla,descrizione,dipendenti");
        }

        return lista;
    }


    /**
     * Importazione di dati <br>
     *
     * @return informazioni sul risultato
     */
    @Override
    public boolean importa(Croce croce) {
        boolean eseguito;

        long inizio = System.currentTimeMillis();
        eseguito = migration.importFunzioni(croce);
        setLastImport(croce, inizio);

        return eseguito;
    }// end of method


    /**
     * Returns instances of the company <br>
     * Lista ordinata <br>
     *
     * @return lista ordinata di tutte le entities
     */
    public List<Funzione> findAll() {
        List<Funzione> items = null;
        Croce croce = getCroce();

        if (croce != null) {
            items = findAllByCroce(croce);
        } else {
            if (getWamLogin() != null && getWamLogin().isDeveloper()) {
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
    public List<Funzione> findAllCroci() {
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
    public List<Funzione> findAllByCroce(Croce croce) {
        return repository.findAllByCroceOrderByOrdineAsc(croce);
    }// end of method


    /**
     * @return lista di code
     */
    public List<String> findAllCode() {
        return findAllCode(getCroce());
    }// end of method


    /**
     * @return lista di code
     */
    public List<String> findAllCode(Croce croce) {
        List lista = new ArrayList();
        List<Funzione> listaFunz = findAllByCroce(croce);

        for (Funzione funz : listaFunz) {
            lista.add(funz.getCode());
        }// end of for cycle

        return lista;
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


    /**
     * Costruisce una lista ordinata di funzioni dipendenti <br>
     * Le funzioni sono memorizzate come Set <br>
     * La lista risultante è composta dalle funzioni originarie e non da quelle embedded nella funzione stessa <br>
     * Nella lista risultante, vengono ordinate secondo la property 'ordine' <br>
     *
     * @return lista ordinata di funzioni dipendenti
     */
    public List<Funzione> getDipendenti(Funzione funzione) {
        List<Funzione> listaDipendenti = null;
        List<Funzione> listaAll = null;
        Set<Funzione> set;
        Croce croce;
        List<String> listaIdFunzioni = null;

        if (funzione != null) {
            croce = funzione.getCroce();
            set = funzione.dipendenti;

            if (croce != null) {
                listaAll = findAllByCroce(croce);
            }// end of if cycle

            if (array.isValid(listaAll) && set != null) {
                listaIdFunzioni = getIdsFunzioni(set);
                listaDipendenti = new ArrayList<>();

                for (Funzione funz : listaAll) {
                    if (listaIdFunzioni.contains(funz.id)) {
                        listaDipendenti.add(funz);
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

        return listaDipendenti;
    }// end of method


    /**
     * Trasforma un Set in una List (ordinata) <br>
     * Lista di ID ordinati (secondo il parametro 'ordine' della Funzione) delle funzioni di un set <br>
     *
     * @return lista IDs delle funzioni del set
     */
    public List<String> getIdsFunzioni(Set<Funzione> set) {
        List<String> listaIdsFunzioni = null;
        HashMap<Integer, String> mappa = null;
        List<Integer> lista;

        if (set != null && set.size() > 0) {
            mappa = new HashMap<>();

            for (Funzione funz : set) {
                mappa.put(funz.ordine, funz.id);
            }// end of for cycle
        }// end of if cycle

        if (mappa != null) {
            listaIdsFunzioni = new ArrayList<>();
            lista = new ArrayList<Integer>(mappa.keySet());
            Collections.sort(lista);

            for (Integer key : lista) {
                listaIdsFunzioni.add(mappa.get(key));
            }// end of for cycle
        }// end of if cycle

        return listaIdsFunzioni;
    }// end of method


    /**
     * Creazione di alcuni dati demo iniziali <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * La collezione viene svuotata <br>
     * I dati possono essere presi da una Enumeration o creati direttamemte <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse
     *
     * @return numero di elementi creato
     */
    @Override
    public int reset() {
        int numRec = super.reset();
        File regioniCSV = new File("config" + File.separator + "funzioni");
        String path = regioniCSV.getAbsolutePath();
        List<LinkedHashMap<String, String>> mappaCSV;
        String croceTxt = VUOTA;
        Croce croce = null;
        String code = VUOTA;
        String sigla = VUOTA;
        String descrizione = VUOTA;
        String iconaTxt = VUOTA;
        VaadinIcon icona = null;

        mappaCSV = fileService.leggeMappaCSV(path);
        for (LinkedHashMap<String, String> riga : mappaCSV) {
            croceTxt = riga.get("croce");
            croce = text.isValid(croceTxt) ? croceService.findByKeyUnica(croceTxt) : null;
            code = riga.get("code");
            sigla = riga.get("sigla");
            descrizione = riga.get("descrizione");
            iconaTxt = riga.get("icona");
            icona = text.isValid(iconaTxt) ? VaadinIcon.valueOf(iconaTxt) : null;

            try {
                creaIfNotExist(croce, code, sigla, descrizione, icona);
            } catch (Exception unErrore) {
                log.error(unErrore.getMessage());
            }
        }

        return numRec;
    }// end of method

}// end of class