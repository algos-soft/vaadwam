package it.algos.vaadflow.modules.role;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_ROL;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-set-2019 16.12.25 <br>
 * <br>
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_ROL)
@AIScript(sovrascrivibile = false)
public interface RoleRepository extends MongoRepository<Role, String> {

	public Role findByCode(String code);

	public List<Role> findAllByOrderByCodeAsc();

	public Role findByOrdine(int ordine);

	public List<Role> findAllByOrderByOrdineAsc();

	public List<Role> findTop1AllByOrderByOrdineDesc();

}// end of interface