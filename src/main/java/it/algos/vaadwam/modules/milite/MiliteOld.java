package it.algos.vaadwam.modules.milite;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldAccessibility;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.person.Person;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadwam.modules.funzione.Funzione;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_MIL;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Date: 3-giu-2018 16.01.10 <br>
 * <p>
 * Estende la entity astratta ACEntity che contiene il riferimento alla property Company <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Document (facoltativo) per avere un nome della collection (DB Mongo) diverso dal nome della Entity <br>
 * Annotated with @Scope (obbligatorio = 'singleton') <br>
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter <br>
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications <br>
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service <br>
 * Annotated with @Builder (Lombok) lets you automatically produce the code required to have your class
 * be instantiable with code such as: Person.builder().name("Adam Savage").city("San Francisco").build(); <br>
 * Annotated with @EqualsAndHashCode (Lombok) per l'uguaglianza di due istanze dellaq classe <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @AIEntity (facoltativo Algos) per alcuni parametri generali del modulo <br>
 * Annotated with @AIList (facoltativo Algos) per le colonne automatiche della Lista  <br>
 * Annotated with @AIForm (facoltativo Algos) per i fields automatici del Dialog e del Form <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * Inserisce SEMPRE la versione di serializzazione <br>
 * Le singole property sono annotate con @AIField (obbligatorio Algos) per il tipo di Field nel Dialog e nel Form <br>
 * Le singole property sono annotate con @AIColumn (facoltativo Algos) per il tipo di Column nella Grid <br>
 */
@SpringComponent
@Document(collection = "milite")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Qualifier(TAG_MIL)
@AIEntity(company = EACompanyRequired.obbligatoria)
@AIList(fields = {"company", "ordine", "code"})
@AIForm(fields = {"company", "ordine", "code", "descrizione"})
@AIScript(sovrascrivibile = false)
public class MiliteOld extends Person {


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
    @AIField(type = EAFieldType.integer, widthEM = 3)
    @AIColumn(name = "#", widthEM = 30)
    private int ordine;


    /**
     * ruolo (obbligatorio, non unico)
     * riferimento dinamico con @DBRef (obbligatorio per il ComboBox)
     */
    @DBRef
    @AIField(type = EAFieldType.combo, required = true, serviceClazz = RoleService.class)
    @AIColumn(name = "Ruolo", widthEM = 20)
    public Role role;


    /**
     * nickname di riferimento (obbligatorio, unico per company)
     */
    @NotNull
    @Size(min = 3, max = 20)
    @Indexed()
    @AIField(
            type = EAFieldType.text,
            required = true,
            focus = true,
            name = "NickName",
            widthEM = 12)
    @AIColumn(name = "Nick", widthEM = 30)
    private String nickname;


    /**
     * password (obbligatoria o facoltativa, non unica)
     */
    @Size(min = 3, max = 20)
    @AIField(
            type = EAFieldType.text,
            required = true,
            widthEM = 12,
            admin = EAFieldAccessibility.allways,
            user = EAFieldAccessibility.showOnly)
    @AIColumn(name = "Password", widthEM = 30)
    private String password;


    /**
     * buttonUser abilitato (facoltativo, di default true)
     */
    @AIField(type = EAFieldType.checkboxlabel, required = true, admin = EAFieldAccessibility.allways)
    @AIColumn(name = "OK")
    private boolean enabled;


    @AIField(type = EAFieldType.checkbox)
    @AIColumn(name = "dip")
    private boolean dipendente = false;


    @AIField(type = EAFieldType.checkbox)
    @AIColumn(name = "inf")
    private boolean infermiere = false;


    /**
     * Funzioni per le quali il milite è abilitato
     * Siccome sono 'embedded' in servizio, non serve @OneToMany() o @ManyToOne()
     * Usando la caratteristica 'embedded', la funzione viene ricopiata dentro milite come si trova al momento.
     * Se modifico successivamente all'interno del milite la copia della funzione, le modifiche rimangono circostritte a questo singolo milite
     * Se modifico successivamente la funzione originaria, le modifiche NON si estendono alla funzione 'congelata' nel milite
     */
    @AIField(type = EAFieldType.noone, widthEM = 20, name = "Funzioni per le quali il milite è abilitato")
    @AIColumn()
    private List<Funzione> funzioni;



}// end of entity class