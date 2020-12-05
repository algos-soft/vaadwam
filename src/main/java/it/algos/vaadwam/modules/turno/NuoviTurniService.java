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

        Servizio msaNotte = servizioService.findByKeyUnica(croce, "msa-notte");
        Servizio msaMat = servizioService.findByKeyUnica(croce, "msa-mat");
        Servizio msaPom = servizioService.findByKeyUnica(croce, "msa-pom");
        Servizio msaPomsera = servizioService.findByKeyUnica(croce, "msa-pomsera");
        Servizio msaSera = servizioService.findByKeyUnica(croce, "msa-sera");
        Servizio msa2Notte = servizioService.findByKeyUnica(croce, "msa2-notte");
        Servizio msa2Mat = servizioService.findByKeyUnica(croce, "msa2-mat");
        Servizio msa2Pom = servizioService.findByKeyUnica(croce, "msa2-pom");
        Servizio msa2Sera = servizioService.findByKeyUnica(croce, "msa2-sera");

        for (LocalDate giorno : feriali) {
            turnoService.creaIfNotExist(croce, giorno, msaNotte, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaMat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaPom, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaPomsera, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msaSera, (List<Iscrizione>) null);
        }

        for (LocalDate giorno : festivi) {
            turnoService.creaIfNotExist(croce, giorno, msa2Notte, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msa2Mat, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msa2Pom, (List<Iscrizione>) null);
            turnoService.creaIfNotExist(croce, giorno, msa2Sera, (List<Iscrizione>) null);
        }
    }


    private boolean isFestivo(List<LocalDate> festivi, LocalDate giorno) {

        return false;
    }

    private void nuoviTurniAnnualiPonteTaro(Croce croce, List<LocalDate> feriali, List<LocalDate> festivi) {
    }

    private void nuoviTurniAnnualiFidenza(Croce croce, List<LocalDate> feriali, List<LocalDate> festivi) {
    }

    private void nuoviTurniAnnualiGaps(Croce croce, List<LocalDate> feriali, List<LocalDate> festivi) {
    }

}