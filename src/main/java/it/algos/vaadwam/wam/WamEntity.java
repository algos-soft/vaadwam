package it.algos.vaadwam.wam;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIColumn;
import it.algos.vaadflow.annotation.AIField;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAFieldAccessibility;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 15-set-2018
 * Time: 16:31
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Getter
public abstract class WamEntity extends AEntity {

    /**
     * Riferimento alla croce (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @Field("croce")
    @AIField(type = EAFieldType.combo, serviceClazz = CroceService.class, dev = EAFieldAccessibility.newOnly, admin = EAFieldAccessibility.showOnly)
    @AIColumn(name = "Croce", widthEM = 30)
    public Croce croce;


}// end of class
