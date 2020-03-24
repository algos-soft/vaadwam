package it.algos.vaadflow.service;

import com.vaadin.flow.component.html.Label;
import it.algos.vaadflow.enumeration.EAFirstChar;
import it.algos.vaadflow.enumeration.EAPrefType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 13:45
 * <p>
 * Gestione e formattazione di stringhe di testo
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ATextService.class); <br>
 * 2) ATextService.getInstance(); <br>
 * 3) @Autowired private ATextService textService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Slf4j
public class ATextService extends AbstractService {

    /**
     * tag per il carattere punto
     */
    public static final String PUNTO = ".";

    /**
     * tag per il carattere barra
     */
    public static final String BARRA = "/";

    public static final String VIRGOLA = ",";

    public static final String REF = "<ref";

    public static final String NOTE = "<!--";

    public static final String GRAFFE = "{{";

    public static final int INT_NULLO = -1;

    public static final String PARENTESI = "(";

    public static final String INTERROGATIVO = "?";

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Private final property
     */
    private static final ATextService INSTANCE = new ATextService();


//    /**
//     * Private constructor to avoid client applications to use constructor
//     */
//    private ATextService() {
//    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static ATextService getInstance() {
        return INSTANCE;
    }// end of static method


    private static boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }// end of method


    /**
     * Null-safe, short-circuit evaluation.
     *
     * @param stringa in ingresso da controllare
     *
     * @return vero se la stringa è vuota o nulla
     */
    public boolean isEmpty(final String stringa) {
        return stringa == null || stringa.trim().isEmpty();
    }// end of method


    /**
     * Null-safe, short-circuit evaluation.
     *
     * @param stringa in ingresso da controllare
     *
     * @return vero se la stringa esiste è non è vuota
     */
    public boolean isValid(final String stringa) {
        return !isEmpty(stringa);
    }// end of method


    /**
     * Controlla che sia una stringa e che sia valida.
     *
     * @param obj in ingresso da controllare
     *
     * @return vero se la stringa esiste è non è vuota
     */
    public boolean isValid(final Object obj) {
        if (obj instanceof String) {
            return !isEmpty((String) obj);
        } else {
            return false;
        }// end of if/else cycle
    }// end of method


    /**
     * Forza il primo carattere della stringa secondo il flag
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     * @param flag    maiuscolo o minuscolo
     *
     * @return uscita string in uscita
     */
    private String primoCarattere(String testoIn, EAFirstChar flag) {
        String testoOut = "";
        String primo;
        String rimanente;

        if (this.isValid(testoIn)) {
            testoIn = testoIn.trim();
            primo = testoIn.substring(0, 1);
            switch (flag) {
                case maiuscolo:
                    primo = primo.toUpperCase();
                    break;
                case minuscolo:
                    primo = primo.toLowerCase();
                    break;
                default: // caso non definito
                    break;
            } // fine del blocco switch
            rimanente = testoIn.substring(1);
            testoOut = primo + rimanente;
        }// fine del blocco if

        return testoOut.trim();
    }// end of method


    /**
     * Forza il primo carattere della stringa al carattere maiuscolo
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return test formattato in uscita
     */
    public String primaMaiuscola(String testoIn) {
        return primoCarattere(testoIn, EAFirstChar.maiuscolo);
    }// end of method


    /**
     * Forza il primo carattere della stringa al carattere minuscolo
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return test formattato in uscita
     */
    public String primaMinuscola(String testoIn) {
        return primoCarattere(testoIn, EAFirstChar.minuscolo);
    }// end of method


    /**
     * Elimina dal testo il tagIniziale, se esiste
     * <p>
     * Esegue solo se il testo è valido
     * Se tagIniziale è vuoto, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn     ingresso
     * @param tagIniziale da eliminare
     *
     * @return test ridotto in uscita
     */
    public String levaTesta(String testoIn, String tagIniziale) {
        String testoOut = testoIn.trim();

        if (this.isValid(testoOut) && this.isValid(tagIniziale)) {
            tagIniziale = tagIniziale.trim();
            if (testoOut.startsWith(tagIniziale)) {
                testoOut = testoOut.substring(tagIniziale.length());
            }// fine del blocco if
        }// fine del blocco if

        return testoOut.trim();
    }// end of method


    /**
     * Elimina dal testo il tagFinale, se esiste
     * <p>
     * Esegue solo se il testo è valido
     * Se tagFinale è vuoto, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn   ingresso
     * @param tagFinale da eliminare
     *
     * @return test ridotto in uscita
     */
    public String levaCoda(final String testoIn, String tagFinale) {
        String testoOut = testoIn.trim();

        if (this.isValid(testoOut) && this.isValid(tagFinale)) {
            tagFinale = tagFinale.trim();
            if (testoOut.endsWith(tagFinale)) {
                testoOut = testoOut.substring(0, testoOut.length() - tagFinale.length());
            }// fine del blocco if
        }// fine del blocco if

        return testoOut.trim();
    }// end of method


    /**
     * Elimina il testo da tagFinale in poi
     * <p>
     * Esegue solo se il testo è valido
     * Se tagInterrompi è vuoto, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn       ingresso
     * @param tagInterrompi da dove inizia il testo da eliminare
     *
     * @return test ridotto in uscita
     */
    public String levaCodaDa(final String testoIn, String tagInterrompi) {
        String testoOut = testoIn.trim();

        if (this.isValid(testoOut) && this.isValid(tagInterrompi)) {
            tagInterrompi = tagInterrompi.trim();
            if (testoOut.contains(tagInterrompi)) {
                testoOut = testoOut.substring(0, testoOut.lastIndexOf(tagInterrompi));
            }// fine del blocco if
        }// fine del blocco if

        return testoOut.trim();
    }// end of method


    /**
     * Controlla se il testo contiene uno elemento di una lista di tag
     *
     * @param testoIn   ingresso
     * @param listaTags lista di tag da controllare
     *
     * @return vero se ne contiene uno o più di uno
     */
    public boolean isContiene(final String testoIn, ArrayList<String> listaTags) {
        boolean neContieneAlmenoUno = false;

        if (array.isValid(listaTags)) {
            for (String singleTag : listaTags) {
                if (testoIn.contains(singleTag)) {
                    neContieneAlmenoUno = true;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return neContieneAlmenoUno;
    }// end of method


    /**
     * Controlla che il testo non contenga nessun elemento di una lista di tag
     *
     * @param testoIn   ingresso
     * @param listaTags lista di tag da controllare
     *
     * @return vero se ne contiene nessuno
     */
    public boolean nonContiene(final String testoIn, ArrayList<String> listaTags) {
        return !isContiene(testoIn, listaTags);
    }// end of method


    /**
     * Sostituisce nel testo tutte le occorrenze di oldTag con newTag.
     * Esegue solo se il testo è valido
     * Esegue solo se il oldTag è valido
     * newTag può essere vuoto (per cancellare le occorrenze di oldTag)
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso da elaborare
     * @param oldTag  da sostituire
     * @param newTag  da inserire
     *
     * @return testo modificato
     */
    public String sostituisce(final String testoIn, String oldTag, String newTag) {
        String testoOut = "";
        String prima = "";
        String rimane = testoIn;
        int pos = 0;
        String charVuoto = " ";

        if (this.isValid(testoIn) && this.isValid(oldTag)) {
            if (rimane.contains(oldTag)) {
                pos = rimane.indexOf(oldTag);

                while (pos != -1) {
                    pos = rimane.indexOf(oldTag);
                    if (pos != -1) {
                        prima += rimane.substring(0, pos);
                        prima += newTag;
                        pos += oldTag.length();
                        rimane = rimane.substring(pos);
                        if (prima.endsWith(charVuoto) && rimane.startsWith(charVuoto)) {
                            rimane = rimane.substring(1);
                        }// end of if cycle
                    }// fine del blocco if
                }// fine di while

                testoOut = prima + rimane;
            }// fine del blocco if
        }// fine del blocco if

        return testoOut.trim();
    }// end of  method


    /**
     * Inserisce nel testo alla posizione indicata
     * Esegue solo se il testo è valido
     * Esegue solo se il newTag è valido
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso da elaborare
     * @param newTag  da inserire
     * @param pos     di inserimento
     *
     * @return testo modificato
     */
    public String inserisce(String testoIn, String newTag, int pos) {
        String testoOut = testoIn;
        String prima = "";
        String dopo = "";

        if (this.isValid(testoIn) && this.isValid(newTag)) {
            prima = testoIn.substring(0, pos);
            dopo = testoIn.substring(pos);

            testoOut = prima + newTag + dopo;
        }// fine del blocco if

        return testoOut.trim();
    }// end of  method


    public boolean isNumber(String value) {
        boolean status = true;
        char[] caratteri = value.toCharArray();

        for (char car : caratteri) {
            if (isNotNumber(car)) {
                status = false;
            }// end of if cycle
        }// end of for cycle

        return status;
    }// end of method


    private boolean isNotNumber(char ch) {
        return !isNumber(ch);
    }// end of method


    public String getModifiche(Object oldValue, Object newValue) {
        return getModifiche(oldValue, newValue, EAPrefType.string);
    } // fine del metodo


    public String getModifiche(Object oldValue, Object newValue, EAPrefType type) {
        String tatNew = "Aggiunto: ";
        String tatEdit = "Modificato: ";
        String tatDel = "Cancellato: ";
        String tagSep = " -> ";

        if (oldValue == null && newValue == null) {
            return "";
        }// end of if cycle

        if (oldValue instanceof String && newValue instanceof String) {
            if (this.isEmpty((String) oldValue) && isEmpty((String) newValue)) {
                return "";
            }// end of if cycle

            if (isValid((String) oldValue) && isEmpty((String) newValue)) {
                return tatDel + oldValue.toString();
            }// end of if cycle

            if (isEmpty((String) oldValue) && isValid((String) newValue)) {
                return tatNew + newValue.toString();
            }// end of if cycle

            if (!oldValue.equals(newValue)) {
                return tatEdit + oldValue.toString() + tagSep + newValue.toString();
            }// end of if cycle
        } else {
            if (oldValue != null && newValue != null && oldValue.getClass() == newValue.getClass()) {
                if (!oldValue.equals(newValue)) {
                    if (oldValue instanceof byte[]) {
                        try { // prova ad eseguire il codice
                            return tatEdit + type.bytesToObject((byte[]) oldValue) + tagSep + type.bytesToObject((byte[]) newValue);
                        } catch (Exception unErrore) { // intercetta l'errore
                            log.error(unErrore.toString() + " LibText.getDescrizione() - Sembra che il PrefType non sia del tipo corretto");
                            return "";
                        }// fine del blocco try-catch
                    } else {
                        return tatEdit + oldValue.toString() + tagSep + newValue.toString();
                    }// end of if/else cycle
                }// end of if cycle
            } else {
                if (oldValue != null && newValue == null) {
                    if (oldValue instanceof byte[]) {
                        return "";
                    } else {
                        return tatDel + oldValue.toString();
                    }// end of if/else cycle
                }// end of if cycle

                if (newValue != null && oldValue == null) {
                    if (newValue instanceof byte[]) {
                        return "";
                    } else {
                        return tatNew + newValue.toString();
                    }// end of if/else cycle
                }// end of if cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return "";
    }// end of method


    public String estrae(String valueIn, String tagIni) {
        return estrae(valueIn, tagIni, tagIni);
    }// end of method


    public String estrae(String valueIn, String tagIni, String tagEnd) {
        String valueOut = valueIn;
        int length = 0;
        int posIni = 0;
        int posEnd = 0;

        if (isValid(valueIn) && valueIn.contains(tagIni) && valueIn.contains(tagEnd)) {
            length = tagIni.length();
            posIni = valueIn.indexOf(tagIni);
            posEnd = valueIn.indexOf(tagEnd, posIni + length);
            valueOut = valueIn.substring(posIni + length, posEnd);
        }// end of if cycle

        return valueOut.trim();
    }// end of method


    /**
     * Elimina tutti i caratteri contenuti nella stringa.
     * Esegue solo se il testo è valido
     *
     * @param testoIn    in ingresso
     * @param subStringa da eliminare
     *
     * @return testoOut stringa convertita
     */
    public String levaTesto(String testoIn, String subStringa) {
        String testoOut = testoIn;

        if (testoIn != null && subStringa != null) {
            testoOut = testoIn.trim();
            if (testoOut.contains(subStringa)) {
                testoOut = sostituisce(testoOut, subStringa, VUOTA);
            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    }// end of method


    /**
     * Elimina tutte le virgole contenute nella stringa.
     * Esegue solo se la stringa è valida
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa convertita
     */
    public String levaVirgole(String entrata) {
        return levaTesto(entrata, VIRGOLA);
    }// end of method


    /**
     * Elimina tutti i punti contenuti nella stringa.
     * Esegue solo se la stringa è valida
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa convertita
     */
    public String levaPunti(String entrata) {
        return levaTesto(entrata, PUNTO);
    }// end of method


    /**
     * Formattazione di un numero.
     * <p>
     * Il numero può arrivare come stringa, intero o double <br>
     * Se la stringa contiene punti e virgole, viene pulita <br>
     * Se la stringa non è convertibile in numero, viene restituita uguale <br>
     * Inserisce il punto separatore ogni 3 cifre <br>
     * Se arriva un oggetto non previsto, restituisce null <br>
     *
     * @param numObj da formattare (stringa, intero, long o double)
     *
     * @return stringa formattata
     */
    public String format(Object numObj) {
        String formattato = VUOTA;
        String numText = VUOTA;
        String sep = PUNTO;
        int numTmp = 0;
        int len;
        String num3;
        String num6;
        String num9;
        String num12;

        if (numObj instanceof String || numObj instanceof Integer || numObj instanceof Long || numObj instanceof Double || numObj instanceof List || numObj instanceof Object[]) {
            if (numObj instanceof String) {
                numText = (String) numObj;
                numText = levaVirgole(numText);
                numText = levaPunti(numText);
                try { // prova ad eseguire il codice
                    numTmp = Integer.decode(numText);
                } catch (Exception unErrore) { // intercetta l'errore
                    return (String) numObj;
                }// fine del blocco try-catch
            } else {
                if (numObj instanceof Integer) {
                    numText = Integer.toString((int) numObj);
                }// fine del blocco if
                if (numObj instanceof Long) {
                    numText = Long.toString((long) numObj);
                }// fine del blocco if
                if (numObj instanceof Double) {
                    numText = Double.toString((double) numObj);
                }// fine del blocco if
                if (numObj instanceof List) {
                    numText = Integer.toString((int) ((List) numObj).size());
                }// fine del blocco if
                if (numObj instanceof Object[]) {
                    numText = Integer.toString(((Object[]) numObj).length);
                }// fine del blocco if
            }// fine del blocco if-else
        } else {
            return null;
        }// fine del blocco if-else

        formattato = numText;
        len = numText.length();
        if (len > 3) {
            num3 = numText.substring(0, len - 3);
            num3 += sep;
            num3 += numText.substring(len - 3);
            formattato = num3;
            if (len > 6) {
                num6 = num3.substring(0, len - 6);
                num6 += sep;
                num6 += num3.substring(len - 6);
                formattato = num6;
                if (len > 9) {
                    num9 = num6.substring(0, len - 9);
                    num9 += sep;
                    num9 += num6.substring(len - 9);
                    formattato = num9;
                    if (len > 12) {
                        num12 = num9.substring(0, len - 12);
                        num12 += sep;
                        num12 += num9.substring(len - 12);
                        formattato = num12;
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return formattato;
    }// end of method


    /**
     * Formattazione di un intero con un decimale.
     * <p>
     * Il numero è stato moltiplicato per 10 e memorizzato come intero <br>
     * Va diviso per 10 ed inserita la virgola <br>
     *
     * @param value da formattare (intero * 10)
     *
     * @return stringa formattata
     */
    public String formatOneDecimal(int value) {
        String sep = VIRGOLA;
        String formattato = VUOTA;
        String valueTxt = VUOTA + value;
        int pos = valueTxt.length() - 1;

        formattato += valueTxt.substring(0, pos);
        formattato += sep;
        formattato += valueTxt.substring(pos);

        // valore di ritorno
        return formattato;
    }// end of method


    /**
     * Formattazione di un numero giustificato a due cifre.
     * <p>
     * Il numero può arrivare come stringa, intero o double
     * Se la stringa contiene punti e virgole, viene pulita
     * Se la stringa non è convertibile in numero, viene restituita uguale
     * Se arriva un oggetto non previsto, restituisce null
     *
     * @param numObj da formattare (stringa, intero, long o double)
     *
     * @return stringa formattata
     */
    public String format2(Object numObj) {
        String numText = VUOTA;
        String sep = PUNTO;
        int num = 0;
        int len;
        String num3;
        String num6;
        String num9;
        String num12;

        if (numObj instanceof String || numObj instanceof Integer || numObj instanceof Long || numObj instanceof Double) {
            if (numObj instanceof String) {
                numText = (String) numObj;
                numText = levaVirgole(numText);
                numText = levaPunti(numText);
                try { // prova ad eseguire il codice
                    num = Integer.decode(numText);
                } catch (Exception unErrore) { // intercetta l'errore
                    return (String) numObj;
                }// fine del blocco try-catch
            } else {
                if (numObj instanceof Integer) {
                    num = (int) numObj;
                }// fine del blocco if
                if (numObj instanceof Long) {
                    num = ((Long) numObj).intValue();
                }// fine del blocco if
                if (numObj instanceof Double) {
                    num = ((Double) numObj).intValue();
                }// fine del blocco if
            }// fine del blocco if-else
        } else {
            return null;
        }// fine del blocco if-else

        numText = "" + num;
        if (num < 10) {
            return numText = "0" + numText;
        }// end of if cycle

        // valore di ritorno
        return numText;
    }// end of method


    /**
     * Formattazione di un numero giustificato a tre cifre.
     * <p>
     * Il numero può arrivare come stringa, intero o double
     * Se la stringa contiene punti e virgole, viene pulita
     * Se la stringa non è convertibile in numero, viene restituita uguale
     * Se arriva un oggetto non previsto, restituisce null
     *
     * @param numObj da formattare (stringa, intero, long o double)
     *
     * @return stringa formattata
     */
    public String format3(Object numObj) {
        String numText = VUOTA;
        String sep = PUNTO;
        int num = 0;
        int len;
        String num3;
        String num6;
        String num9;
        String num12;

        if (numObj instanceof String || numObj instanceof Integer || numObj instanceof Long || numObj instanceof Double) {
            if (numObj instanceof String) {
                numText = (String) numObj;
                numText = levaVirgole(numText);
                numText = levaPunti(numText);
                try { // prova ad eseguire il codice
                    num = Integer.decode(numText);
                } catch (Exception unErrore) { // intercetta l'errore
                    return (String) numObj;
                }// fine del blocco try-catch
            } else {
                if (numObj instanceof Integer) {
                    num = (int) numObj;
                }// fine del blocco if
                if (numObj instanceof Long) {
                    num = ((Long) numObj).intValue();
                }// fine del blocco if
                if (numObj instanceof Double) {
                    num = ((Double) numObj).intValue();
                }// fine del blocco if
            }// fine del blocco if-else
        } else {
            return null;
        }// fine del blocco if-else

        numText = "" + num;
        if (num < 100) {
            if (num < 10) {
                return numText = "00" + numText;
            } else {
                return numText = "0" + numText;
            }// end of if/else cycle
        }// end of if cycle

        // valore di ritorno
        return numText;
    }// end of method


    /**
     * Restituisce la posizione di un tag in un testo
     * Riceve una lista di tag da provare
     * Restituisce la prima posizione tra tutti i tag trovati
     *
     * @param testo in ingresso
     * @param lista di stringhe, oppure singola stringa
     *
     * @return posizione della prima stringa trovata
     * -1 se non ne ha trovato nessuna
     * -1 se il primo parametro è nullo o vuoto
     * -1 se il secondo parametro è nullo
     * -1 se il secondo parametro non è ne una lista di stringhe, ne una stringa
     */
    public int getPos(String testo, ArrayList<String> lista) {
        int pos = testo.length();
        int posTmp;
        ArrayList<Integer> posizioni = new ArrayList<Integer>();

        if (!testo.equals("") && lista != null) {

            for (String stringa : lista) {
                posTmp = testo.indexOf(stringa);
                if (posTmp != -1) {
                    posizioni.add(posTmp);
                }// fine del blocco if
            } // fine del ciclo for-each

            if (posizioni.size() > 0) {
                for (Integer num : posizioni) {
                    pos = Math.min(pos, num);
                } // fine del ciclo for-each
            } else {
                pos = 0;
            }// fine del blocco if-else
        }// fine del blocco if

        return pos;
    }// end of method


    /**
     * Elimina la parte di stringa successiva al tag, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata   stringa in ingresso
     * @param tagFinale dopo il quale eliminare
     *
     * @return uscita stringa ridotta
     */
    public String levaDopo(String entrata, String tagFinale) {
        String uscita = entrata;
        int pos;

        if (uscita != null && tagFinale != null) {
            uscita = entrata.trim();
            if (uscita.contains(tagFinale)) {
                pos = uscita.indexOf(tagFinale);
                uscita = uscita.substring(0, pos);
                uscita = uscita.trim();
            }// fine del blocco if
        }// fine del blocco if

        return uscita;
    }// end of method


    /**
     * Elimina la parte di stringa successiva al tag <ref>, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoRef(String entrata) {
        return levaDopo(entrata, REF);
    }// end of method


    /**
     * Elimina la parte di stringa successiva al tag <!--, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoNote(String entrata) {
        return levaDopo(entrata, NOTE);
    }// end of method


    /**
     * Elimina la parte di stringa successiva al tag {{, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoGraffe(String entrata) {
        return levaDopo(entrata, GRAFFE);
    }// end of method


    /**
     * Elimina la parte di stringa successiva al tag -virgola-, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoVirgola(String entrata) {
        return levaDopo(entrata, VIRGOLA);
    }// end of method


    /**
     * Elimina la parte di stringa successiva al tag -aperta parentesi-, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoParentesi(String entrata) {
        return levaDopo(entrata, PARENTESI);
    }// end of method


    /**
     * Elimina la parte di stringa successiva al tag -punto interrogativo-, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoInterrogativo(String entrata) {
        return levaDopo(entrata, INTERROGATIVO);
    }// end of method


    /**
     * Confronta due numeri.
     *
     * @param primo   numero
     * @param secondo numero
     *
     * @return :
     * 1 if secondo should be before primo
     * -1 if primo should be before secondo
     * 0 otherwise
     */
    public int compareInt(int primo, int secondo) {
        return (primo < secondo) ? -1 : (primo == secondo) ? 0 : 1;
    }// end of method


    /**
     * Confronta due stringhe.
     *
     * @param prima   stringa
     * @param seconda stringa
     *
     * @return :
     * 1 if seconda should be before prima
     * -1 if prima should be before seconda
     * 0 otherwise
     */
    public int compareStr(String prima, String seconda) {
        int test;

        if (isValid(prima) && isValid(seconda)) {
            test = prima.compareTo(seconda);
            return (prima.compareTo(seconda) < 0) ? -1 : (prima.compareTo(seconda) == 0) ? 0 : 1;
        } else {
            log.warn("Algos - ATextService.compareStr(): valori nulli");
            return 0;
        }// end of if/else cycle
    }// end of method


    /**
     * Label colorata
     */
    private Label getLabel(String message, String labelColor) {
        Label label = null;

        if (isValid(message)) {
            label = new Label(message);
            label.getElement().getStyle().set("color", labelColor);
        }// end of if cycle

        return label;
    }// end of method


    /**
     * Label colorata
     */
    public Label getLabelDev(String message) {
        return getLabel(message, "red");
    }// end of method


    /**
     * Label colorata
     */
    public Label getLabelUser(String message) {
        return getLabel(message, "green");
    }// end of method


    /**
     * Label colorata
     */
    public Label getLabelAdmin(String message) {
        return getLabel(message, "blue");
    }// end of method

}// end of class
