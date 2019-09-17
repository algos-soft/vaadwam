package it.algos.vaadflow.modules.log;

import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.backend.entity.ACEntity;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.logtype.Logtype;
import it.algos.vaadflow.modules.logtype.LogtypeService;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 26-ott-2018 9.59.58 <br>
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
@Entity
@Document(collection = "log")
@TypeAlias("log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderLog")
@EqualsAndHashCode(callSuper = false)
@AIEntity(company = EACompanyRequired.obbligatoria)
@AIList(fields = {"livello", "type", "evento", "descrizione"})
@AIForm(fields = {"livello", "type", "descrizione", "evento"})
@AIScript(sovrascrivibile = false)
public class Log extends ACEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * rilevanza del log (obbligatorio) <br>
     */
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Field("liv")
    @AIField(type = EAFieldType.enumeration, enumClazz = Livello.class, required = true, widthEM = 4)
    @AIColumn(widthEM = 6, sortable = false)
    public Livello livello;

    /**
     * descrizione (obbligatoria, non unica) <br>
     */
    @NotNull(message = "La descrizione è obbligatoria")
    @Size(min = 2, max = 50)
    @Field("desc")
    @AIField(type = EAFieldType.textarea, firstCapital = true, widthEM = 24)
    @AIColumn(flexGrow = true)
    public String descrizione;

    /**
     * Data dell'evento (obbligatoria, non modificabile)
     * Gestita in automatico
     * Field visibile solo al buttonAdmin
     */
    @NotNull
    @Indexed()
    @AIField(type = EAFieldType.localdatetime)
    public LocalDateTime evento;

    /**
     * raggruppamento logico dei log per type di eventi (obbligatorio)
     */
    @NotEmpty(message = "La tipologia del log è obbligatoria")
    @Indexed()
    @Field("type")
    @AIField(type = EAFieldType.combo, serviceClazz = LogtypeService.class, nullSelectionAllowed = false, widthEM = 10)
    @AIColumn(widthEM = 7, sortable = false)
    public Logtype type;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getType().toString() + " - " + getDescrizione();
    }// end of method

}// end of entity class