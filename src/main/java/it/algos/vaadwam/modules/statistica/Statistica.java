package it.algos.vaadwam.modules.statistica;

import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import it.algos.vaadwam.wam.WamEntity;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-ott-2019 7.35.49 <br>
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * <p>
 * Not annotated with @SpringComponent (inutile).  <br>
 * Not annotated with @Scope (inutile). Le istanze 'prototype' vengono generate da xxxService.newEntity() <br>
 * Not annotated with @Qualifier (inutile) <br>
 * Annotated with @Entity (facoltativo) per specificare che si tratta di una collection (DB Mongo) <br>
 * Annotated with @Document (facoltativo) per avere un nome della collection (DB Mongo) diverso dal nome della Entity <br>
 * Annotated with @TypeAlias (facoltativo) to replace the fully qualified class name with a different value. <br>
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter <br>
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications <br>
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service <br>
 * Annotated with @Builder (Lombok) con un metodo specifico, per usare quello standard nella (eventuale) sottoclasse <br>
 * - lets you automatically produce the code required to have your class be instantiable with code such as:
 * - Person.builder().name("Adam Savage").city("San Francisco").build(); <br>
 * Annotated with @EqualsAndHashCode (Lombok) per l'uguaglianza di due istanze della classe <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 * Annotated with @AIEntity (facoltativo Algos) per alcuni parametri generali del modulo <br>
 * Annotated with @AIList (facoltativo Algos) per le colonne automatiche della Grid nella lista <br>
 * Annotated with @AIForm (facoltativo Algos) per i fields automatici nel dialogo del Form <br>
 * <p>
 * Inserisce SEMPRE la versione di serializzazione <br>
 * Le singole property sono pubbliche in modo da poterne leggere il valore tramite 'reflection' <br>
 * Le singole property sono annotate con @AIField (obbligatorio Algos) per il tipo di fields nel dialogo del Form <br>
 * Le singole property sono annotate con @AIColumn (facoltativo Algos) per il tipo di Column nella Grid <br>
 * Le singole property sono annotate con @Field("xxx") (facoltativo)
 * -which gives a name to the key to be used to store the field inside the document.
 * -The property name (i.e. 'descrizione') would be used as the field key if this annotation was not included.
 * -Remember that field keys are repeated for every document so using a smaller key name will reduce the required space.
 * -va usato SOLO per 'collection' molto grandi (per evitare confusione sul nome della property da usare).
 * Le property non primitive, di default sono EMBEDDED con un riferimento statico
 * (EAFieldType.link e XxxPresenter.class)
 * Le singole property possono essere annotate con @DBRef per un riferimento DINAMICO (not embedded)
 * (EAFieldType.combo e XXService.class, con inserimento automatico nel ViewDialog)
 * Una (e una sola) property deve avere @AIColumn(flexGrow = true) per fissare la larghezza della Grid <br>
 */
@Entity
@Document(collection = "statistica")
@TypeAlias("statistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderStatistica")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovrascrivibile = false)
@AIEntity(recordName = "statistica", company = EACompanyRequired.obbligatoria)
@AIList(fields = {"milite", "last", "delta", "valido", "turni", "ore", "media"})
@AIForm(fields = {"ordine", "last", "delta", "valido", "turni", "ore", "media"})
public class Statistica extends WamEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di presentazione (obbligatorio, unico) <br>
     * il più importante per primo <br>
     */
    @NotNull
    @Indexed()
    @Field("ord")
    @AIField(type = EAFieldType.integer, widthEM = 3)
    @AIColumn(name = "#", widthEM = 3)
    public int ordine;

    /**
     * milite di riferimento (obbligatorio, unico)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @Field("mil")
    @AIField(type = EAFieldType.combo, serviceClazz = MiliteService.class)
    @AIColumn(widthEM = 14)
    public Milite milite;


    /**
     * ultimo turno (obbligatorio)
     */
    @NotNull
    @Field("last")
    @AIField(type = EAFieldType.monthdate)
    @AIColumn(widthEM = 6)
    public LocalDate last;


    /**
     * giorni trascorsi dall'ultimo turno (obbligatorio) <br>
     */
    @NotNull
    @Indexed()
    @Field("dif")
    @AIField(type = EAFieldType.integer, widthEM = 3)
    @AIColumn(name = "Giorni", widthEM = 5)
    public int delta;

    /**
     * stato di servizio (numero turni sufficiente oppure no)
     */
    @Field("val")
    @AIField(type = EAFieldType.yesnobold)
    @AIColumn(name = "OK", widthEM = 5)
    public boolean valido;

    /**
     * turni totali effettuati nell'anno (obbligatorio) <br>
     */
    @NotNull
    @Indexed()
    @Field("tur")
    @AIField(type = EAFieldType.integer, widthEM = 3)
    @AIColumn(name = "Turni", widthEM = 5)
    public int turni;

    /**
     * ore totali effettuate nell'anno (obbligatorio) <br>
     */
    @NotNull
    @Indexed()
    @Field("ore")
    @AIField(type = EAFieldType.integer, widthEM = 3)
    @AIColumn(name = "Ore", widthEM = 5)
    public int ore;

    /**
     * ore medie per turno <br>
     */
    @NotNull
    @Indexed()
    @Field("media")
    @AIField(type = EAFieldType.onedecimal, widthEM = 3)
    @AIColumn(name = "Media", widthEM = 5)
    public int media;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return milite.toString();
    }// end of method


}// end of entity class