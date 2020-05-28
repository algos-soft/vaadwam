package it.algos.vaadwam.modules.log;

import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldAccessibility;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadwam.enumeration.EAWamLogType;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 27-mag-2020
 * Time: 09:36
 */
@Entity
@Document(collection = "wamlog")
@TypeAlias("wamlog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderWamlog")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovrascrivibile = false)
@AIEntity(recordName = "log", company = EACompanyRequired.obbligatoria)
@AIList(fields = {"croce", "type", "milite", "evento", "descrizione"})
@AIForm(fields = {"croce", "type", "milite", "evento", "descrizione"})
public class WamLog extends AEntity {


    /**
     * Riferimento alla croce (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @Field("croce")
    @AIField(type = EAFieldType.combo, serviceClazz = CroceService.class, dev = EAFieldAccessibility.newOnly, admin = EAFieldAccessibility.showOnly)
    @AIColumn(name = "Croce", widthEM = 20)
    public Croce croce;


    /**
     * milite di riferimento
     * riferimento dinamico CON @DBRef
     * Gestita in automatico
     */
    @DBRef
    @AIField(type = EAFieldType.combo, serviceClazz = MiliteService.class)
    @AIColumn(widthEM = 18)
    public Milite milite;


    /**
     * raggruppamento logico dei log per type di eventi (obbligatorio)
     */
    @NotEmpty(message = "La tipologia del log è obbligatoria")
    @Indexed()
    @Field("type")
    @AIField(type = EAFieldType.enumeration, enumClazz = EAWamLogType.class, nullSelectionAllowed = false, widthEM = 10)
    @AIColumn(widthEM = 12, sortable = false)
    public EAWamLogType type;


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
     * descrizione (facoltativa, non unica) <br>
     */
    @Size(min = 2, max = 50)
    @Field("desc")
    @AIField(type = EAFieldType.textarea, firstCapital = true, widthEM = 24)
    @AIColumn(flexGrow = true)
    public String descrizione;

}
