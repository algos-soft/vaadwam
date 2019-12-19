package it.algos.vaadflow.modules.preferenza;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.enumeration.EAPrefType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_PRE;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 14-ott-2019 18.44.27 <br>
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
@Qualifier(TAG_PRE)
@AIScript(sovrascrivibile = false)
public interface PreferenzaRepository extends MongoRepository<Preferenza, String> {

    public Preferenza findByCode(String code);

    public int countByCode(String code);

    public Preferenza findFirstByCode(String code);

    public List<Preferenza> findAllByOrderByCodeAsc();

    public Preferenza findByDescrizione(String descrizione);

    public List<Preferenza> findAllByOrderByDescrizioneAsc();

    public Preferenza findByOrdine(int ordine);

    public List<Preferenza> findAllByOrderByOrdineAsc();

    public List<Preferenza> findTop1AllByOrderByOrdineDesc();

    public List<Preferenza> findAllByTypeOrderByValue(EAPrefType type);

}// end of class