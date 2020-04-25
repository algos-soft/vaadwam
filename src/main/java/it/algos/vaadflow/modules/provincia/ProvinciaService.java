package it.algos.vaadflow.modules.provincia;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.importa.ImportWiki;
import it.algos.vaadflow.modules.regione.Regione;
import it.algos.vaadflow.modules.regione.RegioneService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.wrapper.WrapTreStringhe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_PROVINCIA;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 6-apr-2020 11.35.26 <br>
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
@Qualifier(TAG_PROVINCIA)
@Slf4j
@AIScript(sovrascrivibile = false)
public class ProvinciaService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public ProvinciaRepository repository;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private RegioneService regioneService;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public ProvinciaService(@Qualifier(TAG_PROVINCIA) MongoRepository repository) {
        super(repository);
        super.entityClass = Provincia.class;
        this.repository = (ProvinciaRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param regione (obbligatoria)
     * @param sigla   (obbligatoria, unica)
     * @param nome    (obbligatorio, unico)
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(Regione regione, String sigla, String nome) {
        boolean creata = false;

        if (isMancaByKeyUnica(sigla)) {
            AEntity entity = save(newEntity(0, regione, sigla, nome));
            creata = entity != null;
        }// end of if cycle

        return creata;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param regione (obbligatoria)
     * @param sigla   (obbligatoria, unica)
     * @param nome    (obbligatorio, unico)
     *
     * @return la entity appena creata
     */
    public Provincia crea(Regione regione, String sigla, String nome) {
        return (Provincia) save(newEntity(0, regione, sigla, nome));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Provincia newEntity() {
        return newEntity(0, (Regione) null, VUOTA, VUOTA);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param ordine  di presentazione (obbligatorio con inserimento automatico se è zero)
     * @param regione (obbligatoria)
     * @param sigla   (obbligatoria, unica)
     * @param nome    (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Provincia newEntity(int ordine, Regione regione, String sigla, String nome) {
        Provincia entity = null;

        entity = Provincia.builderProvincia()
                .ordine(ordine > 0 ? ordine : getNewOrdine())
                .sigla(text.isValid(sigla) ? sigla : null)
                .regione(regione)
                .nome(text.isValid(nome) ? nome : null)
                .build();

        return (Provincia) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param sigla (obbligatoria, unica)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Provincia findByKeyUnica(String sigla) {
        return repository.findBySigla(sigla);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param nome (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Provincia findByNome(String nome) {
        return repository.findByNome(nome);
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Provincia) entityBean).getSigla();
    }// end of method


    /**
     * Returns all entities of the type <br>
     *
     * @return all ordered entities
     */
    public List<Provincia> findAll() {
        return (List) repository.findAllByOrderByNomeAsc();
    }// end of method


    /**
     * Returns all entities of the type <br>
     *
     * @return all ordered entities
     */
    public List<Provincia> findAllByRegione(Regione regione) {
        return (List) repository.findAllByRegioneOrderByNomeAsc(regione);
    }// end of method


    /**
     * Creazione di alcuni dati iniziali <br>
     * Viene invocato alla creazione del programma e dal bottone Reset della lista (solo per il developer) <br>
     * I dati possono essere presi da una Enumeration o creati direttamemte <br>
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse che cancella tutta la Collection <br>
     *
     * @return numero di elementi creati
     */
    @Override
    public int reset() {
        List<WrapTreStringhe> listaWrap = null;
        ImportWiki importService;
        Regione regione;
        int numRec = super.reset();

        //--recupera una lista di tutte le provincie dal server di Wikipedia
        importService = appContext.getBean(ImportWiki.class);
        listaWrap = importService.province();

        if (listaWrap != null && listaWrap.size() > 0) {
            for (WrapTreStringhe wrap : listaWrap) {
                regione = regioneService.findByNome(wrap.getTerza());
                creaIfNotExist(regione, wrap.getPrima(), wrap.getSeconda());
            }// end of for cycle
        }// end of if cycle

        return numRec;
    }// end of method


    public List<Provincia> findItems(AEntity entityBean) {
        return findAllByRegione((Regione)entityBean);
    }// end of method

}// end of class