package it.algos.vaadwam.modules.servizio;

import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.wam.WamEntity;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 10-ott-2019 21.14.46 <br>
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
 * Le property non primitive, di default sono EMBEDDED con un riferimento statico
 * (EAFieldType.link e XxxPresenter.class)
 * Le singole property possono essere annotate con @DBRef per un riferimento DINAMICO (not embedded)
 * (EAFieldType.combo e XXService.class, con inserimento automatico nel ViewDialog)
 * Una (e una sola) property deve avere @AIColumn(flexGrow = true) per fissare la larghezza della Grid <br>
 */
@Entity
@Document(collection = "servizio")
@TypeAlias("servizio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderServizio")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovrascrivibile = false)
@AIEntity(company = EACompanyRequired.obbligatoria)
@AIList(fields = {"ordine", "code", "descrizione", "orarioDefinito", "inizio", "fine", "durataPrevista", "visibile", "ripetibile", "funzioni", "colore"})
@AIForm(fields = {"ordine", "code", "descrizione", "orarioDefinito", "inizio", "fine", "visibile", "ripetibile", "funzioni"})
public class Servizio extends WamEntity {


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
    @AIColumn(name = "#", widthEM = 2)
    public int ordine;

    /**
     * codice di riferimento (obbligatorio, unico) <br>
     */
    @NotNull
    @Indexed()
    @Size(min = 3)
    @Field("cod")
    @AIField(type = EAFieldType.text, required = true, focus = true, widthEM = 12)
    @AIColumn(widthEM = 8)
    public String code;

    /**
     * descrizione (obbligatoria, non unica) <br>
     */
    @NotNull(message = "La descrizione è obbligatoria")
    @Size(min = 2, max = 50)
    @Field("desc")
    @AIField(type = EAFieldType.text, required = true, firstCapital = true, widthEM = 24)
    @AIColumn(widthEM = 18)
    public String descrizione;

    /**
     * orario predefinito (obbligatorio, avis, centralino ed extra non ce l'hanno)
     * colonna non visibile nella grid, perché la costruisce specifica in ServizioList
     */
    @Field("ora")
    @AIField(type = EAFieldType.checkbox, name = "Turno con orario predefinito")
    @AIColumn(headerIcon = VaadinIcon.CLOCK, headerIconColor = "blue")
    public boolean orarioDefinito;


    /**
     * Orario previsto (ore e minuti) per l'inizio del turno (obbligatorio, se orarioDefinito è true)
     */
    @Field("ini")
    @AIField(type = EAFieldType.localtime, name = "Inizio servizio  (previsto)")
    @AIColumn(headerIcon = VaadinIcon.FORWARD, headerIconColor = "green")
    public LocalTime inizio;

    /**
     * Orario previsto (ore e minuti) per la fine del turno (obbligatorio, se orarioDefinito è true)
     */
    @Field("end")
    @AIField(type = EAFieldType.localtime, name = "Fine servizio (prevista)")
    @AIColumn(headerIcon = VaadinIcon.BACKWARDS, headerIconColor = "red")
    public LocalTime fine;


    /**
     * Durata prevista (in ore) del servizio (calcolata per la Grid e non registrata)
     * Informazione ridondante ma comoda per le successive elaborazioni (turno, tabellone, iscrizioni, statistiche)
     */
    @Transient
    @Field("dur")
    @AIField(type = EAFieldType.calculated, name = "Durata", serviceClazz = ServizioService.class)
    @AIColumn(headerIcon = VaadinIcon.PROGRESSBAR, widthEM = 3, methodName = "durata")
    public int durataPrevista;


    /**
     * visibile nel tabellone
     * al contrario è disabilitato dal tabellone, perchè non più utilizzato
     * sempre visibile nello storico per i periodi in cui è stato eventualmente utilizzato
     * (di default true)
     * colonna non visibile nella grid, perché la costruisce specifica in ServizioList
     */
    @Field("vis")
    @AIField(type = EAFieldType.checkbox, name = "Attivo, visibile nel tabellone")
    @AIColumn(headerIcon = VaadinIcon.ALIGN_JUSTIFY)
    public boolean visibile;


    /**
     * servizio ripetibile nella stessa giornata
     */
    @Field("rip")
    @AIField(type = EAFieldType.checkbox, name = "Ripetibile")
    @AIColumn(headerIcon = VaadinIcon.FILE_TREE)
    public boolean ripetibile;


    /**
     * Funzioni previste per espletare il servizio
     * Ozionali come programma, ma in pratica almeno una è indispensabile
     * Siccome sono 'embedded' in servizio, non serve @OneToMany() o @ManyToOne()
     * Possono essere 'obbligatorie' o meno (flag booleano).
     * Il flag appartiene alla singola funzione di questo servizio ma NON viene usato dalla funzione originaria.
     * Usando la caratteristica 'embedded', la funzione viene ricopiata dentro servizio come si trova al momento.
     * Se modifico successivamente all'interno del servizio la copia della funzione, le modifiche rimangono circostritte a questo singolo servizio
     * Se modifico successivamente la funzione originaria, le modifiche NON si estendono alla funzione 'congelata' nel servizio
     * riferimento statico SENZA @DBRef (embedded)
     */
    @Field("funz")
    @AIField(type = EAFieldType.multicombo, serviceClazz = ServizioService.class, widthEM = 20, name = "Funzioni previste per espletare il servizio")
    @AIColumn(name = "funzioni del servizio", flexGrow = true)
    public List<Funzione> funzioni;


    /**
     * colore per un raggruppamento dei servizi (facoltativo) <br>
     */
    @Field("col")
    @AIField(type = EAFieldType.color)
    @AIColumn()
    public String colore;



    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return code;
    }// end of method


}// end of entity class