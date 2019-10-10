package it.algos.vaadflow.modules.address;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow.application.FlowCost.TAG_ADD;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-set-2019 17.58.04 <br>
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
@Qualifier(TAG_ADD)
@Slf4j
@AIScript(sovrascrivibile = false)
public class AddressService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br> Spring costruisce
     * una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br> Qui si una una
     * interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public AddressRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    public AddressService(@Qualifier(TAG_ADD) MongoRepository repository) {
        super(repository);
        super.entityClass = Address.class;
        this.repository = (AddressRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param eaAddress: enumeration di dati iniziali di prova
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(EAAddress eaAddress) {
        boolean creata = false;

        if (isMancaByKeyUnica(eaAddress.getIndirizzo())) {
            AEntity entity = save(newEntity(eaAddress));
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
    public Address newEntity() {
        return newEntity("", "", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Usa una enumeration di dati iniziali di prova <br>
     *
     * @param eaAddress: enumeration di dati iniziali di prova
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Address newEntity(EAAddress eaAddress) {
        return newEntity(eaAddress.getIndirizzo(), eaAddress.getLocalita(), eaAddress.getCap());
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param indirizzo: via, nome e numero (obbligatoria, non unica)
     * @param localita:  località (obbligatoria, non unica)
     * @param cap:       codice di avviamento postale (obbligatoria, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Address newEntity(String indirizzo, String localita, String cap) {
        return Address.builderAddress()
                .indirizzo(text.isValid(indirizzo) ? indirizzo : null)
                .localita(text.isValid(localita) ? localita : null)
                .cap(text.isValid(cap) ? cap : null)
                .build();
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Address) entityBean).getIndirizzo();
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        Address entity = (Address) super.beforeSave(entityBean, operation);

        if (text.isEmpty(entity.indirizzo)) {
            entity = null;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param indirizzo (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Address findByKeyUnica(String indirizzo) {
        return repository.findByIndirizzo(indirizzo);
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

        for (EAAddress eaAddress : EAAddress.values()) {
            numRec = creaIfNotExist(eaAddress) ? numRec + 1 : numRec;
        }// end of for cycle

        return numRec;
    }// end of method

}// end of class