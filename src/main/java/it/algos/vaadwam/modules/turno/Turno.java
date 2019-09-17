package it.algos.vaadwam.modules.turno;

import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldAccessibility;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
@Entity
@Document(collection = "turno")
@TypeAlias("turno")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderTurno")
@EqualsAndHashCode(callSuper = false)
@AIEntity(company = EACompanyRequired.obbligatoria)
@AIList(fields = {"giorno", "servizio", "inizio", "fine"})
@AIForm(fields = {"giorno", "servizio", "titoloExtra", "localitaExtra"})
@AIScript(sovrascrivibile = false)
public class Turno extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

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
     * giorno di inizio turno (obbligatorio, calcolato da inizio - serve per le query)
     */
    @NotNull
    @Field("giorno")
    @AIField(type = EAFieldType.localdate)
    @AIColumn()
    public LocalDate giorno;


    /**
     * servizio di riferimento (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @Field("serv")
    @AIField(type = EAFieldType.combo, serviceClazz = ServizioService.class)
    @AIColumn(widthEM = 8)
    public Servizio servizio;


    /**
     * Orario di inizio turno (obbligatorio, suggerito da servizio)
     */
    @Field("ini")
    @AIField(type = EAFieldType.localtime, name = "Inizio turno")
    @AIColumn(headerIcon = VaadinIcon.FORWARD, headerIconColor = "green")
    public LocalTime inizio;

    /**
     * Orario di fine turno (obbligatorio, suggerito da servizio)
     */
    @NotNull
    @Field("end")
    @AIField(type = EAFieldType.localtime, name = "Fine effettiva turno")
    @AIColumn(headerIcon = VaadinIcon.BACKWARDS, headerIconColor = "red")
    public LocalTime fine;

//    /**
//     * Durata effettiva (in minuti) del turno (obbligatoria, calcolata in automatico prima del Save)
//     * Informazione ridondante ma comoda per le successive elaborazioni (tabellone, iscrizioni, statistiche)
//     */
//    @Field("dur")
//    @AIField(type = EAFieldType.integer, name = "Durata")
//    public int durataEffettiva;

    /**
     * iscrizioni dei volontari a questo turno (obbligatorio per un turno valido)
     * riferimento statico SENZA @DBRef (embedded)
     */
    @NotNull
    @Field("isc")
    @AIField(type = EAFieldType.noone, widthEM = 20, name = "Iscrizioni per questo turno")
    public List<Iscrizione> iscrizioni;

    /**
     * motivazione del turno extra (facoltativo)
     */
    @Field("tit")
    @AIField(type = EAFieldType.text, widthEM = 24)
    @AIColumn()
    public String titoloExtra;

    /**
     * nome evidenziato della località per turni extra (facoltativo)
     */
    @AIField(type = EAFieldType.text, widthEM = 24)
    @Field("loc")
    @AIColumn()
    public String localitaExtra;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "" + croce + giorno.getDayOfYear() + servizio;
    }// end of method


}// end of entity class