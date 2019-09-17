package it.algos.vaadflow.service;

import it.algos.vaadflow.enumeration.EATime;
import it.algos.vaadflow.modules.mese.EAMese;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: lun, 05-feb-2018
 * Time: 14:58
 * <p>
 * Gestione e formattazione delle date e dei tempi
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ADateService.class); <br>
 * 2) ADateService.getInstance(); <br>
 * 3) @Autowired private ADateService dateService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * <p>
 */
@Service
@Slf4j
public class ADateService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private final property
     */
    private static final ADateService INSTANCE = new ADateService();


    private static final String INFERIORE_SECONDO = "meno di un sec.";

    private static final String SECONDI = " sec.";

    private static final String MINUTI = " min.";

    private static final String ORA = " ora";

    private static final String ORE = " ore";

    private static final String GIORNO = " giorno";

    private static final String GIORNI = " gg.";

    private static final String ANNO = " anno";

    private static final String ANNI = " anni";

    private static final long MAX_MILLISEC = 1000;

    private static final long MAX_SECONDI = 60 * MAX_MILLISEC;

    private static final long MAX_MINUTI = 60 * MAX_SECONDI;

    private static final long MAX_ORE = 24 * MAX_MINUTI;

    private static final long MAX_GIORNI = 365 * MAX_ORE;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private ADateService() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static ADateService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Convert java.util.Date to java.time.LocalDate
     * Date HA ore, minuti e secondi
     * LocalDate NON ha ore, minuti e secondi
     * Si perdono quindi le ore i minuti ed i secondi di Date
     *
     * @param data da convertire
     *
     * @return data locale (deprecated)
     */
    @Deprecated
    public LocalDate dateToLocalDate(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }// end of method


    /**
     * Convert java.time.LocalDate to java.util.Date
     * LocalDate NON ha ore, minuti e secondi
     * Date HA ore, minuti e secondi
     * La Date ottenuta ha il tempo regolato a mezzanotte
     *
     * @param localDate da convertire
     *
     * @return data (deprecated)
     */
    @Deprecated
    public Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }// end of method


    /**
     * Convert java.util.Date to java.time.LocalDateTime
     * Date HA ore, minuti e secondi
     * LocalDateTime HA ore, minuti e secondi
     * Non si perde nulla
     *
     * @param data da convertire
     *
     * @return data e ora locale
     */
    public LocalDateTime dateToLocalDateTime(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }// end of method


    /**
     * Convert java.time.LocalDateTime to java.util.Date
     * LocalDateTime HA ore, minuti e secondi
     * Date HA ore, minuti e secondi
     * Non si perde nulla
     *
     * @param localDateTime da convertire
     *
     * @return data (deprecated)
     */
    @Deprecated
    public Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }// end of method


    /**
     * Convert java.time.LocalDate to java.time.LocalDateTime
     * LocalDate NON ha ore, minuti e secondi
     * LocalDateTime HA ore, minuti e secondi
     * La LocalDateTime ottenuta ha il tempo regolato a mezzanotte
     *
     * @param localDate da convertire
     *
     * @return data con ore e minuti alla mezzanotte
     */
    @Deprecated
    public LocalDateTime localDateToLocalDateTime(LocalDate localDate) {
        Date date = localDateToDate(localDate);
        Instant istante = date.toInstant();
        return istante.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }// end of method


    /**
     * Convert java.time.LocalDateTime to java.time.LocalDate
     * LocalDateTime HA ore, minuti e secondi
     * LocalDate NON ha ore, minuti e secondi
     * Si perdono quindi le ore i minuti ed i secondi di Date
     *
     * @param localDateTime da convertire
     *
     * @return data con ore e minuti alla mezzanotte
     */
    public LocalDate localDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }// end of method


    /**
     * Convert java.util.Date to java.time.LocalTime
     * Estrae la sola parte di Time
     * Date HA anni, giorni, ore, minuti e secondi
     * LocalTime NON ha anni e giorni
     * Si perdono quindi gli anni ed i giorni di Date
     *
     * @param data da convertire
     *
     * @return time senza ilgiorno
     */
    public LocalTime dateToLocalTime(Date data) {
        return LocalTime.of(data.getHours(), data.getMinutes());
    }// end of method


    /**
     * Costruisce una data da una stringa in formato ISO 8601
     *
     * @param isoStringa da leggere
     *
     * @return data costruita
     */
    public Date dateFromISO(String isoStringa) {
        Date data = null;
        DateFormat format = new SimpleDateFormat(EATime.iso8601.getPattern());
        try { // prova ad eseguire il codice
            data = format.parse(isoStringa);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return data;
    }// end of method


    /**
     * Costruisce una localData da una stringa in formato ISO 8601
     * ATTENZIONE: si perdono ore, minuti e secondi (se ci sono)
     *
     * @param isoStringa da leggere
     *
     * @return localData costruita
     */
    public LocalDate localDateFromISO(String isoStringa) {
        return dateToLocalDate(dateFromISO(isoStringa));
    }// end of method


    /**
     * Costruisce una localDateTime da una stringa in formato ISO 8601
     *
     * @param isoStringa da leggere
     *
     * @return localDateTime costruita
     */
    public LocalDateTime localDateTimeFromISO(String isoStringa) {
        return dateToLocalDateTime(dateFromISO(isoStringa));
    }// end of method


    /**
     * Restituisce la data attuale nella forma del pattern standard
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @return la data sotto forma di stringa
     */
    public String get() {
        return get(LocalDate.now(), EATime.standard.getPattern());
    }// end of method


    /**
     * Restituisce la data nella forma del pattern standard
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalDate localDate) {
        return get(localDate, EATime.standard.getPattern());
    }// end of method


    /**
     * Restituisce la data nella forma del pattern completo
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getCompleta(LocalDate localDate) {
        return get(localDate, EATime.completa.getPattern());
    }// end of method


    /**
     * Restituisce la data nella forma del pattern ricevuto
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDate   da rappresentare
     * @param patternEnum enumeration di pattern per la formattazione
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalDate localDate, EATime patternEnum) {
        return get(localDate, patternEnum.getPattern());
    }// end of method


    /**
     * Restituisce la data nella forma del pattern ricevuto
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDate da rappresentare
     * @param pattern   per la formattazione
     *
     * @return la data sotto forma di stringa
     */
    public String get(LocalDate localDate, String pattern) {
        return localDate.format(DateTimeFormatter.ofPattern(pattern, LOCALE));
    }// end of method


    /**
     * Restituisce l'anno corrente
     * <p>
     * 2019
     *
     * @return anno corrente
     */
    public int getAnnoCorrente() {
        return LocalDate.now().getYear();
    }// end of method


    /**
     * Restituisce il giorno della settimana in forma estesa
     * <p>
     * domenica 5
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    @Deprecated
    public String getWeekLong(LocalDate localDate) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("EEEE d");
        return format.format(localDateToDate(localDate));
    }// end of method


    /**
     * Restituisce il giorno della settimana in forma estesa
     * <p>
     * domenica 5
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getWeekLong(LocalDateTime localDateTime) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("EEEE d");
        return format.format(localDateTimeToDate(localDateTime));
    }// end of method


    /**
     * Restituisce la data (senza tempo) in forma breve
     * <p>
     * 05-04-14
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getShort(LocalDateTime localDateTime) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd-mm-yy");
        return format.format(localDateTimeToDate(localDateTime));
    }// end of method


    /**
     * Restituisce la data (senza tempo) in forma normale
     * <p>
     * 5-ott-14
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getDate(LocalDateTime localDateTime) {
        return getDate(localDateTimeToLocalDate(localDateTime));
    }// end of method


    /**
     * Restituisce la data (senza tempo) in forma normale
     * <p>
     * 5-ott-14
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getDate(LocalDate localDate) {
        String testo = "";

        testo += localDate.getDayOfMonth();
        testo += "-";
        testo += localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ITALIAN);
        testo += "-";
        testo += localDate.getYear() > 2000 ? localDate.getYear() - 2000 : localDate.getYear();

        return testo;
    }// end of method


    /**
     * Restituisce la data attuale completa di tempo
     * <p>
     * 5-ott-14 7:04
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @return la data sotto forma di stringa
     */
    public String getTime() {
        return getTime(LocalDateTime.now());
    }// end of method


    /**
     * Restituisce la data completa di tempo
     * <p>
     * 5-ott-14 7:04
     * <p>
     * Returns a string representation of the date
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDateTime da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getTime(LocalDateTime localDateTime) {
        return getDate(localDateTime) + SPAZIO + getOrario(localDateTime);
    }// end of method


    /**
     * Restituisce ora e minuti
     * <p>
     * 7:04
     *
     * @param localDateTime da rappresentare
     *
     * @return l'orario sotto forma di stringa
     */
    public String getOrario(LocalDateTime localDateTime) {
        String testo = "";
        int minuti;
        String tag = "0";

        testo += localDateTime.getHour();
        testo += ":";
        minuti = localDateTime.getMinute();
        if (minuti < 10) {
            testo += tag;
        }// end of if cycle
        testo += minuti;

        return testo;
    }// end of method


    /**
     * Restituisce ora e minuti
     * <p>
     * 7:04
     *
     * @param localTime da rappresentare
     *
     * @return l'orario sotto forma di stringa
     */
    public String getOrario(LocalTime localTime) {
        String testo = "";
        int minuti;
        String tag = "0";

        testo += localTime.getHour();
        testo += ":";
        minuti = localTime.getMinute();
        if (minuti < 10) {
            testo += tag;
        }// end of if cycle
        testo += minuti;

        return testo;
    }// end of method


    /**
     * Ritorna il numero della settimana dell'anno di una data fornita.
     * <p>
     * 43
     * <p>
     * Usa Calendar
     *
     * @param data fornita
     *
     * @return il numero della settimana dell'anno
     */
    @Deprecated
    public int getWeekYear(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.WEEK_OF_YEAR);
    }// end of method


    /**
     * Ritorna il numero della settimana del mese di una data fornita.
     * <p>
     * 3
     * <p>
     * Usa Calendar
     *
     * @param data fornita
     *
     * @return il numero della settimana del mese
     */
    @Deprecated
    public int getWeekMonth(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.WEEK_OF_MONTH);
    }// end of method


    /**
     * Ritorna il numero del giorno dell'anno di una data fornita.
     * <p>
     * 294
     * <p>
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero del giorno dell'anno
     */
    @Deprecated
    public int getDayYear(Date data) {
        return dateToLocalDate(data).getDayOfYear();
    }// end of method


    /**
     * Ritorna il numero del giorno del mese di una data fornita.
     * <p>
     * 21
     * <p>
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero del giorno del mese
     */
    @Deprecated
    public int getDayOfMonth(Date data) {
        return dateToLocalDate(data).getDayOfMonth();
    }// end of method


    /**
     * Ritorna il numero del giorno della settimana di una data fornita.
     * <p>
     * 4
     * <p>
     * Usa Calendar
     *
     * @param data fornita
     *
     * @return il numero del giorno della settimana (1=dom, 7=sab)
     */
    @Deprecated
    public int getDayWeek(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.DAY_OF_WEEK);
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana di una data fornita.
     * <p>
     * sab
     *
     * @param localDateTime fornita
     *
     * @return il giorno della settimana in forma breve
     */
    public String getDayWeekShort(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana ed il giorno (numero) del mese di una data fornita.
     * <p>
     * sab 23
     *
     * @param localDate fornita
     *
     * @return il giorno della settimana in forma breve
     */
    public String getWeekShort(LocalDate localDate) {
        return get(localDate, EATime.weekShort);
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana ed il giorno (numero) del mese ed il nome del mese di una data fornita.
     * <p>
     * sab 23 apr
     *
     * @param localDate fornita
     *
     * @return il giorno della settimana in forma breve
     */
    public String getWeekShortMese(LocalDate localDate) {
        return get(localDate, EATime.weekShortMese);
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana di una data fornita.
     * <p>
     * sab
     *
     * @param localDate fornita
     *
     * @return il giorno della settimana in forma breve
     */
    public String getDayWeekShort(LocalDate localDate) {
        return localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana di una data fornita.
     * <p>
     * sab
     * <p>
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il giorno della settimana in forma breve
     */
    @Deprecated
    public String getDayWeekShort(Date data) {
        return getDayWeekShort(dateToLocalDate(data));
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana di una data fornita.
     * <p>
     * martedì
     *
     * @param localDate fornita
     *
     * @return il giorno della settimana in forma estesa
     */
    public String getDayWeekFull(LocalDate localDate) {
        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
    }// end of method


    /**
     * Trasforma la data nel formato standard ISO 8601.
     * <p>
     * 2017-02-16T21:00:00
     * Unsupported field: OffsetSeconds
     * Dovrebbe essere 2017-02-16T21:00:00.000+01:00 per essere completa
     *
     * @param localDateTime fornito
     *
     * @return testo standard ISO senza OffsetSeconds
     */
    public String getISO(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(EATime.iso8601.getPattern(), LOCALE));
    }// end of method


    /**
     * Trasforma la data nel formato standard ISO 8601.
     * <p>
     * 2017-02-16T21:00:00
     * Unsupported field: OffsetSeconds
     * Dovrebbe essere 2017-02-16T21:00:00.000+01:00 per essere completa
     *
     * @param localDate fornita
     *
     * @return testo standard ISO senza OffsetSeconds
     */
    public String getISO(LocalDate localDate) {
        return getISO(localDateToLocalDateTime(localDate));
    }// end of method


    /**
     * Trasforma la data nel formato standard ISO 8601.
     * <p>
     * 2017-02-16T21:00:00
     * Unsupported field: OffsetSeconds
     * Dovrebbe essere 2017-02-16T21:00:00.000+01:00 per essere completa
     *
     * @param data fornita
     *
     * @return testo standard ISO senza OffsetSeconds
     */
    public String getISO(Date data) {
        return getISO(dateToLocalDateTime(data));
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana di una data fornita.
     * <p>
     * martedì
     * <p>
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il giorno della settimana in forma estesa
     */
    @Deprecated
    public String getDayWeekFull(Date data) {
        return getDayWeekFull(dateToLocalDate(data));
    }// end of method


    /**
     * Ritorna il numero delle ore di una data fornita.
     * <p>
     * 7
     * <p>
     * Usa LocalDateTime internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero delle ore
     */
    @Deprecated
    public int getOre(Date data) {
        return dateToLocalDateTime(data).getHour();
    }// end of method


    /**
     * Ritorna il numero dei minuti di una data fornita.
     * <p>
     * 43
     * <p>
     * Usa LocalDateTime internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero dei minuti
     */
    @Deprecated
    public int getMinuti(Date data) {
        return dateToLocalDateTime(data).getMinute();
    }// end of method


    /**
     * Ritorna il numero dei secondi di una data fornita.
     * <p>
     * 38
     * <p>
     * Usa LocalDateTime internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero dei secondi
     */
    @Deprecated
    public int getSecondi(Date data) {
        return dateToLocalDateTime(data).getSecond();
    }// end of method


    /**
     * Ritorna il numero dell'anno di una data fornita.
     * <p>
     * 2014
     * <p>
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @return il numero dell'anno
     */
    @Deprecated
    public int getYear(Date data) {
        return dateToLocalDate(data).getYear();
    }// end of method


    /**
     * Costruisce la data per il 1° gennaio dell'anno indicato.
     *
     * @param anno di riferimento
     *
     * @return primo gennaio dell'anno indicato
     */
    public LocalDate primoGennaio(int anno) {
        return LocalDate.of(anno, 1, 1);
    }// end of method


    /**
     * Costruisce la data per il 1° gennaio dell'anno corrente.
     *
     * @return primo gennaio dell'anno corrente
     */
    public LocalDate primoGennaio() {
        return primoGennaio(LocalDate.now().getYear());
    }// end of method


    /**
     * Costruisce la data per il 31° dicembre dell'anno indicato.
     *
     * @param anno di riferimento
     *
     * @return ultimo giorno dell'anno indicato
     */
    public LocalDate trentunDicembre(int anno) {
        return LocalDate.of(anno, 12, 31);
    }// end of method


    /**
     * Costruisce la data per il 31° dicembre dell'anno corrente.
     *
     * @return ultimo giorno dell'anno corrente
     */
    public LocalDate trentunDicembre() {
        return trentunDicembre(LocalDate.now().getYear());
    }// end of method


    /**
     * Costruisce la localData per il giorno dell'anno indicato.
     *
     * @param giorno di riferimento (numero progressivo dell'anno)
     *
     * @return localData
     */
    public LocalDate getLocalDateByDay(int giorno) {
        return LocalDate.ofYearDay(LocalDate.now().getYear(), giorno);
    }// end of single test


    /**
     * Forza la data all'ultimo millisecondo.
     * <p>
     *
     * @param dateIn la data da forzare
     *
     * @return la data con ore/minuti/secondi/millisecondi al valore massimo
     */
    public Date lastTime(Date dateIn) {
        Calendar calendario = getCal(dateIn);

        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        calendario.set(Calendar.MILLISECOND, 999);

        return calendario.getTime();
    }// end of method


    /**
     * Crea una data.
     * <p>
     *
     * @param giorno          il giorno del mese (1 per il primo)
     * @param numMeseDellAnno il mese dell'anno (1 per gennaio)
     * @param anno            l'anno
     *
     * @return la data creata
     */
    public Date creaData(int giorno, int numMeseDellAnno, int anno) {
        return creaData(giorno, numMeseDellAnno, anno, 0, 0, 0);
    }// end of method


    /**
     * Crea una data.
     * <p>
     *
     * @param giorno          il giorno del mese (1 per il primo)
     * @param numMeseDellAnno il mese dell'anno (1 per gennaio)
     * @param anno            l'anno
     * @param ora             ora (24H)
     * @param minuto          il minuto
     * @param secondo         il secondo
     *
     * @return la data creata
     */
    public Date creaData(int giorno, int numMeseDellAnno, int anno, int ora, int minuto, int secondo) {
        Calendar calendario;

        if (numMeseDellAnno > 0) {
            numMeseDellAnno--;
        }// fine del blocco if

        calendario = new GregorianCalendar(anno, numMeseDellAnno, giorno, ora, minuto, secondo);
        return calendario.getTime();
    }// end of method


    private Calendar getCal() {
        /* crea il calendario */
        Calendar calendario = new GregorianCalendar(0, 0, 0, 0, 0, 0);

        /**
         * regola il calendario come non-lenient (se la data non è valida non effettua la rotazione automatica dei
         * valori dei campi, es. 32-12-2004 non diventa 01-01-2005)
         */
        calendario.setLenient(false);

        return calendario;
    }// end of method


    /**
     * Calendario con regolata la data
     *
     * @param data da inserire nel calendario
     *
     * @return calendario regolato
     */
    private Calendar getCal(Date data) {
        Calendar calendario = getCal();
        calendario.setTime(data);
        return calendario;
    }// end of  method


    /**
     * Restituisce come stringa (intelligente) una durata espressa in long
     * - Meno di 1 secondo
     * - Meno di 1 minuto
     * - Meno di 1 ora
     * - Meno di 1 giorno
     * - Meno di 1 anno
     *
     * @return durata (arrotondata e semplificata) in forma leggibile
     */
    public String toText(long durata) {
        String tempo = "null";
        long div;
        long mod;

        if (durata < MAX_MILLISEC) {
            tempo = INFERIORE_SECONDO;
        } else {
            if (durata < MAX_SECONDI) {
                div = Math.floorDiv(durata, MAX_MILLISEC);
                mod = Math.floorMod(durata, MAX_MILLISEC);
                if (div < 60) {
                    tempo = div + SECONDI;
                } else {
                    tempo = "1" + MINUTI;
                }// fine del blocco if-else
            } else {
                if (durata < MAX_MINUTI) {
                    div = Math.floorDiv(durata, MAX_SECONDI);
                    mod = Math.floorMod(durata, MAX_SECONDI);
                    if (div < 60) {
                        tempo = div + MINUTI;
                    } else {
                        tempo = "1" + ORA;
                    }// fine del blocco if-else
                } else {
                    if (durata < MAX_ORE) {
                        div = Math.floorDiv(durata, MAX_MINUTI);
                        mod = Math.floorMod(durata, MAX_MINUTI);
                        if (div < 24) {
                            if (div == 1) {
                                tempo = div + ORA;
                            } else {
                                tempo = div + ORE;
                            }// fine del blocco if-els
                            tempo += " e " + toText(durata - (div * MAX_MINUTI));
                        } else {
                            tempo = "1" + GIORNO;
                        }// fine del blocco if-else
                    } else {
                        if (durata < MAX_GIORNI) {
                            div = Math.floorDiv(durata, MAX_ORE);
                            mod = Math.floorMod(durata, MAX_ORE);
                            if (div < 365) {
                                if (div == 1) {
                                    tempo = div + GIORNO;
                                } else {
                                    tempo = div + GIORNI;
                                }// fine del blocco if-else
                            } else {
                                tempo = "1" + ANNO;
                            }// fine del blocco if-else
                        } else {
                            div = Math.floorDiv(durata, MAX_GIORNI);
                            mod = Math.floorMod(durata, MAX_GIORNI);
                            if (div == 1) {
                                tempo = div + ANNO;
                            } else {
                                tempo = div + ANNI;
                            }// fine del blocco if-else
                        }// fine del blocco if-else
                    }// fine del blocco if-else
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del blocco if-else

        return tempo;
    }// end of method


    /**
     * Restituisce come stringa (intelligente) un durata espressa in long
     *
     * @return tempo in forma leggibile
     */
    public String deltaText(long inizio) {
        long fine = System.currentTimeMillis();
        return toText(fine - inizio);
    }// end of method


    /**
     * Costruisce tutti i giorni dell'anno
     * Considera anche l'anno bisestile
     * <p>
     * Restituisce un array di Map
     * Ogni mappa ha:
     * numeroMese
     * #progressivoNormale
     * #progressivoBisestile
     * nome  (numero per il primo del mese)
     * titolo (1° per il primo del mese)
     */
    public ArrayList<HashMap> getAllGiorni() {
        ArrayList<HashMap> listaAnno = new ArrayList<HashMap>();
        ArrayList<HashMap> listaMese;
        int progAnno = 0;

        for (int k = 1; k <= 12; k++) {
            listaMese = getGiorniMese(k, progAnno);
            listaAnno = array.somma(listaAnno, listaMese);
            progAnno += listaMese.size();
        }// end of for cycle

        return listaAnno;
    }// end of method


    /**
     * Costruisce tutti i giorni del mese
     * Considera anche l'anno bisestile
     * <p>
     * Restituisce un array di Map
     * Ogni mappa ha:
     * numeroMese
     * nomeMese
     * #progressivoNormale
     * #progressivoBisestile
     * nome  (numero per il primo del mese)
     * titolo (1° per il primo del mese)
     *
     * @param numMese  numero del mese, partendo da 1 per gennaio
     * @param progAnno numero del giorno nell'anno, partendo da 1 per il 1° gennaio
     *
     * @return lista di mappe, una per ogni giorno del mese considerato
     */
    public ArrayList<HashMap> getGiorniMese(int numMese, int progAnno) {
        ArrayList<HashMap> listaMese = new ArrayList<HashMap>();
        HashMap mappa;
        int giorniDelMese;
        String nomeMese;
        EAMese mese = EAMese.getMese(numMese);
        nomeMese = EAMese.getLong(numMese);
        giorniDelMese = EAMese.getGiorni(numMese, 2016);
        final int taglioBisestile = 60;
        String tag;
        String tagUno;

        for (int k = 1; k <= giorniDelMese; k++) {
            progAnno++;
            tag = k + SPAZIO + nomeMese;
            mappa = new HashMap();
            mappa.put(KEY_MAPPA_GIORNI_MESE_NUMERO, numMese);
            mappa.put(KEY_MAPPA_GIORNI_MESE_TESTO, nomeMese);
            mappa.put(KEY_MAPPA_GIORNI_NOME, tag);
            mappa.put(KEY_MAPPA_GIORNI_BISESTILE, progAnno);
            mappa.put(KEY_MAPPA_GIORNI_NORMALE, progAnno);
            mappa.put(KEY_MAPPA_GIORNI_MESE_MESE, mese);
            if (k == 1) {
                mappa.put(KEY_MAPPA_GIORNI_TITOLO, PRIMO_GIORNO_MESE + SPAZIO + nomeMese);
            } else {
                mappa.put(KEY_MAPPA_GIORNI_TITOLO, tag);
            }// fine del blocco if-else

            //--gestione degli anni bisestili
            if (progAnno == taglioBisestile) {
                mappa.put(KEY_MAPPA_GIORNI_NORMALE, 0);
            }// fine del blocco if
            if (progAnno > taglioBisestile) {
                mappa.put(KEY_MAPPA_GIORNI_NORMALE, progAnno - 1);
            }// fine del blocco if

            listaMese.add(mappa);
        } // fine del ciclo for

        return listaMese;
    }// end of method


    public int getNumSettimana(LocalDate giornoIn) {
        return giornoIn.getDayOfWeek().getValue();
    }// fine del metodo


    /**
     * Recupera il primo lunedì precedente al giorno indicato <br>
     */
    public LocalDate getFirstLunedì(LocalDate giorno) {
        LocalDate lunedi = null;
        LocalDate corrente = null;
        int tagLunedi = 1;

        for (int k = 0; k < 7; k++) {
            corrente = giorno.minusDays(k);
            if (getNumSettimana(corrente) == tagLunedi) {
                lunedi = corrente;
                break;
            }// end of if cycle
        }// end of for cycle

        return lunedi;
    }// end of method


    /**
     * Recupera un giorno tot giorni prima o dopo la data corrente <br>
     */
    public LocalDate getGiornoDelta(String textGiorniDelta) {
        int numGiorniDelta = 0;

        if (text.isValid(textGiorniDelta)) {
            try { // prova ad eseguire il codice
                numGiorniDelta = Integer.decode(textGiorniDelta);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return getGiornoDelta(numGiorniDelta);
    }// end of method


    /**
     * Recupera un giorno tot giorni prima o dopo la data corrente <br>
     */
    public LocalDate getGiornoDelta(int numGiorniDelta) {
        return LocalDate.now().plusDays(numGiorniDelta);
    }// end of method


    /**
     * Durata tra due momenti individuati da ora e minuti <br>
     */
    @Deprecated
    public int getDurata(int oraFine, int oraIni, int minFine, int minIni) {
        int durata = 0;
        int ore = 0;
        int minuti = 0;

        ore = oraFine - oraIni;
        durata += ore * 60;

        minuti = minFine - minIni;
        durata += minuti;

        return durata;
    }// end of method


    /**
     * Differenza (in giorni) tra due date (LocalDate) <br>
     *
     * @param giornoFine   data iniziale
     * @param giornoInizio data finale
     *
     * @return giorni di differenza
     */
    public int differenza(LocalDate giornoFine, LocalDate giornoInizio) {
        int differenza = 0;
        long fine = 0;
        long inizio = 0;
        Long delta;

        if (giornoFine != null && giornoInizio != null) {
            fine = localDateToDate(giornoFine).getTime();
            inizio = localDateToDate(giornoInizio).getTime();
            delta = fine - inizio;
            delta = delta / 1000;
            delta = delta / 60;
            delta = delta / 60;
            delta = delta / 24;
            differenza = delta.intValue();
        }// end of if cycle

        return differenza;
    }// end of method


    /**
     * Differenza (in ore) tra due orari (LocalTime) <br>
     * Arrotondamento matematico
     *
     * @param orarioInizio orario iniziale
     * @param orarioFine   orario finale
     *
     * @return minuti di differenza
     */
    public int differenza(LocalTime orarioFine, LocalTime orarioInizio) {
        int differenza = 0;
        int minutiFine = 0;
        int minutiInizio = 0;
        int minuti = 60;
        int ore = 24;
        int resto = 0;

        if (isValid(orarioFine) && isValid(orarioInizio)) {
            if (orarioFine.isAfter(orarioInizio)) {
                minutiFine = orarioFine.getHour() * minuti + orarioFine.getMinute();
                minutiInizio = orarioInizio.getHour() * minuti + orarioInizio.getMinute();
                differenza = minutiFine - minutiInizio;
                resto = differenza % minuti;
                differenza = differenza / minuti;
                if (resto > minuti / 2) {
                    differenza++;
                }// end of if cycle
            } else {
                minutiFine = orarioFine.getHour() * minuti + orarioFine.getMinute();
                minutiInizio = (ore - orarioInizio.getHour()) * minuti;
                if (orarioInizio.getMinute() > minuti / 2) {
                    minutiInizio = minutiInizio - orarioInizio.getMinute();
                }// end of if cycle
                differenza = minutiFine + minutiInizio;
                resto = differenza % minuti;
                differenza = differenza / minuti;
                if (resto > minuti / 2) {
                    differenza++;
                }// end of if cycle
            }// end of if/else cycle
        }// end of if cycle

        return differenza;
    }// end of method


    /**
     * Differenza (in minuti) tra due orari (LocalTime) <br>
     *
     * @param orarioInizio orario iniziale
     * @param orarioFine   orario finale
     *
     * @return minuti di differenza
     */
    public int durata(LocalTime orarioFine, LocalTime orarioInizio) {
        int durata = 0;
        int minutiFine = 0;
        int minutiInizio = 0;

        if (orarioFine != null && orarioInizio != null) {
            minutiFine = orarioFine.getHour() * 60 + orarioFine.getMinute();
            minutiInizio = orarioInizio.getHour() * 60 + orarioInizio.getMinute();
            durata = minutiFine - minutiInizio;
        }// end of if cycle

        return durata;
    }// end of method


    /**
     * Controlla la validità del localTime
     * Deve esistere (not null)
     * Deve avere valori delle ore o dei minuti
     *
     * @param localTime in ingresso da controllare
     *
     * @return vero se il localTime soddisfa le condizioni previste
     */
    public boolean isValid(LocalTime localTime) {
        boolean status = false;

        if (localTime != null) {
            if (localTime.getHour() > 0 || localTime.getMinute() > 0) {
                status = true;
            }// end of if cycle
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Distanza tra due orari
     * Deve esistere (not null)
     * Deve avere valori delle ore o dei minuti
     *
     * @param orarioInizio orario iniziale
     * @param orarioFine   orario finale
     *
     * @return LocalTime di differenza
     */
    public LocalTime periodo(LocalTime orarioFine, LocalTime orarioInizio) {
        LocalTime periodo = null;
        int durata = 0;
        int minuti = 60;
        int ore = 0;
        int resto = 0;

        if (orarioFine != null && orarioInizio != null) {
            durata = durata(orarioFine, orarioInizio);
            if (durata == 0) {
                periodo = LocalTime.of(0, 0);
            } else {
                if (durata >= minuti) {
                    ore = durata / minuti;
                    resto = durata % minuti;
                    periodo = LocalTime.of(ore, resto);
                } else {
                    periodo = LocalTime.of(0, durata);
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if cycle

        return periodo;
    }// end of method

}// end of class
