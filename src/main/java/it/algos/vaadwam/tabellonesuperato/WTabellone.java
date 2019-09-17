package it.algos.vaadwam.tabellonesuperato;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Contenitore di un insieme di righe di tabellonesuperato di tabellonesuperato.
 * Questo non è un componente grafico ma solo un wrapper.
 * Questo wrapper è dato in pasto all'engine che crea graficamente il tabellonesuperato.
 * Created by alex on 01/03/16.
 */
public class WTabellone extends ArrayList<WRigaTab>{

    private LocalDate d1;
    private LocalDate d2;


    public WTabellone(LocalDate d1, LocalDate d2) {
        this.d1 = d1;
        this.d2 = d2;
    }

    /**
     * Determina la data minima tra tutti i turni delle varie righe
     * @return la data minima
     */
    public LocalDate getMinDate() {
        LocalDate date=null;
        for(WRigaTab riga : this){
            LocalDate inizio= riga.getMinDate();
            if(inizio!=null){
                if (date==null || inizio.isBefore(date)){
                    date=inizio;
                }
            }
        }
        return date;
    }


    /**
     * Determina la data massima tra tutti i turni delle varie righe
     * @return la data massima
     */
    public LocalDate getMaxDate() {
        LocalDate date=null;
        for(WRigaTab riga : this){
            LocalDate fine= riga.getMaxDate();
            if(fine!=null){
                if (date==null || fine.isAfter(date)){
                    date=fine;
                }
            }
        }
        return date;
    }

    public LocalDate getD1() {
        return d1;
    }

    public LocalDate getD2() {
        return d2;
    }

}
