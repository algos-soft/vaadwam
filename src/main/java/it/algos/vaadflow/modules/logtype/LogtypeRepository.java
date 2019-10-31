package it.algos.vaadflow.modules.logtype;

import java.util.List;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import static it.algos.vaadflow.application.FlowCost.TAG_LOG;
import static it.algos.vaadflow.application.FlowCost.TAG_TYP;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 14-ott-2019 18.40.19 <br>
 * <br>
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_TYP)
@AIScript(sovrascrivibile = false)
public interface LogtypeRepository extends MongoRepository<Logtype, String> {

	public Logtype findByCode(String code);

	public List<Logtype> findAllByOrderByOrdineAsc();

}// end of class