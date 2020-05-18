package it.algos.vaadwam.modules.turno;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.servizio.Servizio;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.TAG_TUR;

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
@Qualifier(TAG_TUR)
@AIScript(sovrascrivibile = false)
public interface TurnoRepository extends MongoRepository<Turno, String> {


    //	public Turno findByCompanyAndCode(Company company, String code);

    public int countAllByCroceAndGiornoBetweenOrderByGiornoAsc(Croce croce, LocalDate inzio, LocalDate fine);

    public int countAllByCroceOrderByGiornoAsc(Croce croce);

    public List<Turno> findAllByCroceOrderByGiornoAsc(Croce croce);


    public List<Turno> findAllByCroceAndGiornoBetweenOrderByGiornoAsc(Croce croce, LocalDate inzio, LocalDate fine);

    public List<Turno> findAllByCroceAndServizioAndGiornoBetweenOrderByGiornoAsc(Croce croce, Servizio servizio, LocalDate inzio, LocalDate fine);

    public List<Turno> findAllByCroceAndServizioAndGiorno(Croce croce, Servizio servizio, LocalDate giorno);


}