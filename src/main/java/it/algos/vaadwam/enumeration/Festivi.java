package it.algos.vaadwam.enumeration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 04-dic-2020
 * Time: 20:00
 */
public enum Festivi {
    capodanno("1 gennaio", 1, 1, 1, 1, 1),
    epifania("6 gennaio", 6, 6, 6, 6, 6),
    //    carnevale("14 febbraio", 65, 50, 41, 0, 46),
    pasqua("4 aprile", 94, 99, 90, 110, 106),
    pasquetta("5 aprile", 95, 100, 96, 88, 107),
    liberazione("25 aprile", 115, 115, 115, 115, 115),
    lavoro("1 maggio", 121, 122, 121, 121, 121),
    repubblica("2 giugno", 153, 154, 153, 153, 153),
    ferragosto("15 agosto", 227, 228, 227, 227, 227),
    ognissanti("1 novembre", 305, 306, 305, 305, 305),
    immacolata("8 dicembre", 342, 343, 342, 342, 342),
    natale("25 dicembre", 359, 360, 359, 359, 359),
    stefano("26 dicembre", 360, 361, 360, 360, 360),
    ;

    private int anno21;

    private int anno22;

    private int anno23;

    private int anno24;

    private int anno25;

    private String giornoTxt;

    /**
     * Costruttore con parametri.
     */
    Festivi(String giornoTxt, int anno21, int anno22, int anno23, int anno24, int anno25) {
        this.giornoTxt = giornoTxt;
        this.setAnno21(anno21);
        this.setAnno22(anno22);
        this.setAnno23(anno23);
        this.setAnno24(anno24);
        this.setAnno25(anno25);
    }// fine del metodo costruttore

    //--restituisce tutti e solo i giorni festivi dell' anno
    public static List<Integer> all2021() {
        List<Integer> giorni = new ArrayList<Integer>();
        List<Festivi> festivi = Arrays.asList(values());
        Festivi festivo;
        int numProgressivoGiorno;

        for (Festivi fest : festivi) {
            numProgressivoGiorno = fest.getAnno21();
            if (numProgressivoGiorno > 0) {
                giorni.add(numProgressivoGiorno);
            }// fine del blocco if
        }

        return giorni;
    }// fine del metodo statico

    //--restituisce tutti e solo i giorni festivi dell' anno
    public static List<LocalDate> festivi2021() {
        List<LocalDate> listaGiorni = new ArrayList<>();
        List<Festivi> festivi = Arrays.asList(values());
        LocalDate giorno;
        int num;
        String nome;

        for (Festivi fest : festivi) {
            num = fest.getAnno21();
            nome = fest.getGiornoTxt();
            giorno = LocalDate.ofYearDay(2021, num);
            if (giorno != null) {
                listaGiorni.add(giorno);
            }
        }

        return listaGiorni;
    }// fine del metodo statico

    //    //--restituisce tutti e solo i giorni festivi dell' anno
    //    public  static  ArrayList<Integer> all(String anno) {
    //        ArrayList<Integer> giorni = new ArrayList<Integer>()
    //        ArrayList anniValidi = [
    //        '2011', '2012', '2013', '2014', '2015', '2016', '2017']
    //
    //        if (anno && anno in anniValidi){
    //            switch (anno) {
    //                case anniValidi[0]:
    //                    giorni = all2011()
    //                    break
    //                case anniValidi[1]:
    //                    giorni = all2012()
    //                    break
    //                case anniValidi[2]:
    //                    giorni = all2013()
    //                    break
    //                case anniValidi[3]:
    //                    giorni = all2014()
    //                    break
    //                case anniValidi[4]:
    //                    giorni = all2015()
    //                    break
    //                case anniValidi[5]:
    //                    giorni = all2016()
    //                    break
    //                case anniValidi[6]:
    //                    giorni = all2017()
    //                    break
    //                default: // caso non definito
    //                    break
    //            } // fine del blocco switch
    //        }// fine del blocco if
    //
    //        return giorni
    //    }// fine del metodo statico

    public String getGiornoTxt() {
        return giornoTxt;
    }

    public void setGiornoTxt(String giornoTxt) {
        this.giornoTxt = giornoTxt;
    }

    public int getAnno21() {
        return anno21;
    }

    public void setAnno21(int anno21) {
        this.anno21 = anno21;
    }

    public int getAnno22() {
        return anno22;
    }

    public void setAnno22(int anno22) {
        this.anno22 = anno22;
    }

    public int getAnno23() {
        return anno23;
    }

    public void setAnno23(int anno23) {
        this.anno23 = anno23;
    }

    public int getAnno24() {
        return anno24;
    }

    public void setAnno24(int anno24) {
        this.anno24 = anno24;
    }

    public int getAnno25() {
        return anno25;
    }

    public void setAnno25(int anno25) {
        this.anno25 = anno25;
    }
} // fine della classe

