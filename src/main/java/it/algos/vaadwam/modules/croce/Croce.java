package it.algos.vaadwam.modules.croce;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.person.Person;
import lombok.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;

import static it.algos.vaadwam.application.WamCost.TAG_CRO;

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
 * Alcune property vengono gestite dalla superclasse Company <br>
 */
@Entity
@Document(collection = "croce")
@TypeAlias("croce")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderCroce")
@EqualsAndHashCode(callSuper = false)
@AIEntity(company = EACompanyRequired.nonUsata)
@AIList(fields = {"code", "descrizione", "presidente", "contatto", "telefono", "mail"})
@AIForm(fields = {"organizzazione", "code", "descrizione", "presidente", "contatto", "telefono", "mail", "indirizzo", "note"})
@AIScript(sovrascrivibile = false)
public class Croce extends Company {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    @Transient
    public boolean usaSuperClasse;

    /**
     * organizzazione (facoltativo)
     */
    @Field("organizzazione")
    @AIField(type = EAFieldType.enumeration, enumClazz = EAOrganizzazione.class, nullSelectionAllowed = false, widthEM = 30)
    @AIColumn(widthEM = 20)
    public EAOrganizzazione organizzazione;


    /**
     * presidente (facoltativo)
     * riferimento statico SENZA @DBRef
     */
    @Field("presidente")
    @AIField(type = EAFieldType.link)
    @AIColumn(widthEM = 14)
    public Person presidente;


    // il numero di giorni considerato critico per blocco iscrizioni nel tabellone
    // TODO: 05/05/20 da gestire come preferenza
    public int getGiorniCritico(){
        return 2;
    }

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }// end of method

    // il numero di giorni considerato semicritico (arancione)
    // TODO: 05/05/20 da gestire come preferenza
    public int getGiorniSemicritico() {
        return 4;
    }

}// end of entity class