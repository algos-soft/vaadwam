package it.algos.vaadwam.modules.iscrizione;

import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
@Document(collection = "iscrizione")
@TypeAlias("iscrizione")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderIscrizione")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovrascrivibile = false)
@AIEntity(company = EACompanyRequired.nonUsata)
@AIList(fields = {"funzione", "milite", "lastModifica", "durata", "esisteProblema"})
@AIForm(fields = {"funzione", "milite", "lastModifica", "inizio", "note", "fine", "durataEffettiva", "esisteProblema", "notificaInviata"})
public class Iscrizione extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * funzione per cui il milite/volontario/utente si iscrive (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @Field("funz")
    @AIField(type = EAFieldType.combo, serviceClazz = FunzioneService.class)
    @AIColumn(widthEM = 20)
    public Funzione funzione;


    /**
     * milite di riferimento (facoltativo alla creazione, dopo obbligatorio)
     * riferimento dinamico CON @DBRef
     */
//    @NotNull
    @DBRef
    @Field("mil")
    @AIField(type = EAFieldType.combo, serviceClazz = MiliteService.class)
    @AIColumn(widthEM = 20)
    public Milite milite;


    /**
     * timestamp di creazione (obbligatorio, inserito in automatico)
     * (usato per bloccare la cancIscrizione dopo un determinato intervallo di tempo)
     */
    @NotNull
    @Field("last")
    @AIField(type = EAFieldType.localdatetime)
    public LocalDateTime lastModifica;


    /**
     * Orario di inizio turno del singolo milite (obbligatorio per alcuni servizi, suggerito da turno)
     */
    @Field("ini")
    @AIField(type = EAFieldType.localtime, name = "Inizio effettivo turno")
    @AIColumn(headerIcon = VaadinIcon.FORWARD, headerIconColor = "green")
    public LocalTime inizio;

    /**
     * Orario di fine turno del singolo milite (obbligatorio per alcuni servizi, suggerito da turno)
     */
    @NotNull
    @Field("end")
    @AIField(type = EAFieldType.localtime, name = "Fine effettiva turno")
    @AIColumn(headerIcon = VaadinIcon.BACKWARDS, headerIconColor = "red")
    public LocalTime fine;

    /**
     * Durata effettiva (in minuti) del turno del singolo milite (obbligatoria per alcuni servizi, calcolata in automatico prima del Save)
     * Informazione ridondante ma comoda per le successive elaborazioni (statistiche)
     */
    @Field("dur")
    @AIField(type = EAFieldType.integer, name = "Durata")
    @AIColumn(headerIcon = VaadinIcon.CALENDAR)
    public int durataEffettiva;


    /**
     * eventuali problemi di presenza del milite/volontario di questa iscrizione nel turno (facoltativa)
     * serve per evidenziare il problema nel tabellone
     */
    @Field("prob")
    @AIField(type = EAFieldType.checkboxreverse)
    @AIColumn(headerIcon = VaadinIcon.STOPWATCH)
    public boolean esisteProblema;


    /**
     * se è stata inviata la notifica di inizio turno dal sistema di notifiche automatiche (facoltativa)
     */
    @Field("inv")
    @AIField(type = EAFieldType.yesno)
    @AIColumn( headerIcon = VaadinIcon.ENVELOPE, headerIconColor = "green")
    public boolean notificaInviata;


}// end of entity class