package it.algos.vaadflow.service;

import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.modules.company.CompanyList;
import it.algos.vaadflow.modules.anno.AnnoList;
import it.algos.vaadflow.modules.role.RoleList;
import it.algos.vaadflow.modules.utente.UtenteList;
import it.algos.vaadflow.modules.versione.VersioneList;
import it.algos.vaadflow.modules.preferenza.PreferenzaList;
import it.algos.vaadflow.modules.log.LogList;
import it.algos.vaadflow.modules.address.AddressList;
import it.algos.vaadflow.modules.mese.MeseList;
import it.algos.vaadflow.modules.giorno.GiornoList;
import it.algos.vaadflow.modules.person.PersonList;
import it.algos.vaadflow.modules.secolo.SecoloList;
import it.algos.vaadflow.modules.logtype.LogtypeList;
import it.algos.vaadflow.developer.DeveloperView;
import it.algos.vaadflow.enumeration.EAPreferenza;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.wizard.WizardView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 21-set-2018
 * Time: 19:31
 * <p>
 * Gestisce la creazione delle liste standard di menu <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ABootService.class); <br>
 * 2) ABootService.getInstance(); <br>
 * 3) @Autowired private ABootService bootService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * <p>
 */
@Service
@Slf4j
public class ABootService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private final property
     */
    private static final ABootService INSTANCE = new ABootService();


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private ABootService() {
    }// end of constructor

    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static ABootService getInstance() {
        return INSTANCE;
    }// end of static method


    public void creaRouteStandard() {
        FlowVar.menuClazzList = new ArrayList<>();

        //--developer
//        if (pref.isBool(EAPreferenza.showDeveloper.getCode())) {
//            FlowCost.MENU_CLAZZ_LIST.add(DeveloperView.class);
//        }// end of if cycle
        if (pref.isBool(EAPreferenza.showWizard.getCode())) {
            FlowVar.menuClazzList.add(WizardView.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showRole.getCode())) {
            FlowVar.menuClazzList.add(RoleList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showUser.getCode())) {
            FlowVar.menuClazzList.add(UtenteList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showLogType.getCode())) {
            FlowVar.menuClazzList.add(LogtypeList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showCompany.getCode())) {
            FlowVar.menuClazzList.add(CompanyList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showAddress.getCode())) {
            FlowVar.menuClazzList.add(AddressList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showPerson.getCode())) {
            FlowVar.menuClazzList.add(PersonList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showVersione.getCode())) {
            FlowVar.menuClazzList.add(VersioneList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showGiorno.getCode())) {
            FlowVar.menuClazzList.add(GiornoList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showAnno.getCode())) {
            FlowVar.menuClazzList.add(AnnoList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showMese.getCode())) {
            FlowVar.menuClazzList.add(MeseList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showSecolo.getCode())) {
            FlowVar.menuClazzList.add(SecoloList.class);
        }// end of if cycle

        //--admin
        if (pref.isBool(EAPreferenza.showPreferenza.getCode())) {
            FlowVar.menuClazzList.add(PreferenzaList.class);
        }// end of if cycle
        if (pref.isBool(EAPreferenza.showLog.getCode())) {
            FlowVar.menuClazzList.add(LogList.class);
        }// end of if cycle

    }// end of method


    public void creaRouteStandardDeveloper() {
        FlowVar.menuClazzList = new ArrayList<>();

        FlowVar.menuClazzList.add(DeveloperView.class);
        FlowVar.menuClazzList.add(RoleList.class);
        FlowVar.menuClazzList.add(UtenteList.class);
        FlowVar.menuClazzList.add(WizardView.class);

        FlowVar.menuClazzList.add(PreferenzaList.class);
        FlowVar.menuClazzList.add(VersioneList.class);
        FlowVar.menuClazzList.add(LogList.class);

        FlowVar.menuClazzList.add(CompanyList.class);
        FlowVar.menuClazzList.add(AddressList.class);
        FlowVar.menuClazzList.add(PersonList.class);

        FlowVar.menuClazzList.add(LogtypeList.class);
        FlowVar.menuClazzList.add(RoleList.class);
        FlowVar.menuClazzList.add(PersonList.class);
        FlowVar.menuClazzList.add(AddressList.class);
        FlowVar.menuClazzList.add(LogList.class);
        FlowVar.menuClazzList.add(PreferenzaList.class);
        FlowVar.menuClazzList.add(VersioneList.class);
        FlowVar.menuClazzList.add(UtenteList.class);
        FlowVar.menuClazzList.add(CompanyList.class);
        FlowVar.menuClazzList.add(LogtypeList.class);
        FlowVar.menuClazzList.add(RoleList.class);
        FlowVar.menuClazzList.add(SecoloList.class);
        FlowVar.menuClazzList.add(PersonList.class);
        FlowVar.menuClazzList.add(GiornoList.class);
        FlowVar.menuClazzList.add(MeseList.class);
        FlowVar.menuClazzList.add(AddressList.class);
        FlowVar.menuClazzList.add(LogList.class);
        FlowVar.menuClazzList.add(PreferenzaList.class);
        FlowVar.menuClazzList.add(VersioneList.class);
        FlowVar.menuClazzList.add(UtenteList.class);
        FlowVar.menuClazzList.add(AnnoList.class);
        FlowVar.menuClazzList.add(CompanyList.class);
	}// end of method


    public void creaRouteStandardAdmin() {
        FlowVar.menuClazzList = new ArrayList<>();

        FlowVar.menuClazzList.add(PreferenzaList.class);
        FlowVar.menuClazzList.add(LogList.class);

    }// end of method

}// end of class