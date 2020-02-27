package it.algos.vaadwam.modules.milite;

import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldAccessibility;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.funzione.Funzione;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.22.05 <br>
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * <p>
 * Not annotated with @SpringComponent (inutile).  <br>
 * Not annotated with @Scope (inutile). Le istanze 'prototype' vengono generate da xxxService.newEntity() <br>
 * Not annotated with @Qualifier (inutile) <br>
 * Annotated with @Document (facoltativo) per avere un nome della collection (DB Mongo) diverso dal nome della Entity <br>
 * Annotated with @TypeAlias (facoltativo) to replace the fully qualified class name with a different value. <br>
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter <br>
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications <br>
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service <br>
 * Annotated with @Builder (Lombok) con un metodo specifico, per usare quello standard nella (eventuale) sottoclasse <br>
 * - lets you automatically produce the code required to have your class be instantiable with code such as:
 * - Person.builder().name("Adam Savage").city("San Francisco").build(); <br>
 * Annotated with @EqualsAndHashCode (Lombok) per l'uguaglianza di due istanze della classe <br>
 * Annotated with @AIEntity (facoltativo Algos) per alcuni parametri generali del modulo <br>
 * Annotated with @AIList (facoltativo Algos) per le colonne automatiche della Grid nella lista <br>
 * Annotated with @AIForm (facoltativo Algos) per i fields automatici nel dialogo del Form <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * <p>
 * Inserisce SEMPRE la versione di serializzazione <br>
 * Le singole property sono pubbliche in modo da poterne leggere il valore tramite 'reflection' <br>
 * Le singole property sono annotate con @AIColumn (facoltativo Algos) per il tipo di Column nella Grid <br>
 * Le singole property sono annotate con @AIField (obbligatorio Algos) per il tipo di fields nel dialogo del Form <br>
 * Le singole property sono annotate con @Field("xxx") (facoltativo)
 * -which gives a name to the key to be used to store the field inside the document.
 * -The property name (i.e. 'descrizione') would be used as the field key if this annotation was not included.
 * -Remember that field keys are repeated for every document so using a smaller key name will reduce the required space.
 */


/**
 * Alcune property vengono gestite dalla superclasse Utente <br>
 */
@Entity
@Document(collection = "milite")
@TypeAlias("milite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderMilite")
@EqualsAndHashCode(callSuper = false)
@AIEntity(company = EACompanyRequired.obbligatoria)
@AIList(fields = {"ordine", "username", "enabled", "nome", "cognome", "admin", "infermiere", "dipendente", "funzioni", "noteWam"})
@AIForm(fields = {"ordine", "nome", "cognome", "username", "password", "telefono", "mail", "indirizzo", "role", "locked", "admin", "dipendente", "infermiere", "enabled", "funzioni", "noteWam"})
@AIScript(sovrascrivibile = false)
public class Milite extends Person {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    @Transient
    public boolean usaSuperClasse;

    /**
     * Riferimento alla croce (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @Field("croce")
    @AIField(type = EAFieldType.combo, serviceClazz = CroceService.class, dev = EAFieldAccessibility.newOnly, admin = EAFieldAccessibility.showOnly)
    @AIColumn(name = "Croce", widthEM = 20)
    public Croce croce;

    /**
     * ordine di presentazione (obbligatorio, unico) <br>
     * il più importante per primo <br>
     */
    @NotNull
    @Indexed()
    @Field("ord")
    @AIField(type = EAFieldType.integer, widthEM = 3)
    @AIColumn(name = "#")
    public int ordine;

    @Field("adm")
    @AIField(type = EAFieldType.checkbox)
    @AIColumn(headerIcon = VaadinIcon.USER_STAR)
    public boolean admin;

    @Field("dip")
    @AIField(type = EAFieldType.checkbox)
    @AIColumn(headerIcon = VaadinIcon.USER_CLOCK)
    public boolean dipendente;

    @Field("inf")
    @AIField(type = EAFieldType.checkbox)
    @AIColumn(headerIcon = VaadinIcon.USER_HEART)
    public boolean infermiere;

    /**
     * Funzioni per le quali il milite è abilitato
     * la property viene registrata come Set perché MultiselectComboBox usa un set nel Binder e nel dialogo
     * viene poi resa disponibile da MiliteService come List (ordinata) per comodità d'uso
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @Field("funz")
    @AIField(type = EAFieldType.multicombo, serviceClazz = MiliteService.class, widthEM = 20, name = "Funzioni per le quali il milite è abilitato")
    @AIColumn(flexGrow = true)
    public Set<Funzione> funzioni;

    /**
     * note aggiuntive (facoltativo)
     */
    @Field("noteWam")
    @AIField(name = "note", type = EAFieldType.textarea)
    @AIColumn(name = "note", widthEM = 20)
    public String noteWam;


    @Transient
    public String sigla;


    /**
     * Presentazione della persona sul tabellonesuperato <br>
     * Alcune Croci usano il cognome, altre il nome (le croci con meno persone) <br>
     * Segue la prima iniziale di nome/cognome nel caso ci siano omonimie <br>
     */
    public String getSigla() {
        //@da aggiungere una preferenza alla croce
        if (true) {
            return cognome + " " + nome.substring(0, 1) + ".";
        } else {
            return nome + " " + cognome.substring(0, 1) + ".";
        }// end of if/else cycle
    }// end of method


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getUsername();
    }// end of method

}// end of entity class