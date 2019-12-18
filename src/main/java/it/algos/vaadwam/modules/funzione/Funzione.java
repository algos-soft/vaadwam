package it.algos.vaadwam.modules.funzione;

import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadwam.wam.WamEntity;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 10-ott-2019 21.14.36 <br>
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
@Document(collection = "funzione")
@TypeAlias("funzione")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderFunzione")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovrascrivibile = false)
@AIEntity(company = EACompanyRequired.obbligatoria)
@AIList(fields = {"ordine", "code", "icona", "sigla", "descrizione", "dipendenti"})
@AIForm(fields = {"ordine", "code", "icona", "sigla", "descrizione", "dipendenti", "note"})
public class Funzione extends WamEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di presentazione nelle liste (obbligatorio, unico nella company,
     * con controllo automatico prima del save se è zero)
     * modificabile da developer ed admin non dall'utente
     * unico all'interno della company
     */
    @NotNull
    @Indexed()
    @Field("ord")
    @AIField(type = EAFieldType.integer, widthEM = 3)
    @AIColumn(name = "#", widthEM = 2)
    public int ordine;


    /**
     * sigla di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * visibile solo per admin e developer
     */
    @NotNull
    @Indexed()
    @Size(min = 2, max = 20)
    @Field("code")
    @AIField(type = EAFieldType.text, required = true, widthEM = 9)
    @AIColumn()
    public String code;


    /**
     * icona (facoltativa)
     */
    @Field("icon")
    @AIField(type = EAFieldType.vaadinIcon, widthEM = 8, color = "verde")
    @AIColumn(headerIcon = VaadinIcon.USERS, headerIconColor = "green")
    public VaadinIcon icona;


    /**
     * icona (facoltativa)
     */
    public String nomeIcona;


    /**
     * sigla di codifica visibile (obbligatoria, non unica)
     */
    @NotNull
    @Indexed()
    @Size(min = 2, max = 20)
    @Field("sigla")
    @AIField(type = EAFieldType.text, required = true, focus = true, widthEM = 9)
    @AIColumn()
    public String sigla;


    /**
     * descrizione (obbligatoria, non unica) <br>
     */
    @NotNull(message = "La descrizione è obbligatoria")
    @Size(min = 2, max = 50)
    @Field("desc")
    @AIField(type = EAFieldType.text, firstCapital = true)
    @AIColumn(flexGrow = true, widthEM = 15)
    public String descrizione;


    /**
     * funzione obbligatoria o facoltativa per uno specifico servizio (ha senso solo per il servizio in cui è 'embedded')
     * Il flag NON viene usato direttamente da questa funzione.
     * Quando un servizio usa una funzione, ne effettua una 'copia' e la mantiene al suo interno per uso esclusivo.
     * Se modifico successivamente all'interno del servizio la copia della funzione, le modifiche rimangono circostritte a quello specifico servizio
     * Se modifico successivamente questa funzione, le modifiche NON si estendono alle funzioni 'congelata' nei singoli servizi
     * Possono esserci decine di copie di questa funzione, 'embedded' nei servizi ed ognuna avere property diverse tra di loro, se sono state modifcate all'interno del singolo servizio
     */
    @Field("obb")
    @AIField(type = EAFieldType.checkbox)
    @AIColumn()
    public boolean obbligatoria;

    /**
     * funzioni dipendenti che vengono automaticamente abilitate quando il militi è abilitato per questa funzione
     * riferimento statico SENZA @DBRef (embedded)
     */
    @Field("dip")
    @AIField(type = EAFieldType.multicombo, name = "funzioni dipendenti")
    @AIColumn(name = "funzioni dipendenti", flexGrow = true, widthEM = 20)
    public List<Funzione> dipendenti;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return code;
    }// end of method


}// end of entity class