package it.algos.vaadflow.enumeration;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mar, 07-apr-2020
 * Time: 09:27
 */
public enum EARegione {
    abruzzo("Abruzzo", "dell'Abruzzo", "Comune,Provincia", 305, 1, 2),
    basilicata("Basilicata", "della Basilicata", "Comune,Provincia", 131, 1, 2),
    calabria("Calabria", "della Calabria", "Comune,Provincia", 404, 1, 2),
    campania("Campania", "della Campania", "Comune,Area", 550, 1, 2),
    emilia("Emilia-Romagna", "dell'Emilia-Romagna", "Comune,Provincia", 328, 1, 2),
    friuli("Friuli-Venezia Giulia", "del Friuli-Venezia Giulia", "Comune,ex Provincia", 215, 1, 2),
    lazio("Lazio", "del Lazio", "Comune,Provincia", 378, 1, 2),
    liguria("Liguria", "della Liguria", "Comune,Provincia", 234, 1, 2),
    lombardia("Lombardia", "della Lombardia", "Comune,Provincia", 1506, 1, 2),
    marche("Marche", "delle Marche", "Comune,Provincia", 228, 1, 2),
    molise("Molise", "del Molise", "pos.,comune,provincia", 136, 2, 3),
    piemonte("Piemonte", "del Piemonte", "Comune,Città metropolitana", 1181, 1, 2),
    puglia("Puglia", "della Puglia", "Comune,Provincia", 257, 1, 2),
    sardegna("Sardegna", "della Sardegna", "Comune,Toponimo in lingua locale", 377, 1, 3),
    sicilia("Sicilia", "della Sicilia", "Comune,Provincia", 390, 1, 2),
    toscana("Toscana", "della Toscana", "Comune,Provincia", 273, 1, 2),
    trentino("Trentino-Alto Adige", "del Trentino-Alto Adige", "Comune,Provincia", 282, 1, 2),
    umbria("Umbria", "dell'Umbria", "Comune,Provincia", 92, 1, 2),
    aosta("Valle d'Aosta", "della Valle d'Aosta", "Comune,Popolazione", 74, 1, 0),
    veneto("Veneto", "del Veneto", "Comune,Provincia", 563, 1, 2),
    ;

    static String COMUNI = "Comuni ";

    String nome;

    String paginaWikiShort;

    String titoli;

    int comuniTotali;

    int posColonnaUno;

    int posColonnaDue;


    EARegione(String nome, String paginaWikiShort, String titoli, int comuniTotali, int posColonnaUno, int posColonnaDue) {
        this.nome = nome;
        this.paginaWikiShort = paginaWikiShort;
        this.titoli = titoli;
        this.comuniTotali = comuniTotali;
        this.posColonnaUno = posColonnaUno;
        this.posColonnaDue = posColonnaDue;
    }// end of constructor


    public String getNome() {
        return nome;
    }// end of method


    public String getPaginaWikiShort() {
        return paginaWikiShort;
    }// end of method


    public String getPaginaWiki() {
        return COMUNI + paginaWikiShort;
    }// end of method


    public String getTitoli() {
        return titoli;
    }// end of method


    public int getComuniTotali() {
        return comuniTotali;
    }// end of method


    public int getPosColonnaUno() {
        return posColonnaUno;
    }// end of method


    public int getPosColonnaDue() {
        return posColonnaDue;
    }// end of method

}// end of enum class
