package it.algos.vaadflow.importa;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EARegione;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.AWebService;
import it.algos.vaadflow.wrapper.WrapDueStringhe;
import it.algos.vaadflow.wrapper.WrapTreStringhe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VIRGOLA;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 06-apr-2020
 * Time: 15:25
 * Importazione delle provincie da Wikipedia <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ImportWiki {

    public static String KEY_SIGLA = "sigla";

    public static String KEY_NOME = "nome";

    public static String KEY_REGIONE = "regione";

    public static String PAGINA = "ISO 3166-2:IT";

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public AWebService aWebService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public ATextService text;


    public ImportWiki() {
    }// end of constructor


    public List<String> pagineComuni() {
        List<String> lista = new ArrayList<>();

//        lista.add("Comuni dell'Abruzzo");
//        lista.add("Comuni della Basilicata");
//        lista.add("Comuni della Calabria");
//        lista.add("Comuni della Campania");
//        lista.add("Comuni dell'Emilia-Romagna");
//        lista.add("Comuni del Friuli-Venezia Giulia");
//        lista.add("Comuni del Lazio");
//        lista.add("Comuni della Liguria");
//        lista.add("Comuni della Lombardia");
        lista.add("Comuni delle Marche");
        lista.add("Comuni del Molise");
//        lista.add("Comuni del Piemonte");
//        lista.add("Comuni della Puglia");
//        lista.add("Comuni della Sardegna");
//        lista.add("Comuni della Sicilia");
//        lista.add("Comuni della Toscana");
//        lista.add("Comuni del Trentino-Alto Adige");
//        lista.add("Comuni dell'Umbria");
//        lista.add("Comuni della Valle d'Aosta");
//        lista.add("Comuni del Veneto");

        return lista;
    }// end of method


    /**
     * Sorgente completo di una pagina web <br>
     *
     * @param paginaWiki
     *
     * @return sorgente
     */
    public String getSorgente(String paginaWiki) {
        String sorgente = null;

        sorgente = aWebService.leggeSorgenteWiki(paginaWiki);

        return sorgente;
    }// end of method


    /**
     * Import da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con due stringhe ognuno
     */
    @Deprecated
    public List<WrapDueStringhe> estraeListaDue(String pagina, String titoli, String posizioni) {
        List<WrapDueStringhe> listaWrap = null;
        LinkedHashMap<String, LinkedHashMap<String, String>> mappaGenerale = null;
        List<List<String>> matriceTable = null;
        LinkedHashMap<String, String> mappa;
        String[] titoliTable = text.getMatrice(titoli);
//        Integer[] posizioniColonne = text.getMatriceInt(posizioni);
//        String tagUno = titoliTable[posizioniColonne[0]];
//        String tagDue = titoliTable[posizioniColonne[1]];
        String tagUno = "";
        String tagDue = "";
        WrapDueStringhe wrapGrezzo;

        matriceTable = aWebService.getMatriceTableWiki(pagina, titoliTable);
        if (matriceTable != null && matriceTable.size() > 0) {
            listaWrap = new ArrayList<>();
            for (List riga : matriceTable) {
//                wrapGrezzo = new WrapDueStringhe(mappa.get(tagUno), mappa.get(tagDue));
//                listaWrap.add(wrapGrezzo);
            }// end of for cycle
        }// end of if cycle


//        mappaGenerale = aWebService.getMappaTableWiki(pagina, titoliTable);
//        if (mappaGenerale != null && mappaGenerale.size() > 0) {
//            listaWrap = new ArrayList<>();
//            for (String elemento : mappaGenerale.keySet()) {
//                mappa = mappaGenerale.get(elemento);
//                wrapGrezzo = new WrapDueStringhe(mappa.get(tagUno), mappa.get(tagDue));
//                listaWrap.add(wrapGrezzo);
//            }// end of for cycle
//        }// end of if cycle

        return listaWrap;
    }// end of method


    /**
     * Estrae una lista da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con due stringhe ognuno
     */
    public List<WrapDueStringhe> estraeListaDue(String pagina, String titoli, int posUno, int posDue) {
        List<WrapDueStringhe> listaWrap = null;
        List<List<String>> matriceTable = null;
        String[] titoliTable = text.getMatrice(titoli);
        WrapDueStringhe wrapGrezzo;

        matriceTable = aWebService.getMatriceTableWiki(pagina, titoliTable);
        if (matriceTable != null && matriceTable.size() > 0) {
            listaWrap = new ArrayList<>();
            for (List<String> riga : matriceTable) {
                wrapGrezzo = new WrapDueStringhe(riga.get(posUno - 1), posDue > 0 ? riga.get(posDue - 1) : VUOTA);
                listaWrap.add(wrapGrezzo);
            }// end of for cycle
        }// end of if cycle

        return listaWrap;
    }// end of method


    /**
     * Import da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con due stringhe ognuno
     */
    public List<WrapTreStringhe> estraeListaTre(String pagina, String titoli) {
        List<WrapTreStringhe> listaWrap = null;
        LinkedHashMap<String, LinkedHashMap<String, String>> mappaGenerale = null;
        LinkedHashMap<String, String> mappa;
        String[] titoliTable = text.getMatrice(titoli);
        String tagUno = titoliTable[0];
        String tagDue = titoliTable[1];
        String tagTre = titoliTable[2];
        WrapTreStringhe wrapGrezzo;

        mappaGenerale = aWebService.getMappaTableWiki(pagina, titoliTable);
        if (mappaGenerale != null && mappaGenerale.size() > 0) {
            listaWrap = new ArrayList<>();
            for (String elemento : mappaGenerale.keySet()) {
                mappa = mappaGenerale.get(elemento);
                wrapGrezzo = new WrapTreStringhe(mappa.get(tagUno), mappa.get(tagDue), mappa.get(tagTre));
                listaWrap.add(wrapGrezzo);
            }// end of for cycle
        }// end of if cycle

        return listaWrap;
    }// end of method


    /**
     * Import delle regioni da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno (sigla, nome, regione)
     */
    public String elaboraCodice(String testoGrezzo) {
        String testoValido = testoGrezzo.trim();
        String tagIni = "<code>";
        String tagEnd = "</code>";

        testoValido = text.levaTesta(testoValido, tagIni);
        testoValido = text.levaCoda(testoValido, tagEnd);
        testoValido = testoValido.substring(3);
        return testoValido;
    }// end of method


    /**
     * Import delle regioni da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno (sigla, nome, regione)
     */
    public String elaboraNome(String testoGrezzo) {
        String testoValido = testoGrezzo.trim();
        int posIni = 0;
        int posEnd = 0;

        posEnd = testoValido.lastIndexOf("</a>");
        if (posEnd > 0) {
            testoValido = testoValido.substring(0, posEnd);
        }// end of if cycle
        posIni = testoValido.lastIndexOf(">") + 1;
        testoValido = testoValido.substring(posIni);
        return testoValido;
    }// end of method


    /**
     * Import delle regioni da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno (sigla, nome, regione)
     */
    public List<WrapDueStringhe> regioni() {
        List<WrapDueStringhe> listaWrap = null;
        List<WrapDueStringhe> listaWrapGrezzo = null;
        WrapDueStringhe wrapValido;
        String titoli = "Codice,Regioni";
        String prima;
        String seconda;

        listaWrapGrezzo = estraeListaDue(PAGINA, titoli, 1, 2);
        if (listaWrapGrezzo != null && listaWrapGrezzo.size() > 0) {
            listaWrap = new ArrayList<>();
            for (WrapDueStringhe wrap : listaWrapGrezzo) {
                prima = wrap.getPrima();
                seconda = wrap.getSeconda();
                prima = elaboraCodice(prima);
                seconda = elaboraNome(seconda);
                wrapValido = new WrapDueStringhe(prima, seconda);
                listaWrap.add(wrapValido);
            }// end of for cycle
        }// end of if cycle

        return listaWrap;
    }// end of method


    /**
     * Import delle provincie da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno (sigla, nome, regione)
     */
    public List<WrapTreStringhe> provinceBase(String pagina, String titoli) {
        List<WrapTreStringhe> listaWrap = null;
        List<WrapTreStringhe> listaWrapGrezzo = null;
        WrapTreStringhe wrapValido;
        String[] titoliTable = text.getMatrice(titoli);
        String prima;
        String seconda;
        String terza;

        listaWrapGrezzo = estraeListaTre(PAGINA, titoli);
        if (listaWrapGrezzo != null && listaWrapGrezzo.size() > 0) {
            listaWrap = new ArrayList<>();
            for (WrapTreStringhe wrap : listaWrapGrezzo) {
                prima = wrap.getPrima();
                seconda = wrap.getSeconda();
                terza = wrap.getTerza();
                prima = elaboraCodice(prima);
                seconda = elaboraNome(seconda);
                terza = elaboraNome(terza);
                wrapValido = new WrapTreStringhe(prima, seconda, terza);
                listaWrap.add(wrapValido);
            }// end of for cycle
        }// end of if cycle

        return listaWrap;
    }// end of method


    /**
     * Import delle provincie da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno (sigla, nome, regione)
     */
    public List<WrapTreStringhe> province() {
        List<WrapTreStringhe> listaWrap = new ArrayList<>();
        List<WrapTreStringhe> listaWrapCitta = null;
        List<WrapTreStringhe> listaWrapProvince = null;

        WrapTreStringhe wrapValido;
        String tagCodice = "Codice";
        String tagCitta = "Città metropolitane";
        String tagProvince = "Province";
        String tagRegioni = "Nella regione";
        String titoliCitta = tagCodice + VIRGOLA + tagCitta + VIRGOLA + tagRegioni;
        String titoliProvince = tagCodice + VIRGOLA + tagProvince + VIRGOLA + tagRegioni;

        listaWrapCitta = provinceBase(PAGINA, titoliCitta);
        listaWrapProvince = provinceBase(PAGINA, titoliProvince);
        listaWrap.addAll(listaWrapCitta);
        listaWrap.addAll(listaWrapProvince);
        return listaWrap;
    }// end of method


    /**
     * Import dei comuni da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno (regione, provincia, nome)
     */
    public List<WrapDueStringhe> comuniBase(String regioneTxt, String pagina, String titoli, int posUno, int posDue) {
        List<WrapDueStringhe> listaWrap = null;
        List<WrapDueStringhe> listaWrapGrezzo = null;
        WrapDueStringhe wrapValido;
        String prima;
        String seconda;

        listaWrapGrezzo = estraeListaDue(pagina, titoli, posUno, posDue);
        if (text.isValid(regioneTxt) && listaWrapGrezzo != null && listaWrapGrezzo.size() > 0) {
            listaWrap = new ArrayList<>();
            for (WrapDueStringhe wrap : listaWrapGrezzo) {
                prima = wrap.getPrima();
                seconda = wrap.getSeconda();
                prima = elaboraNome(prima);
                seconda = elaboraNome(seconda);
                seconda = text.isValid(seconda) ? seconda : regioneTxt;
                if (text.isValid(prima)) {
                    wrapValido = new WrapDueStringhe(seconda, prima);
                    listaWrap.add(wrapValido);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return listaWrap;
    }// end of method


    /**
     * Import dei comuni di una singola regione <br>
     *
     * @return lista di wrapper con tre stringhe ognuno (regione, provincia, nome)
     */
    public List<WrapDueStringhe> singolaRegione(EARegione eaRegione) {
        List<WrapDueStringhe> listaWrap = null;
        String regioneTxt;
        String paginaWiki;
        String titoli;
        int posUno;
        int posDue;

        regioneTxt = eaRegione.getNome();
        paginaWiki = eaRegione.getPaginaWiki();
        titoli = eaRegione.getTitoli();
        posUno = eaRegione.getPosColonnaUno();
        posDue = eaRegione.getPosColonnaDue();
        if (text.isValid(titoli)) {
            listaWrap = comuniBase(regioneTxt, paginaWiki, titoli, posUno, posDue);
        }// end of if cycle

        return listaWrap;
    }// end of method


    /**
     * Import dei comuni dalle pagine di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno (regione, provincia, nome)
     */
    public List<WrapDueStringhe> comuni() {
        List<WrapDueStringhe> listaWrap = new ArrayList<>();
        List<WrapDueStringhe> listaWrapRegione = null;

        for (EARegione eaRegione : EARegione.values()) {
            listaWrapRegione = singolaRegione(eaRegione);
            listaWrap.addAll(listaWrapRegione);
        }// end of for cycle

        return listaWrap;
    }// end of method

}// end of class
