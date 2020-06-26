package it.algos.vaadwam.modules.statistica;

import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 26-giu-2020
 * Time: 08:40
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * <p>
 * Not annotated with @SpringComponent (inutile).  <br>
 * Not annotated with @Scope (inutile). Le istanze 'prototype' vengono generate da xxxService.newEntity() <br>
 * Not annotated with @Qualifier (inutile) <br>
 * Annotated with @QueryEntity (facoltativo MongoDB) per specificare che si tratta di una collection (DB Mongo) <br>
 * Annotated with @Document (facoltativo MongoDB) per avere un nome della collection diverso dal nome della Entity <br>
 * Annotated with @TypeAlias (facoltativo MongoDB) to replace the fully qualified class name with a different value <br>
 * Annotated with @Data (facoltativo Lombok) for automatic use of Getter and Setter <br>
 * Annotated with @NoArgsConstructor (facoltativo Lombok) for JavaBean specifications <br>
 * Annotated with @AllArgsConstructor (facoltativo Lombok) per usare il costruttore completo nel Service <br>
 * Annotated with @Builder (facoltativo Lombok) per richiamare dalla eventuale sottoclasse un costruttore specifico <br>
 * - lets you automatically produce the code required to have your class be instantiable with code such as:
 * - Person.builder().name("Adam Savage").city("San Francisco").build(); <br>
 * Annotated with @EqualsAndHashCode (facoltativo Lombok) per l'uguaglianza di due istanze della classe <br>
 * Annotated with @AIEntity (facoltativo Algos) per alcuni parametri generali del modulo <br>
 * Annotated with @AIList (facoltativo Algos) per le colonne automatiche della Grid nella lista <br>
 * Annotated with @AIForm (facoltativo Algos) per i fields automatici nel dialogo del Form <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * In ogni caso la ri-creazione del file header avviene sempre FINO alla Annotation @AIScript <br>
 * <p>
 * Inserisce SEMPRE la versione di serializzazione <br>
 * Le singole property sono pubbliche in modo da poterne leggere il valore tramite 'reflection' <br>
 * Le singole property sono annotate con @AIField (obbligatorio Algos) per il tipo di fields nel dialogo del Form <br>
 * Le singole property sono annotate con @AIColumn (facoltativo Algos) per il tipo di Column nella Grid <br>
 * Le singole property sono annotate con @Field("xxx") (facoltativo MongoDB) per collections molto numerose <br>
 * -which gives a name to the key to be used to store the field inside the document <br>
 * -The property name (i.e. 'descrizione') would be used as the field key if this annotation was not included <br>
 * -Remember that field keys are repeated for every document so using a smaller key name will reduce the required space <br>
 * Le property non primitive, di default sono EMBEDDED con un riferimento statico
 * (EAFieldType.link e XxxPresenter.class)
 * Le singole property possono essere annotate con @DBRef per un riferimento DINAMICO (not embedded)
 * (EAFieldType.combo e XXService.class, con inserimento automatico nel ViewDialog)
 * Una (e una sola) property deve avere @AIColumn(flexGrow = true) per fissare la larghezza della Grid <br>//@todo Controllare
 */
@Document(collection = "StatTurnoIsc")
@TypeAlias("StatTurnoIsc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderStatTurnoIsc")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovrascrivibile = false)
@AIEntity(recordName = "StatTurnoIsc")
//@AIList(fields = {"ordine","giorno","servizio","funzione","inizio","fine","durataEffettiva","esisteProblema","titoloExtra","localitaExtra","equipaggio"})
@AIList(fields = {"ordine", "giorno", "servizio", "funzione", "inizio", "fine", "durataEffettiva", "esisteProblema", "equipaggio"})
public class StaTurnoIsc extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * ordine di presentazione (obbligatorio, unico) <br>
     */
    @NotNull
    @Indexed()
    @Field("ord")
    @AIField(type = EAFieldType.integer, widthEM = 3)
    @AIColumn(name = "#", widthEM = 3)
    public int ordine;

    /**
     * giorno di inizio turno (obbligatorio, calcolato da inizio - serve per le query)
     */
    @NotNull
    @Field("giorno")
    @AIField(type = EAFieldType.localdate, widthEM = 8)
    @AIColumn(widthEM = 8)
    public LocalDate giorno;


    /**
     * servizio di riferimento (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @Field("serv")
    @AIField(type = EAFieldType.link, serviceClazz = ServizioService.class, required = true)
    @AIColumn(widthEM = 7)
    public Servizio servizio;


    /**
     * funzione per cui il milite/volontario/utente si iscrive (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @Field("funz")
    @AIField(type = EAFieldType.link, serviceClazz = FunzioneService.class)
    @AIColumn(widthEM = 20)
    public Funzione funzione;


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
     * motivazione del turno extra (facoltativo)
     */
    @Field("tit")
    @AIField(type = EAFieldType.text)
    @AIColumn(widthEM = 18)
    public String titoloExtra;

    /**
     * nome evidenziato della località per turni extra (facoltativo)
     */
    @Field("loc")
    @AIField(type = EAFieldType.text)
    @AIColumn(widthEM = 18)
    public String localitaExtra;


    /**
     * altri militi nel turno (facoltativo)
     */
    @Field("altri")
    @AIField(type = EAFieldType.text)
    @AIColumn(widthEM = 40)
    public String equipaggio;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return VUOTA;
    }

}