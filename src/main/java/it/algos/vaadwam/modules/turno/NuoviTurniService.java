package it.algos.vaadwam.modules.turno;

import it.algos.vaadflow.service.AService;
import it.algos.vaadwam.enumeration.Festivi;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.servizio.ServizioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwam.application.WamCost.*;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 04-dic-2020
 * Time: 17:58
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ANuoviTurniService.class); <br>
 * 3) @Autowired public ANuoviTurniService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class NuoviTurniService extends AService {

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private ServizioService servizioService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private TurnoService turnoService;

    /**
     * Costruttore <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public NuoviTurniService(TurnoRepository repository) {
        super();
    }


    public void nuoviTurniAnnuali(Croce croce, int anno) {
        LocalDate primoGennaio = date.primoGennaio(anno);
        int numGiorni = 365;
        List<LocalDate> festivi = new ArrayList<>();
        List<LocalDate> feriali = new ArrayList<>();
        LocalDate giorno;
        List<Integer> feste = Festivi.all2021();

        for (int k = 0; k < numGiorni; k++) {
            giorno = primoGennaio.plusDays(k);
            if (giorno.getDayOfWeek().getValue() == 6 || giorno.getDayOfWeek().getValue() == 7 || feste.contains(giorno.getDayOfYear())) {
                festivi.add(giorno);
            }
            else {
                feriali.add(giorno);
            }
        }

        //        System.out.println("Festivi");
        //        System.out.println("");
        //        for (LocalDate giorno2 : festivi) {
        //            System.out.println(date.getDataCompleta(giorno2));
        //        }
        //        System.out.println("");
        //        System.out.println("Feriali");
        //        System.out.println("");
        //        for (LocalDate giorno3 : feriali) {
        //            System.out.println(date.getDataCompleta(giorno3));
        //        }

        switch (croce.code) {
            case CroceService.PAP:
                nuoviTurniAnnualiPianoro(croce, feriali, festivi);
                break;
            case CroceService.CRPT:
                nuoviTurniAnnualiPonteTaro(croce, feriali, festivi);
                break;
            case CroceService.CRF:
                nuoviTurniAnnualiFidenza(croce, feriali, festivi);
                break;
            case CroceService.GAPS:
                nuoviTurniAnnualiGaps(croce, feriali, festivi);
            default:
                logger.warn("Switch - caso non definito", this.getClass(), "nomeDelMetodo");
                break;
        }
    }

    //--creazione dei turni vuoti per la croce Pianoro
    //--li crea SOLO se non esistono già
    //--logica
    //--    ambulanza  notte 0-7 -> feriali da lunedi a venerdi
    //--    ambulanza  mattina 7-14 -> feriali da lunedi a venerdi
    //--    ambulanza  pomeriggio 14-17 -> feriali da lunedi a venerdi
    //--    ambulanza  pomeriggio sera 17-20 -> feriali da lunedi a venerdi
    //--    ambulanza  sera 20-24 -> feriali da lunedi a venerdi
    //--    ambulanza  notte 0-8 -> sabato e domenica più festivi dell'anno
    //--    ambulanza  mattina 8-13 -> sabato e domenica più festivi dell'anno
    //--    ambulanza  pomeriggio 13-19 -> sabato e domenica più festivi dell'anno
    //--    ambulanza  sera 19-24 -> sabato e domenica più festivi dell'anno
    private void nuoviTurniAnnualiPianoro(Croce croce, List<LocalDate> feriali, List<LocalDate> festivi) {
        Servizio msaNotte = servizioService.findByKeyUnica(croce, PAP_SERVIZIO_LUNVEN_NOTTE);
        Servizio msaMat = servizioService.findByKeyUnica(croce, PAP_SERVIZIO_LUNVEN_MATTINA);
        Servizio msaPom = servizioService.findByKeyUnica(croce, PAP_SERVIZIO_LUNVEN_POMERIGGIO);
        Servizio msaPomSera = servizioService.findByKeyUnica(croce, PAP_SERVIZIO_LUNVEN_POMERIGGIOSERA);
        Servizio msaSera = servizioService.findByKeyUnica(croce, PAP_SERVIZIO_LUNVEN_SERA);
        Servizio msa2Notte = servizioService.findByKeyUnica(croce, PAP_SERVIZIO_SABDOM_NOTTE);
        Servizio msa2Mat = servizioService.findByKeyUnica(croce, PAP_SERVIZIO_SABDOM_MATTINA);
        Servizio msa2Pom = servizioService.findByKeyUnica(croce, PAP_SERVIZIO_SABDOM_POMERIGGIO);
        Servizio msa2Sera = servizioService.findByKeyUnica(croce, PAP_SERVIZIO_SABDOM_SERA);

        for (LocalDate giorno : feriali) {
            turnoService.creaIfNotExist(croce, giorno, msaNotte, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaMat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaPom, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaPomSera, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaSera, (List<Iscrizione>) null);
        }

        for (LocalDate giorno : festivi) {
            turnoService.creaIfNotExist(croce, giorno, msa2Notte, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msa2Mat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msa2Pom, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msa2Sera, (List<Iscrizione>) null);
        }
    }


    //--creazione dei turni vuoti per la croce PonteTaro
    //--li crea SOLO se non esistono già
    //--logica
    //--    ambulanza  mattina -> tutti i giorni
    //--    ambulanza  pomeriggio -> tutti i giorni
    //--    ambulanza  notte -> tutti i giorni
    //--    dialisi uno andata -> mai
    //--    dialisi uno ritorno -> mai
    //--    dialisi due andata -> mai
    //--    dialisi due ritorno -> mai
    //--    ordinario singolo -> mai
    //--    ordinario doppio -> mai
    //--    extra -> mai
    //--    servizi -> mai
    //--    centralino mattino -> tutti i giorni
    //--    centralino pomeriggio -> tutti i giorni
    private void nuoviTurniAnnualiPonteTaro(Croce croce, List<LocalDate> feriali, List<LocalDate> festivi) {
        Servizio ambMat = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_AMBULANZA_MATTINO);
        Servizio ambPom = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_AMBULANZA_POMERIGGIO);
        Servizio ambNotte = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_AMBULANZA_NOTTE);
        Servizio diaUnoAnd = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_DIALISI_UNO_ANDATA);
        Servizio diaUnoRit = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_DIALISI_UNO_RITORNO);
        Servizio diaDueAnd = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_DIALISI_DUE_ANDATA);
        Servizio diaDueRit = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_DIALISI_DUE_RITORNO);
        Servizio ordSin = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_ORDINARIO_SINGOLO);
        Servizio ordDop = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_ORDINARIO_DOPPIO);
        Servizio extra = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_EXTRA);
        Servizio servizi = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_SERVIZI);
        Servizio cenMat = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_CENTRALINO_MATTINO);
        Servizio cenPom = servizioService.findByKeyUnica(croce, CRPT_SERVIZIO_CENTRALINO_POMERIGGIO);

        for (LocalDate giorno : feriali) {
            turnoService.creaIfNotExist(croce, giorno, ambMat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, ambPom, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, ambNotte, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, cenMat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, cenPom, (List<Iscrizione>) null);
        }

        for (LocalDate giorno : festivi) {
            turnoService.creaIfNotExist(croce, giorno, ambMat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, ambPom, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, ambNotte, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, cenMat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, cenPom, (List<Iscrizione>) null);
        }
    }

    //--creazione dei turni vuoti per la croce Fidenza
    //--li crea SOLO se non esistono già
    //--logica
    //--    automedica  mattina -> tutti i giorni
    //--    automedica  pomeriggio -> tutti i giorni
    //--    automedica  notte -> tutti i giorni
    //--    ambulanza  mattina -> solo sabato e domenica
    //--    ambulanza  pomeriggio -> solo sabato e domenica
    //--    ambulanza  notte -> tutti i giorni
    //--    extra -> mai
    private void nuoviTurniAnnualiFidenza(Croce croce, List<LocalDate> feriali, List<LocalDate> festivi) {
        Servizio msaMat = servizioService.findByKeyUnica(croce, CRF_SERVIZIO_AUTOMEDICA_MATTINO);
        Servizio msaPom = servizioService.findByKeyUnica(croce, CRF_SERVIZIO_AUTOMEDICA_POMERIGGIO);
        Servizio msaNotte = servizioService.findByKeyUnica(croce, CRF_SERVIZIO_AUTOMEDICA_NOTTE);
        Servizio ambMat = servizioService.findByKeyUnica(croce, CRF_SERVIZIO_AMBULANZA_MATTINO);
        Servizio ambPom = servizioService.findByKeyUnica(croce, CRF_SERVIZIO_AMBULANZA_POMERIGGIO);
        Servizio ambNotte = servizioService.findByKeyUnica(croce, CRF_SERVIZIO_AMBULANZA_NOTTE);

        for (LocalDate giorno : feriali) {
            turnoService.creaIfNotExist(croce, giorno, msaMat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaPom, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaNotte, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, ambNotte, (List<Iscrizione>) null);
        }

        for (LocalDate giorno : festivi) {
            turnoService.creaIfNotExist(croce, giorno, msaMat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaPom, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaNotte, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, ambNotte, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, ambMat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, ambPom, (List<Iscrizione>) null);
        }
    }

    private void nuoviTurniAnnualiGaps(Croce croce, List<LocalDate> feriali, List<LocalDate> festivi) {
    }

}