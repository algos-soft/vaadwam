package it.algos.vaadflow.modules.mese;

import it.algos.vaadflow.modules.secolo.EASecolo;
import it.algos.vaadflow.service.ATextService;

import java.time.Year;
import java.util.ArrayList;

public enum EAMese {

    gennaio("gen", "gennaio", 31, 31),
    febbraio("feb", "febbraio", 28, 29),
    marzo("mar", "marzo", 31, 31),
    aprile("apr", "aprile", 30, 30),
    maggio("mag", "maggio", 31, 31),
    giugno("giu", "giugno", 30, 30),
    luglio("lug", "luglio", 31, 31),
    agosto("ago", "agosto", 31, 31),
    settembre("set", "settembre", 30, 30),
    ottobre("ott", "ottobre", 31, 31),
    novembre("nov", "novembre", 30, 30),
    dicembre("dic", "dicembre", 31, 31);


    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    String breve;
    String lungo;
    int giorni;
    int giorniBis;

    /**
     * Costruttore interno dell'Enumeration
     */
    EAMese(String breve, String lungo, int giorni, int giorniBis) {
        this.breve = breve;
        this.lungo = lungo;
        this.giorni = giorni;
        this.giorniBis = giorniBis;
    }// fine del costruttore interno


    /**
     * Numero di giorni del mese
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     * @param anno            l'anno di riferimento (per sapere se è bisestile)
     *
     * @return Numero di giorni del mese
     */
    public static int getGiorni(int numMeseDellAnno, int anno) {
        int giorniDelMese = 0;
        EAMese mese = getMese(numMeseDellAnno);

        if (mese != null) {
            if (!Year.of(anno).isLeap()) {
                giorniDelMese = mese.giorni;
            } else {
                giorniDelMese = mese.giorniBis;
            }
        }// fine del blocco if

        return giorniDelMese;
    }// end of static method


    /**
     * Mese
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     *
     * @return Mese
     */
    public static EAMese getMese(int numMeseDellAnno) {
        EAMese mese = null;

        if (numMeseDellAnno > 0 && numMeseDellAnno < 13) {
            numMeseDellAnno = numMeseDellAnno - 1;
            for (EAMese meseTmp : EAMese.values()) {
                if (meseTmp.ordinal() == numMeseDellAnno) {
                    mese = meseTmp;
                }// fine del blocco if
            }// end of for cycle
        }// fine del blocco if

        return mese;
    }// end of static method


    /**
     * Mese
     *
     * @param nomeBreveLungo Nome breve o lungo del mese
     *
     * @return Mese
     */
    public static EAMese getMese(String nomeBreveLungo) {
        EAMese mese = null;
        String nomeBreveLungoMinuscolo;

        if (nomeBreveLungo != null && !nomeBreveLungo.equals("")) {
            nomeBreveLungoMinuscolo = nomeBreveLungo.toLowerCase();
            for (EAMese meseTmp : EAMese.values()) {
                if (meseTmp.breve.equals(nomeBreveLungoMinuscolo) || meseTmp.lungo.equals(nomeBreveLungoMinuscolo)) {
                    mese = meseTmp;
                }// fine del blocco if
            }// end of for cycle
        }// fine del blocco if

        return mese;
    }// end of method


    /**
     * Numero del mese nell'anno
     *
     * @param nomeBreveLungo L'anno parte da gennaio che è il mese numero 1
     *
     * @return Numero del mese
     */
    public static int getOrder(String nomeBreveLungo) {
        int numMeseDellAnno = 0;
        EAMese mese = getMese(nomeBreveLungo);

        if (mese != null) {
            numMeseDellAnno = mese.ordinal();
            numMeseDellAnno = numMeseDellAnno + 1;
        }// fine del blocco if

        return numMeseDellAnno;
    }// end of method


    // l'anno parte da gennaio che è il numero 1
    private static String getMese(int ord, boolean flagBreve) {
        String nome = "";
        EAMese mese = null;

        mese = getMese(ord);
        if (mese != null) {
            if (flagBreve) {
                nome = mese.breve;
            } else {
                nome = mese.lungo;
            }// fine del blocco if-else
        }// fine del blocco if

        return nome;
    }// end of static method


    /**
     * Nome breve del mese
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     *
     * @return Nome breve del mese
     */
    public String getShort(int numMeseDellAnno) {
        return getMese(numMeseDellAnno, true);
    }// end of method


    /**
     * Nome completo del mese
     *
     * @param numMeseDellAnno L'anno parte da gennaio che è il mese numero 1
     *
     * @return Nome breve del mese
     */
    public static String getLong(int numMeseDellAnno) {
        return getMese(numMeseDellAnno, false);
    }// end of static method


    /**
     * Elenco di tutti i nomi in forma breve
     *
     * @return Stringa dei nomi brevi separati da virgola
     */
    public String getAllShortString() {
        String stringa = "";
        String sep = ", ";

        for (EAMese mese : EAMese.values()) {
            stringa += mese.breve;
            stringa += sep;
        }// end of for cycle
        stringa = text.levaCoda(stringa, sep);

        return stringa;
    }// end of method


    /**
     * Elenco di tutti i nomi in forma completa
     *
     * @return Stringa dei nomi completi separati da virgola
     */
    public String getAllLongString() {
        String stringa = "";
        String sep = ", ";

        for (EAMese mese : EAMese.values()) {
            stringa += mese.lungo;
            stringa += sep;
        }// end of for cycle
        stringa = text.levaCoda(stringa, sep);

        return stringa;
    }// end of method


    /**
     * Elenco di tutti i nomi in forma breve
     *
     * @return Array dei nomi brevi
     */
    public ArrayList<String> getAllShortList() {
        ArrayList<String> lista = new ArrayList<String>();

        for (EAMese mese : EAMese.values()) {
            lista.add(mese.breve);
        }// end of for cycle

        return lista;
    }// end of method


    /**
     * Elenco di tutti i nomi in forma completa
     *
     * @return Array dei nomi completi
     */
    public ArrayList<String> getAllLongList() {
        ArrayList<String> lista = new ArrayList<String>();

        for (EAMese mese : EAMese.values()) {
            lista.add(mese.lungo);
        }// end of for cycle

        return lista;
    }// end of method

    @Override
    public String toString() {
        return lungo;
    }// end of method


    public int getOrd() {
        return this.ordinal() + 1;
    }// fine del metodo

    public String getBreve() {
        return breve;
    }// end of getter method


    public String getLungo() {
        return lungo;
    }// end of getter method


    public int getGiorni() {
        return giorni;
    }// end of getter method


    public int getGiorniBis() {
        return giorniBis;
    }// end of getter method

}// fine della classe Enumeration
