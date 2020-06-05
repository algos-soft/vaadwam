package it.algos.vaadflow.modules.secolo;


/**
 * Created by gac on 15/10/14.
 * .
 */
public enum EASecolo {

    XXac("XX", 2000, 1901, true),

    XIXac("XIX", 1900, 1801, true),

    XVIIIac("XVII", 1800, 1701, true),

    XVIIac("XVII", 1700, 1601, true),

    XVIac("XVI", 1600, 1501, true),

    XVac("XV", 1500, 1401, true),

    XIVac("XIV", 1400, 1301, true),

    XIIIac("XIII", 1300, 1201, true),

    XIIac("XII", 1200, 1101, true),

    XIac("XI", 1100, 1001, true),

    Xac("X", 1000, 901, true),

    IXac("IX", 900, 801, true),

    VIIIac("VIII", 800, 701, true),

    VIIac("VII", 700, 601, true),

    VIac("VI", 600, 501, true),

    Vac("V", 500, 401, true),

    IVac("IV", 400, 301, true),

    IIIac("III", 300, 201, true),

    IIac("II", 200, 101, true),

    Iac("I", 100, 1, true),

    I("I", 1, 100, false),

    II("II", 101, 200, false),

    III("III", 201, 300, false),

    IV("IV", 301, 400, false),

    V("V", 401, 500, false),

    VI("VI", 501, 600, false),

    VII("VII", 601, 700, false),

    VIII("VIII", 701, 800, false),

    IX("IX", 801, 900, false),

    X("X", 901, 1000, false),

    XI("XI", 1001, 1100, false),

    XII("XII", 1101, 1200, false),

    XIII("XIII", 1201, 1300, false),

    XIV("XIV", 1301, 1400, false),

    XV("XV", 1401, 1500, false),

    XVI("XVI", 1501, 1600, false),

    XVII("XVII", 1601, 1700, false),

    XVIII("XVIII", 1701, 1800, false),

    XIX("XIX", 1801, 1900, false),

    XX("XX", 1901, 2000, false),

    XXI("XXI", 2001, 2100, false);

    public final static String TAG_AC = " a.C.";

    private final static String SECOLO_DC = " secolo";

    private final static String SECOLO_AC = SECOLO_DC + TAG_AC;

    private String titolo;

    private int inizio;

    private int fine;

    private boolean anteCristo;


    /**
     * Costruttore completo con parametri.
     *
     * @param titolo     del secolo
     * @param inizio     primo anno
     * @param fine       ultimo anno
     * @param anteCristo flag booleano
     */
    EASecolo(String titolo, int inizio, int fine, boolean anteCristo) {
        if (anteCristo) {
            this.setTitolo(titolo + SECOLO_AC);
        } else {
            this.setTitolo(titolo + SECOLO_DC);
        }// fine del blocco if-else
        this.setInizio(inizio);
        this.setFine(fine);
        this.setAnteCristo(anteCristo);
    } // fine del costruttore


    public static EASecolo getSecoloAC(int anno) {
        EASecolo secolo = null;
        int inizio;
        int fine;

        for (EASecolo secoloTmp : values()) {
            if (secoloTmp.anteCristo) {
                inizio = secoloTmp.inizio;
                fine = secoloTmp.fine;
                if (anno >= fine && anno <= inizio) {
                    secolo = secoloTmp;
                }// fine del blocco if
            }// fine del blocco if
        }// end of for cycle

        return secolo;
    }// end of method


    public static EASecolo getSecoloDC(int anno) {
        EASecolo secolo = null;
        int inizio;
        int fine;

        for (EASecolo secoloTmp : values()) {
            if (!secoloTmp.anteCristo) {
                inizio = secoloTmp.inizio;
                fine = secoloTmp.fine;
                if (anno >= inizio && anno <= fine) {
                    secolo = secoloTmp;
                }// fine del blocco if
            }// fine del blocco if
        }// end of for cycle

        return secolo;
    }// end of method


    /**
     * Recupera l'enumeration dal titolo <br>
     *
     * @return enumeration trovata
     */
    public static EASecolo getSecolo(String titolo) {
        EASecolo secolo = null;

        for (EASecolo secoloTmp : values()) {
            if (secoloTmp.titolo.equals(titolo)) {
                secolo = secoloTmp;
            }// fine del blocco if
        }// end of for cycle

        return secolo;
    }//end of setter method


    /**
     * Recupera l'ordine della enumeration dal titolo <br>
     *
     * @return ordinamento
     */
    public static int getOrder(String titolo) {
        int ordine = 0;
        EASecolo secolo = getSecolo(titolo);

        if (secolo != null) {
            ordine = secolo.ordinal() + 1;
        }// end of if cycle

        return ordine;
    }//end of setter method


    public String getTextSecoloAC(int anno) {
        String titolo = "";
        EASecolo secolo = getSecoloAC(anno);

        if (secolo != null) {
            titolo = secolo.getTitolo();
        }// end of if cycle

        return titolo;
    }// end of method


    public String getTextSecoloDC(int anno) {
        String titolo = "";
        EASecolo secolo = getSecoloDC(anno);

        if (secolo != null) {
            titolo = secolo.getTitolo();
        }// end of if cycle

        return titolo;
    }// end of method


    public String getTitolo() {
        return titolo;
    }// end of getter method


    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }//end of setter method


    public int getInizio() {
        return inizio;
    }// end of getter method


    public void setInizio(int inizio) {
        this.inizio = inizio;
    }//end of setter method


    public int getFine() {
        return fine;
    }// end of getter method


    public void setFine(int fine) {
        this.fine = fine;
    }//end of setter method


    public boolean isAnteCristo() {
        return anteCristo;
    }// end of getter method


    public void setAnteCristo(boolean anteCristo) {
        this.anteCristo = anteCristo;
    }//end of setter method

} // fine della Enumeration
