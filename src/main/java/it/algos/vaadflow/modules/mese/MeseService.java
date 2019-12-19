package it.algos.vaadflow.modules.mese;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.secolo.EASecolo;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_MES;


/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-set-2019 19.27.41 <br>
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
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_MES)
@Slf4j
@AIScript(sovrascrivibile = false)
public class MeseService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public MeseRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public MeseService(@Qualifier(TAG_MES) MongoRepository repository) {
        super(repository);
        super.entityClass = Mese.class;
        this.repository = (MeseRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param eaMese: enumeration di dati iniziali di prova
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(EAMese eaMese) {
        boolean creata = false;

        if (isMancaByKeyUnica(eaMese.getLungo())) {
            AEntity entity = save(newEntity(eaMese.getLungo(), eaMese.getBreve(), eaMese.getGiorni(), eaMese.ordinal() + 1));
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
    public Mese newEntity() {
        return newEntity("", "", 0, 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param titoloLungo nome completo (obbligatorio, unico)
     * @param titoloBreve nome abbreviato di tre cifre (obbligatorio, unico)
     * @param giorni      numero di giorni presenti (obbligatorio)
     * @param ordine      (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Mese newEntity(String titoloLungo, String titoloBreve, int giorni, int ordine) {
        return Mese.builderMese()
                .titoloLungo(text.isValid(titoloLungo) ? titoloLungo : null)
                .titoloBreve(text.isValid(titoloBreve) ? titoloBreve : null)
                .giorni(giorni)
                .ordine(ordine)
                .build();
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Mese) entityBean).getTitoloLungo();
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        Mese entity = (Mese) super.beforeSave(entityBean, operation);

        if (text.isEmpty(entity.titoloLungo) || text.isEmpty(entity.titoloBreve) || entity.giorni == 0) {
            entity = null;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param titolo (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Mese findByKeyUnica(String titolo) {
        return repository.findByTitoloLungo(titolo);
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     * Se esiste la property 'ordine', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'code', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'descrizione', ordinate secondo questa property <br>
     * Altrimenti, ordinate secondo il metodo sovrascritto nella sottoclasse concreta <br>
     * Altrimenti, ordinate in ordine di inserimento nel DB mongo <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<? extends AEntity> findAll() {
        return (List<Mese>) super.findAll();
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

        for (EAMese eaMese : EAMese.values()) {
            numRec = creaIfNotExist(eaMese) ? numRec + 1 : numRec;
        }// end of for cycle

        return numRec;
    }// end of method

    /**
     * Riordina una lista di valori <br>
     *
     * @return numero di elementi creato
     */
    public List<String> riordina(List<String> listaDisordinata) {
        List<String> listaOrdinata = null;
        List<Integer> keyList = null;
        HashMap<Integer, String> mappa;
        int key;

        if (listaDisordinata != null && listaDisordinata.size() > 0) {
            mappa = new LinkedHashMap();
            keyList = new ArrayList<>();

            for (String titolo : listaDisordinata) {
                key = EAMese.getOrder(titolo);
                keyList.add(key);
                mappa.put(key, titolo);
            }// end of for cycle

            keyList = array.sort(keyList);
            listaOrdinata = new ArrayList<>();
            for (int pos : keyList) {
                listaOrdinata.add(mappa.get(pos));
            }// end of for cycle
        }// end of if cycle

        return listaOrdinata;
    }// end of method

}// end of class