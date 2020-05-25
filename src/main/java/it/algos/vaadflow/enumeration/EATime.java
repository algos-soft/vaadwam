package it.algos.vaadflow.enumeration;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 27-set-2018
 * Time: 16:23
 */
public enum EATime {

    /**
     * Pattern: d-MM <br>
     * Esempio: 5-10 <br>
     */
    meseShort("meseShort", "d-MM", "5-10"),
    /**
     * Pattern: d-MMM <br>
     * Esempio: 5-ott <br>
     */
    meseLong("meseLong", "d-MMM", "5-ott"),
    /**
     * Pattern: d-MM-yy <br>
     * Esempio: 5-10-14 <br>
     */
    shortDate("short", "d-MM-yy", "5-10-14"),
    /**
     * Pattern: d-MMM-yy <br>
     * Esempio: 5-ott-14 <br>
     */
    normal("normal", "d-MMM-yy", "5-ott-14"),
    /**
     * Pattern: EEE d <br>
     * Esempio: dom 5 <br>
     */
    weekShort("weekShort", "EEE d", "dom 5"),
    /**
     * Pattern: EEE, d MMM <br>
     * Esempio: dom, 5 apr <br>
     */
    weekShortMese("weekShortMese", "EEE d MMM", "dom 5 apr"),
    /**
     * Pattern: d MMMM <br>
     * Esempio: 5 ottobre <br>
     */
    mese("mese", "d MMMM", "5 ottobre"),
    /**
     * Pattern: EEEE d <br>
     * Esempio: domenica 5 <br>
     */
    weekLong("weekLong", "EEEE d", "domenica 5"),
    /**
     * Pattern: d-MMMM-yy <br>
     * Esempio: 5-ottobre-14 <br>
     */
    medium("medium", "d-MMMM-yy", "5-ottobre-14"),
    /**
     * Pattern: d-MMMM-yyy <br>
     * Esempio: 5-ottobre-2014 <br>
     */
    lunga("lunga", "d-MMMM-yyy", "5-ottobre-2014"),
    /**
     * Pattern: EEEE, d-MMMM-yyy <br>
     * Esempio: domenica, 5-ottobre-2014 <br>
     */
    completa("completa", "EEEE, d MMMM yyy", "domenica, 5 ottobre 2014"),
    /**
     * Pattern: MMMM yyy <br>
     * Esempio: ottobre 2014 <br>
     */
    meseCorrente("meseCorrente", "MMMM yyy", "ottobre 2014"),
    /**
     * Pattern: d MMM yyyy <br>
     * Esempio: 20 gen 2019 <br>
     */
    standard("standard", "d MMM yyyy", "20 gen 2019"),
    /**
     * ISO8601: yyyy-MM-dd'T'HH:mm:ss.SSSXXX <br>
     * Pattern: yyyy-MM-dd'T'HH:mm:ss <br>
     * Esempio: 2017-02-16T21:00:00.000+01:00 <br>
     */
    iso8601("iso8601", "yyyy-MM-dd'T'HH:mm:ss", "2017-02-16T21:00:00"),
    /**
     * Pattern: EEEE, d-MMMM-yyy 'alle' HH:mm <br>
     * Esempio: domenica, 5-ottobre-2014 alle 13:45<br>
     */
    completaOrario("completa", "EEEE, d-MMMM-yyy 'alle' HH:mm", "domenica, 5-ottobre-2014 alle 13:45"),
    ;

    private String tag;

    private String pattern;
    private String esempio;


    EATime(String tag, String pattern, String esempio) {
        this.tag = tag;
        this.pattern = pattern;
        this.esempio = esempio;
    }// end of constructor


    public String getTag() {
        return tag;
    }// end of method


    public void setTag(String tag) {
        this.tag = tag;
    }// end of method


    public String getPattern() {
        return pattern;
    }// end of method


    public void setPattern(String pattern) {
        this.pattern = pattern;
    }// end of method


    public String getEsempio() {
        return esempio;
    }// end of method


    public void setEsempio(String esempio) {
        this.esempio = esempio;
    }// end of method
}// end of enumeration
