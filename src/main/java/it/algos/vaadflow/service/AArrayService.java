package it.algos.vaadflow.service;

import com.vaadin.flow.component.grid.Grid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 13:46
 * <p>
 * Utility per la gestione degli array <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AArrayService.class); <br>
 * 2) AArrayService.getInstance(); <br>
 * 3) @Autowired private AArrayService arrayService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Slf4j
public class AArrayService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private final property
     */
    private static final AArrayService INSTANCE = new AArrayService();


//    /**
//     * Private constructor to avoid client applications to use constructor
//     */
//    private AArrayService() {
//    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AArrayService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Ordina la lista
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param listaDisordinata in ingresso
     *
     * @return lista ordinata, null se listaDisordinata è null
     */
    public ArrayList sort(List listaDisordinata) {
        ArrayList<Object> objList;
        Object[] objArray = listaDisordinata.toArray();

        try { // prova ad eseguire il codice
            Arrays.sort(objArray);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        objList = fromObj(objArray);

        return objList;
    }// end of method


    /**
     * Ordina la mappa secondo la chiave
     *
     * @param mappaDisordinata in ingresso
     *
     * @return mappa ordinata, null se mappaDisordinata è null
     */
    public LinkedHashMap sort(HashMap mappaDisordinata) {
        LinkedHashMap mappaOrdinata = new LinkedHashMap();
        Object[] listaChiavi = mappaDisordinata.keySet().toArray();

        try { // prova ad eseguire il codice
            Arrays.sort(listaChiavi);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        for (Object chiave : listaChiavi) {
            mappaOrdinata.put(chiave, mappaDisordinata.get(chiave));
        }// end of for cycle

        return mappaOrdinata;
    }// end of method


    /**
     * Controlla la validità dell'array
     * Deve esistere (not null)
     * Deve avere degli elementi (size > 0)
     * Il primo elemento deve essere valido
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isValid(final List array) {
        boolean status = false;

        if (array != null && array.size() > 0) {
            if (array.get(0) != null) {
                if (array.get(0) instanceof String) {
                    status = text.isValid(array.get(0));
                } else {
                    status = true;
                }// end of if/else cycle
            }// end of if cycle
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Controlla la validità dell'array
     * Deve esistere (not null)
     * Deve avere degli elementi (length > 0)
     * Il primo elemento deve essere una stringa valida
     *
     * @param array (String[]) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isValid(final String[] array) {
        boolean status = false;

        if (array != null && array.length > 0) {
            if (array[0] != null) {
                if (array[0] instanceof String) {
                    status = text.isValid((String) array[0]);
                } else {
                    status = true;
                }// end of if/else cycle
            }// end of if cycle
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Controlla la validità della mappa
     * Deve esistere (not null)
     * Deve avere degli elementi (size > 0)
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isValid(final Map array) {
        return array != null && array.size() > 0;
    }// end of method


    /**
     * Controlla che l'array sia nullo o vuoto
     * Non deve esistere (null)
     * Se esiste, non deve avere elementi (size = 0)
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isEmpty(final List array) {
        return !isValid(array);
    }// end of method


    /**
     * Controlla che l'array sia nullo o vuoto
     * Non deve esistere (null)
     * Se esiste, non deve avere elementi (size = 0)
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isEmpty(final String[] array) {
        return !isValid(array);
    }// end of method


    /**
     * Aggiunge un elemento ad una List (di per se immutabile)
     * Deve esistere (not null)
     *
     * @param arrayIn (List) ingresso da incrementare
     *
     * @return la lista aumentata di un elemento
     */
    public List add(final ArrayList arrayIn, Object obj) {
        List arrayOut = null;
        ArrayList lista = null;

        if (this.isValid(arrayIn)) {
            lista = new ArrayList(arrayIn);
            lista.add(obj);
            arrayOut = lista;
        }// end of if cycle

        return arrayOut;
    }// end of method


    /**
     * Somma due array (liste) e restituisce una lista ordinata
     * <p>
     * Almeno uno dei due array in ingresso deve essere non nullo
     * Normalmente si usa di più la somma ordinata
     * I valori negli array sono unici
     * <p>
     * Se entrambi i parametri sono nulli, restituisce un nullo
     * Se uno dei parametri è nullo, restituisce l'altro
     * La lista di valori in uscita è unica (quindi la dimensione può essere minore dalla somma delle due)
     *
     * @param arrayPrimo   - prima lista
     * @param arraySecondo - seconda lista
     *
     * @return arraySomma ordinata
     */
    public ArrayList somma(ArrayList arrayPrimo, ArrayList arraySecondo) {
        ArrayList arraySomma = sommaDisordinata(arrayPrimo, arraySecondo);

        if (arraySomma != null) {
            arraySomma = sort(arraySomma);
        }// fine del blocco if

        return arraySomma;
    }// end of method


    /**
     * Somma due array (liste) e restituisce una lista NON ordinata
     * <p>
     * Almeno uno dei due array in ingresso deve essere non nullo
     * Normalmente si usa di meno la somma disordinata
     * I valori negli array sono unici
     * <p>
     * Se entrambi i parametri sono nulli, restituisce un nullo
     * Se uno dei parametri è nullo, restituisce l'altro
     * La lista di valori in uscita è unica (quindi la dimensione può essere minore dalla somma delle due)
     *
     * @param arrayPrimo   - prima lista
     * @param arraySecondo - seconda lista
     *
     * @return arraySomma disordinata
     */
    public ArrayList sommaDisordinata(ArrayList arrayPrimo, ArrayList arraySecondo) {
        ArrayList arraySomma = null;

        if (arrayPrimo != null && arraySecondo != null) {
            arraySomma = new ArrayList();
            for (Object ogg : arrayPrimo) {
                arraySomma.add(ogg);
            } // fine del ciclo for-each
            for (Object ogg : arraySecondo) {
                arraySomma.add(ogg);
            } // fine del ciclo for-each
            arraySomma = valoriUniciBase(arraySomma, false);
        }// fine del blocco if

        if (arrayPrimo == null) {
            arraySecondo = valoriUniciBase(arraySecondo, false);
            return arraySecondo;
        }// fine del blocco if

        if (arraySecondo == null) {
            arrayPrimo = valoriUniciBase(arrayPrimo, false);
            return arrayPrimo;
        }// fine del blocco if

        return arraySomma;
    }// end of method


    /**
     * Estrae i valori unici da un lista con (eventuali) valori doppi
     * Eventualmente (tag booleano) ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     *
     * @param listaValoriDoppi in ingresso
     * @param ordina           tag per forzare l'ordinamento
     *
     * @return valoriUnici disordinati oppure ordinati, null se listaValoriDoppi è null
     */
    @SuppressWarnings("all")
    private ArrayList valoriUniciBase(ArrayList listaValoriDoppi, boolean ordina) {
        ArrayList listaValoriUniciNonOrdinati = null;
        Set set;

        if (listaValoriDoppi != null) {
            set = new LinkedHashSet((List) listaValoriDoppi);
            listaValoriUniciNonOrdinati = new ArrayList(set);
            if (ordina) {
                return sort(listaValoriUniciNonOrdinati);
            } else {
                return listaValoriUniciNonOrdinati;
            }// fine del blocco if-else
        }// fine del blocco if

        return null;
    }// end of method


    /**
     * Convert a objArray to ArrayList
     *
     * @param objArray to convert
     *
     * @return the corresponding casted ArrayList
     */
    public ArrayList<Object> fromObj(Object[] objArray) {
        ArrayList<Object> objList = new ArrayList<Object>();

        for (Object lungo : objArray) {
            objList.add(lungo);
        } // fine del ciclo for-each

        return objList;
    }// end of method


    /**
     * Convert a stringArray to List
     *
     * @param strArray to convert
     *
     * @return the corresponding casted List
     */
    public List<String> fromStr(String[] strArray) {
        List<String> strList = new ArrayList<String>();

        Collections.addAll(strList, strArray);

        return strList;
    }// end of method


    /**
     * Costruisce una stringa con i singoli valori divisi da un pipe
     * <p>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori divisi da un separatore
     */
    public String toStringaPipe(ArrayList array) {
        return toStringa(array, "|");
    }// end of method


    /**
     * Costruisce una stringa con i singoli valori divisi da una virgola seguita da uno spazio
     * <p>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori divisi da un separatore
     */
    public String toStringaSpazio(List array) {
        return toStringa(array, VIRGOLA + SPAZIO);
    }// end of method


    /**
     * Costruisce una stringa con i singoli valori divisi da un separatore virgola
     * <p>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori divisi da un separatore
     */
    public String toStringa(List array) {
        return toStringa(array, VIRGOLA);
    }// end of method


    /**
     * Costruisce una stringa con i singoli valori divisi da un separatore
     * <p>
     *
     * @param array lista di valori
     * @param sep   carattere separatore
     *
     * @return stringa con i singoli valori divisi da un separatore
     */
    public String toStringa(List array, String sep) {
        String testo = VUOTA;
        StringBuilder textBuffer = null;

        if (array != null) {
            textBuffer = new StringBuilder();
            for (Object obj : array) {
                textBuffer.append(obj.toString());
                textBuffer.append(sep);
            } // fine del ciclo for-each
            testo = textBuffer.toString();
            testo = text.levaCoda(testo, sep);
        }// end of if cycle

        return testo;
    }// end of method


    public ArrayList<Long> fromLong(long[] longArray) {
        ArrayList<Long> longList = new ArrayList();
        long[] var2 = longArray;
        int var3 = longArray.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Long lungo = var2[var4];
            longList.add(lungo);
        } // fine del ciclo for-each

        return longList;
    }// end of method


    public List<String> getList(String testo) {
        List<String> lista = null;
        String tag = ",";
        String[] parti = null;

        if (text.isValid(testo) && testo.contains(tag)) {
            parti = testo.split(tag);
        }// end of if cycle

        if (parti != null && parti.length > 0) {
            lista = Arrays.asList(parti);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Numero di cicli
     *
     * @param totale da dividere
     * @param blocco divisore
     *
     * @return numero di cicli
     */
    public int numCicli(int totale, int blocco) {
        int cicli = 0;
        int resto;

        if (blocco < 0) {
            return 0;
        }// end of if cycle

        if (blocco == 0) {
            return totale;
        }// end of if cycle

        if (blocco > totale) {
            return 1;
        }// end of if cycle

        cicli = totale / blocco;
        resto = totale % blocco;
        if (resto > 0) {
            cicli++;
        }// end of if cycle

        return cicli;
    }// end of method


    /**
     * Estra un subset dalla lista
     *
     * @param listaTotale   da suddividere
     * @param dimBlocco     di suddivisione
     * @param cicloCorrente attuale - inizia da 1 (non da zero)
     *
     * @return sublista corrente del ciclo
     */
    public ArrayList estraeSublista(List listaTotale, int dimBlocco, int cicloCorrente) {
        int posIni = 0;
        int posEnd = 0;

        if (listaTotale == null || listaTotale.size() < 1 || cicloCorrente == 0) {

            if (cicloCorrente == 0) {
                log.warn("Algos - AArrayService.estraeSubLista(), cicloCorrente=0");
            } else {
                log.warn("Algos - AArrayService.estraeSubLista(), listaTotale non valida");
            }// end of if/else cycle

            return null;
        }// end of if cycle

        posIni = dimBlocco * (cicloCorrente - 1);
        posEnd = posIni + dimBlocco;
        posEnd = Math.min(posEnd, listaTotale.size());

        return new ArrayList(listaTotale.subList(posIni, posEnd));
    }// end of method


    /**
     * Estra un subset dalla lista
     *
     * @param listaTotale   da suddividere
     * @param dimBlocco     di suddivisione
     * @param cicloCorrente attuale
     *
     * @return sublista corrente del ciclo
     */
    public ArrayList<Long> estraeSublistaLong(ArrayList<Long> listaTotale, int dimBlocco, int cicloCorrente) {
        return estraeSublista(listaTotale, dimBlocco, cicloCorrente);
    }// end of method


    /**
     * Posizione di un valore nella lista
     *
     * @param lista da esaminare
     * @param value di trovare
     *
     * @return posizione
     */
    public int getPos(List lista, Object value) {
        int pos = 0;

        if (isValid(lista)) {
            for (Object obj : lista) {
                pos++;
                if (obj.equals(value)) {
                    return pos;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return pos;
    }// end of method


    /**
     * Costruisce una matrice di colonne della grid
     *
     * @param grid da esaminare
     *
     * @return matrice
     */
    public Grid.Column[] getColumnArray(Grid grid) {
        List<Grid.Column> lista = grid.getColumns();
        Grid.Column[] matrice = new Grid.Column[lista.size()];

        for (int k = 0; k < lista.size(); k++) {
            matrice[k] = lista.get(k);
        }// end of for cycle

        return matrice;
    }// end of method


    /**
     * Differenza tra due array
     *
     * @param primo   array
     * @param secondo array
     *
     * @return differenza
     */
    public ArrayList differenza(List primo, List secondo) {
        ArrayList differenza = null;
        int pos = 0;
        int sec = 0;
        long inizio = System.currentTimeMillis();

        if (primo != null && secondo != null) {
            differenza = new ArrayList();

            for (Object value : primo) {
                if (!secondo.contains(value)) {
                    differenza.add(value);
                }// fine del blocco if
                pos++;
                sec++;
                if (sec == 10000) {
                    log.info("Array. Differenza di " + text.format(pos) + " elementi in " + date.deltaText(inizio));
                    sec = 0;
                }// end of if cycle
            } // fine del ciclo for-each


//            for (int k = 0; k < primo.size(); k++) {
//                for (int j = k; j < secondo.size(); j++) {
//                    if (test) {
//                        differenza.add(value);
//                    }// end of if cycle
//                }// end of for cycle
//            }// end of for cycle

//            int max = secondo.size()-1;
//            for (int k = 0; k < primo.size(); k++) {
//                if (!secondo.subList(k, max).contains(primo.get(k))) {
//                    differenza.add(k);
//                }// fine del blocco if
//            } // fine del ciclo for-each

        }// fine del blocco if

        return differenza;
    } // fine del metodo


    /**
     * Differenza tra due array di Long
     *
     * @param listaUno array
     * @param listaDue array
     *
     * @return differenza
     */
    public List<Long> delta(List<Long> listaUno, List<Long> listaDue) {
        List<Long> listaUnoCopia = new ArrayList<>(listaUno);
        HashSet<Long> hashA = new HashSet<Long>();
        HashSet<Long> hashB = new HashSet<Long>();

        for (Long uno : listaUno) {
            hashA.add(uno);
        }// end of for cycle
        for (Long due : listaDue) {
            hashB.add(due);
        }// end of for cycle

        hashA.removeAll(hashB);
        return new ArrayList<Long>(hashA);
    } // fine del metodo


//    /**
//     * Differenza tra due array
//     *
//     * @param primo   array
//     * @param secondo array
//     *
//     * @return differenza
//     */
//    public ArrayList delta(List<String> listaUno, List<String> listaDue) {
//        HashSet<String> hashA = new HashSet<String>();
//        HashSet<String> hashB = new HashSet<String>();
//        int pos = 0;
//        int sec = 0;
//        long inizio = System.currentTimeMillis();
//
//        for (String stringaA : listaUno) {
//            hashA.add(stringaA);
//        }// end of for cycle
//        for (String stringaB : listaDue) {
//            hashB.add(stringaB);
//        }// end of for cycle
//
//        for (String stringa : hashA) {
//            if (hashB.contains(stringa)) {
//                hashB.remove(stringa);
//            }// end of if cycle
//            pos++;
//            sec++;
//            if (sec == 10000) {
//                log.info("Array. Differenza di " + text.format(pos) + " elementi in " + date.deltaText(inizio));
//                sec = 0;
//            }// end of if cycle
//        }// end of for cycle
//
//        return new ArrayList<String>(hashB);
//    } // fine del metodo


    /**
     * Converte una lista di oggetti in una lista di stringhe
     */
    public List<String> toString(List listaOggetti) {
        List<String> listaStringhe = null;

        if (isValid(listaOggetti)) {
            listaStringhe = new ArrayList<>();
            for (Object obj : listaOggetti) {
                listaStringhe.add(obj.toString());
            }// end of for cycle
        }// end of if cycle

        return listaStringhe;
    }// end of method


    /**
     * Converte una lista di oggetti in una lista di stringhe
     */
    public List<String> toString(Object[] listaOggetti) {
        List<String> listaStringhe = null;

        if (listaOggetti != null && listaOggetti.length > 0) {
            listaStringhe = new ArrayList<>();
            for (Object obj : listaOggetti) {
                listaStringhe.add(obj.toString());
            }// end of for cycle
        }// end of if cycle

        return listaStringhe;
    }// end of method


    /**
     * Controlla se la mappa può essere semplicficata
     * La mappa prevede delle liste di valori per ogni key, quindi Map<String, List<String>>
     * Se tutte le lisye hanno un singolo valore, si può usare una mappa più semplice Map<String, String>
     */
    public boolean isMappaSemplificabile(Map<String, List<String>> mappaConListe) {
        boolean status = true;

        for (Map.Entry<String, List<String>> entry : mappaConListe.entrySet()) {
            if (entry.getValue().size() > 1) {
                status = false;
            }// end of if cycle
        }// end of for cycle

        return status;
    }// end of method


    /**
     * Semplifica la mappa
     * Questa prevede delle liste di valori per ogni key, quindi Map<String, List<String>>
     * Spesso basta un valore.
     * Se tutte le keys hanno un solo valore, si usa una mappa più semplice Map<String, String>
     */
    public Map<String, String> semplificaMappa(Map<String, List<String>> mappaConListe) {
        Map<String, String> mappaSemplice = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : mappaConListe.entrySet()) {
            if (entry.getValue().size() == 1) {
                mappaSemplice.put(entry.getKey(), entry.getValue().get(0));
            } else {
                log.error("Qualcosa non ha funzionato");
                System.out.println("Qualcosa non ha funzionato");
            }// end of if/else cycle
        }// end of for cycle

        return mappaSemplice;
    }// end of method

}// end of singleton class
