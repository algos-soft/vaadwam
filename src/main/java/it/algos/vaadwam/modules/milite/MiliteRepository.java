package it.algos.vaadwam.modules.milite;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadwam.modules.croce.Croce;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_MIL;

/**
 * Project vaadwam <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.22.05 <br>
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
@Qualifier(TAG_MIL)
@AIScript(sovrascrivibile = false)
public interface MiliteRepository extends MongoRepository<Milite, String> {

    public Milite findByUsername(String username);

    public List<Milite> findAllByCroceOrderByOrdineAsc(Croce croce);

    public List<Milite> findAllByCroceOrderByUsernameAsc(Croce croce);

    public List<Milite> findAllByUsername(String username);

    public List<Milite> findAllByCroceAndAdminIsTrue(Croce croce);

    public List<Milite> findAllByCroceAndDipendenteIsTrue(Croce croce);

    public List<Milite> findAllByCroceAndInfermiereIsTrue(Croce croce);

    public List<Milite> findAllByCroceAndEnabledIsTrue(Croce croce);

    public List<Milite> findAllByCroceAndEnabledIsTrueAndFunzioniIsNull(Croce croce);

    public List<Milite> findAllByCroceAndNoteWamIsNotNull(Croce croce);


    public int countByCroce(Croce croce);

}// end of class