package it.algos.vaadwam.tabellonesuperato;


import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.turno.Turno;

import java.time.LocalDate;

/**
 * Contenitore degli elementi necessari per creare una riga di tabellonesuperato.
 * Questo non è un componente grafico ma solo un wrapper.
 * Questo wrapper è dato in pasto all'engine che crea i componenti grafici visualizzati nel tabellonesuperato.
 * Created by alex on 01/03/16.
 */
public class WRigaTab {

    private Servizio servizio;
    private Turno[] turni;

    // se è l'ultima di un gruppo di righe dello stesso servizio variabile
    // serve per abilitare il bottone crea nuovo servizio solo sull'ultima riga del gruppo
    private boolean ultimaDelGruppo=false;

    public WRigaTab(Servizio servizio, Turno[] turni) {
        this.servizio = servizio;
        this.turni = turni;
        this.ultimaDelGruppo=true;
    }

    public Servizio getServizio() {
        return servizio;
    }

    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
    }

    public Turno[] getTurni() {
        return turni;
    }

    public void setTurni(Turno[] turni) {
        this.turni = turni;
    }

    /**
     * Ritorna la data minima tra le date dei turni
     *
     * @return la data minima
     */
    public LocalDate getMinDate() {
        LocalDate date = null;
//        for (Turno t : turni) {
//            LocalDate inizioTurno = t.getInizio().toLocalDate();
//            if (inizioTurno != null) {
//                if (date == null || inizioTurno.isBefore(date)) {
//                    date = inizioTurno;
//                }
//            }
//        }
        return date;
    }

    /**
     * Ritorna la data massima tra le date dei turni
     *
     * @return la data massima
     */
    public LocalDate getMaxDate() {
        LocalDate date = null;
//        for (Turno t : turni) {
//            LocalDate fineTurno = t.getFine().toLocalDate();
//            if(fineTurno!=null){
//                if (date == null || fineTurno.isAfter(date)) {
//                    date = fineTurno;
//                }
//            }
//        }
        return date;
    }

    /**
     * Ritorna il turno corrispondente a una certa data
     * @return il turno, null se non previsto turno nella data specificata
     */
    public Turno getTurno(LocalDate currDate) {
        Turno turno=null;
        if(turni!=null){
//            for(Turno t : turni){
//                LocalDate dStart=(t.getInizio().toLocalDate());
//                if(dStart.equals(currDate)){
//                    turno=t;
//                    break;
//                }
//            }
        }
        return turno;
    }

    public boolean isUltimaDelGruppo() {
        return ultimaDelGruppo;
    }

    public void setUltimaDelGruppo(boolean ultimaDelGruppo) {
        this.ultimaDelGruppo = ultimaDelGruppo;
    }
}
