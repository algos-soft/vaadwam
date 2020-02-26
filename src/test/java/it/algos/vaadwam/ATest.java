package it.algos.vaadwam;


import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.role.Role;
import it.algos.vaadflow.modules.role.RoleList;
import it.algos.vaadflow.service.AAnnotationService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.AReflectionService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.ui.IAView;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: lun, 25-dic-2017
 * Time: 09:54
 */
public class ATest {

    protected final static String FIELD_NAME_KEY = "id";

    protected final static String FIELD_NAME_ORDINE = "ordine";

    protected final static String FIELD_NAME_CODE = "code";

    protected final static String FIELD_NAME_COMPANY = "company2";

    protected final static String FIELD_NAME_NICKNAME = "nickname";

    protected final static String FIELD_NAME_PASSWORD = "password";

    protected final static String FIELD_NAME_ROLE = "role";

    protected final static String FIELD_NAME_ENABLED = "enabled";

    protected final static String FIELD_NAME_NOTE = "note";

    protected final static String FIELD_NAME_CREAZIONE = "creazione";

    protected final static String FIELD_NAME_MODIFICA = "modifica";

    protected final static String NAME_ORDINE = "ordine";

    protected final static String NAME_CODE = "code";

    protected final static String NAME_ROLE = "role";

    protected final static String HEADER_ORDINE = "#";

    protected final static String HEADER_CODE = "Code";

    // alcune date di riferimento
    protected final static Date DATE_UNO = new Date(1413870120000L); // 21 ottobre 2014, 7 e 42

    protected final static Date DATE_DUE = new Date(1412485440000L); // 5 ottobre 2014, 7 e 04

    protected final static Date DATE_TRE = new Date(1412485920000L); // 5 ottobre 2014, 7 e 12

    protected final static Date DATE_QUATTRO = new Date(1394259124000L); // 8 marzo 2014, 7 e 12 e 4

    protected final static LocalDate LOCAL_DATE_UNO = LocalDate.of(2014, 10, 21);

    protected final static LocalDate LOCAL_DATE_DUE = LocalDate.of(2014, 10, 5);

    protected final static LocalDate LOCAL_DATE_TRE = LocalDate.of(2015, 10, 5);

    protected final static LocalDate LOCAL_DATE_QUATTRO = LocalDate.of(2015, 3, 8);

    protected final static LocalDate LOCAL_DATE_VUOTA = LocalDate.of(1970, 1, 1);

    protected final static LocalDate LOCAL_DATE_PRIMO_VALIDO = LocalDate.of(1970, 1, 2);

    protected final static LocalDate LOCAL_DATE_OLD = LocalDate.of(1946, 10, 28);

    protected final static LocalDateTime LOCAL_DATE_TIME_UNO = LocalDateTime.of(2014, 10, 21, 7, 42);

    protected final static LocalDateTime LOCAL_DATE_TIME_DUE = LocalDateTime.of(2014, 10, 5, 7, 4);

    protected final static LocalDateTime LOCAL_DATE_TIME_VUOTA = LocalDateTime.of(1970, 1, 1, 0, 0);

    protected final static LocalDateTime LOCAL_DATE_TIME_PRIMO_VALIDO = LocalDateTime.of(1970, 1, 1, 0, 1);

    protected final static LocalDateTime LOCAL_DATE_TIME_OLD = LocalDateTime.of(1946, 10, 28, 0, 0);

    protected final static LocalTime LOCAL_TIME_UNO = LocalTime.of(7, 42);

    protected final static LocalTime LOCAL_TIME_DUE = LocalTime.of(7, 4);

    protected final static LocalTime LOCAL_TIME_TRE = LocalTime.of(22, 0);

    protected final static LocalTime LOCAL_TIME_QUATTRO = LocalTime.of(6, 0);

    protected final static LocalTime LOCAL_TIME_VUOTO = LocalTime.of(0, 0);

    protected static String DATABASE_NAME = "vaadwam";

    protected static Field FIELD_ORDINE;

    protected static Field FIELD_CODE;

    protected static Field FIELD_ROLE;

    protected static Class<? extends IAView> ROLE_VIEW_CLASS = RoleList.class;

    protected static Class<? extends AEntity> ROLE_ENTITY_CLASS = Role.class;

    protected static String TITOLO_WEB = "http://www.algos.it/estudio";

    protected static String TITOLO_WEB_ERRATO = "http://www.pippozbelloz.it/";

    protected static String SEP3 = "/";

    protected static String CODE_VOL = "vol";

    protected static String CODE_VOL2 = "vol2";

    protected static String CODE_TIR = "tir";

    protected static String CODE_TUT = "tut";

    protected static String CODE_AUT_AMB = "aut-amb";
//    protected EAFieldAccessibility previstaAccessibilità;
//    protected EAFieldAccessibility ottenutaAccessibilità;
//    protected EACompanyRequired previstoCompany;
//    protected EACompanyRequired ottenutoCompany;

    protected static String CODE_PRI_AMB = "pri-amb";

    protected static String CODE_SEC_AMB = "sec-amb";

    protected static String CODE_TER_AMB = "ter-amb";

    protected static String CODE_AUT_MSA = "aut-msa";

    protected static String CODE_PRI_MSA = "pri-msa";

    protected static String CODE_SEC_MSA = "sec-msa";

    protected static String CODE_MAT = "mat";

    protected static String CODE_MSA = "msa-mat";

    protected static String CODE_AMB = "amb-mat";

    private static String SEP1 = ": ";

    private static String SEP2 = " -> ";

    @InjectMocks
    public AArrayService array;

    @InjectMocks
    public ATextService text;

    @InjectMocks
    protected AAnnotationService annotation;

    @InjectMocks
    protected AReflectionService reflection;

    protected Field reflectionJavaField;

    protected String sorgente = "";

    protected String previsto = "";

    protected String ottenuto = "";

    protected boolean previstoBooleano;

    protected boolean ottenutoBooleano;

    protected int sorgenteIntero = 0;

    protected int previstoIntero = 0;

    protected int ottenutoIntero = 0;

    protected long sorgenteLungo = 0;

    protected long previstoLungo = 0;

    protected long ottenutoLungo = 0;

    protected double previstoDouble = 0;

    protected double ottenutoDouble = 0;

    protected List<String> previstoList;

    protected List<String> sorgenteList;

    protected List<String> ottenutoList;

    protected ArrayList<Field> previstoFieldList;

    protected ArrayList<Field> ottenutoFieldList;

    protected EAFieldType previstoType;

    protected EAFieldType ottenutoType;

    protected Funzione funzioneVol;

    protected Funzione funzioneVol2;

    protected Funzione funzioneTir;

    protected Funzione funzioneTut;

    protected Funzione funzioneAutAmb;

    protected Funzione funzionePriAmb;

    protected Funzione funzioneSecAmb;

    protected Funzione funzioneTerAmb;

    protected Funzione funzioneAutMsa;

    protected Funzione funzionePriMsa;

    protected Funzione funzioneSecMsa;

    protected Servizio servizioMat;

    protected Servizio servizioMsaMat;

    protected Servizio servizioAmbMat;

    protected Funzione funzione;

    protected Croce croceGaps;

    protected Croce croceCrf;

    protected List<Funzione> listaFunzioniPreviste = null;

    protected List<Funzione> listaFunzioniOttenute = null;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected FunzioneService funzioneService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ServizioService servizioService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected CroceService croceService;


    protected void setUpTest() {
        MockitoAnnotations.initMocks(annotation);
        MockitoAnnotations.initMocks(reflection);
        MockitoAnnotations.initMocks(array);
        MockitoAnnotations.initMocks(text);
        annotation.reflection = reflection;
        annotation.text = text;
        array.text = text;
        annotation.array = array;
        reflection.array = array;
        FIELD_ORDINE = reflection.getField(ROLE_ENTITY_CLASS, NAME_ORDINE);
        FIELD_CODE = reflection.getField(ROLE_ENTITY_CLASS, NAME_CODE);
        Assert.assertNotNull(funzioneService);
        Assert.assertNotNull(servizioService);

        //--recupera una croce esistente
        croceGaps = croceService.getGAPS();
        croceCrf = croceService.getCRF();

        //--recupera una funzione esistente
        funzioneVol = funzioneService.findByKeyUnica(croceGaps, CODE_VOL);
        funzioneVol2 = funzioneService.findByKeyUnica(croceGaps, CODE_VOL2);
        funzioneTir = funzioneService.findByKeyUnica(croceGaps, CODE_TIR);
        funzioneTut = funzioneService.findByKeyUnica(croceGaps, CODE_TUT);
        funzioneAutAmb = funzioneService.findByKeyUnica(croceCrf, CODE_AUT_AMB);
        funzionePriAmb = funzioneService.findByKeyUnica(croceCrf, CODE_PRI_AMB);
        funzioneSecAmb = funzioneService.findByKeyUnica(croceCrf, CODE_SEC_AMB);
        funzioneTerAmb = funzioneService.findByKeyUnica(croceCrf, CODE_TER_AMB);
        funzioneAutMsa = funzioneService.findByKeyUnica(croceCrf, CODE_AUT_MSA);
        funzionePriMsa = funzioneService.findByKeyUnica(croceCrf, CODE_PRI_MSA);
        funzioneSecMsa = funzioneService.findByKeyUnica(croceCrf, CODE_SEC_MSA);

        //--recupera un servizio esistente
        servizioMat = servizioService.findByKeyUnica(croceGaps, CODE_MAT);
        servizioMsaMat = servizioService.findByKeyUnica(croceCrf, CODE_MSA);
        servizioAmbMat = servizioService.findByKeyUnica(croceCrf, CODE_AMB);
    }// end of method


    protected void print(String message, String sorgente, Object ottenuto) {
        System.out.println(message + SEP1 + sorgente + SEP2 + ottenuto);
    }// end of single test

}// end of class
