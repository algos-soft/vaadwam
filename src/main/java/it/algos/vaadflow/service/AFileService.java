package it.algos.vaadflow.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.SLASH;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 06-mar-2018
 * Time: 09:54
 * <p>
 * Gestione dei file di sistema
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AFileService.class); <br>
 * 2) AFileService.getInstance(); <br>
 * 3) @Autowired private AFileService fileService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * <p>
 * Fondamentalmente per l'accesso al file system si costruisce un'istanza della classe File a cui si applicano i seguenti metodi:
 * fileToBeChecked.exists()
 * directoryToBeChecked.exists()
 * fileToBeCreated.createNewFile()
 * directoryToBeCreated.mkdirs()
 * fileToBeDeleted.delete()
 * directoryToBeDeleted.delete()
 * directoryToBeDeleted.deleteOnExit()
 * FileUtils.forceDelete(directoryToBeDeleted)
 * Questa libreria costituisce un layer di collegamento per questi metodi base <br>
 * Viene restituito un messaggio di errore per i metodi principali <br>
 * <p>
 * Nel system, un file viene creato SOLO se esiste già il path completo <br>
 * In questa libreria, un file viene creato ANCHE se manca il path completo (se lo costruisce in automatico) <br>
 * Le richieste sono CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
 * Nel system tutti gli indirizzi che NON cominciano con '/' (slash) vengono riferiti alla directory di funzionamento del programma in uso <br>
 * In questa libreria vengono accettati SOLO indirizzi di root completi che inziano con '/' (slash) <br>
 * Alcuni metodi (quasi tutti) restituiscono un booleano <br>
 * Spesso  sono associati ad un metodo analogo ...str() che restituisce una stringa di errore <br>
 */
@Service
@Slf4j
public class AFileService extends AbstractService {

    public static final String PARAMETRO_NULLO = "Il parametro in ingresso è nullo";

    public static final String PATH_NULLO = "Il path in ingresso è nullo o vuoto";

    public static final String PATH_NOT_ABSOLUTE = "Il primo carattere del path NON è uno '/' (slash)";

    public static final String NON_ESISTE_FILE = "Il file non esiste";

    public static final String PATH_SENZA_SUFFIX = "Manca il 'suffix' terminale";

    public static final String PATH_FILE_ESISTENTE = "Esiste già il file";

    public static final String NON_E_FILE = "Non è un file";

    public static final String NON_CREATO_FILE = "Il file non è stato creato";

    public static final String NON_COPIATO_FILE = "Il file non è stato copiato";

    public static final String NON_CANCELLATO_FILE = "Il file non è stato cancellato";

    public static final String NON_CANCELLATA_DIRECTORY = "La directory non è stata cancellata";

    public static final String NON_ESISTE_DIRECTORY = "La directory non esiste";

    public static final String NON_CREATA_DIRECTORY = "La directory non è stata creata";


    public static final String NON_E_DIRECTORY = "Non è una directory";


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Private final property
     */
    private static final AFileService INSTANCE = new AFileService();


    /**
     * Private constructor to avoid client applications to use constructor
     */
    public AFileService() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AFileService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeChecked path completo del file che DEVE cominciare con '/' SLASH
     *
     * @return true se il file esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteFile(String absolutePathFileWithSuffixToBeChecked) {
        return isEsisteFileStr(absolutePathFileWithSuffixToBeChecked).equals(VUOTA);
    }// end of method


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeChecked path completo del file che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file esiste
     */
    public String isEsisteFileStr(String absolutePathFileWithSuffixToBeChecked) {
        String risposta = VUOTA;

        if (text.isEmpty(absolutePathFileWithSuffixToBeChecked)) {
            return PATH_NULLO;
        }// end of if cycle

        if (text.isNotSlasch(absolutePathFileWithSuffixToBeChecked)) {
            return PATH_NOT_ABSOLUTE;
        }// end of if cycle

        risposta = isEsisteFileStr(new File(absolutePathFileWithSuffixToBeChecked));
        if (!risposta.equals(VUOTA)) {
            if (isEsisteDirectory(new File(absolutePathFileWithSuffixToBeChecked))) {
                return NON_E_FILE;
            }// end of if cycle

            if (text.isNotSuffix(absolutePathFileWithSuffixToBeChecked)) {
                return PATH_SENZA_SUFFIX;
            }// end of if cycle
        }// end of if cycle

        return risposta;
    }// end of method


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se il file esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteFile(File fileToBeChecked) {
        return isEsisteFileStr(fileToBeChecked).equals(VUOTA);
    }// end of method


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file esiste
     */
    public String isEsisteFileStr(File fileToBeChecked) {
        if (fileToBeChecked == null) {
            return PARAMETRO_NULLO;
        }// end of if cycle

        if (text.isEmpty(fileToBeChecked.getName())) {
            return PATH_NULLO;
        }// end of if cycle

        if (!fileToBeChecked.getPath().equals(fileToBeChecked.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }// end of if/else cycle

        if (fileToBeChecked.exists()) {
            if (fileToBeChecked.isFile()) {
                return VUOTA;
            } else {
                return NON_E_FILE;
            }// end of if/else cycle
        } else {
            if (text.isNotSuffix(fileToBeChecked.getAbsolutePath())) {
                return PATH_SENZA_SUFFIX;
            }// end of if cycle

            if (!fileToBeChecked.exists()) {
                return NON_ESISTE_FILE;
            }// end of if cycle

            return VUOTA;
        }// end of if/else cycle
    }// end of method


    /**
     * Controlla l'esistenza di una directory <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param absolutePathDirectoryToBeChecked path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteDirectory(String absolutePathDirectoryToBeChecked) {
        return isEsisteDirectoryStr(absolutePathDirectoryToBeChecked).equals(VUOTA);
    }// end of method


    /**
     * Controlla l'esistenza di una directory <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param absolutePathDirectoryToBeChecked path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se la directory esiste
     */
    public String isEsisteDirectoryStr(String absolutePathDirectoryToBeChecked) {
        if (text.isEmpty(absolutePathDirectoryToBeChecked)) {
            return PATH_NULLO;
        }// end of if cycle

        if (text.isNotSlasch(absolutePathDirectoryToBeChecked)) {
            return PATH_NOT_ABSOLUTE;
        }// end of if cycle

        return isEsisteDirectoryStr(new File(absolutePathDirectoryToBeChecked));
    }// end of method


    /**
     * Controlla l'esistenza di una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param directoryToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteDirectory(File directoryToBeChecked) {
        return isEsisteDirectoryStr(directoryToBeChecked).equals(VUOTA);
    }// end of method


    /**
     * Controlla l'esistenza di una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param directoryToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file esiste
     */
    public String isEsisteDirectoryStr(File directoryToBeChecked) {
        if (directoryToBeChecked == null) {
            return PARAMETRO_NULLO;
        }// end of if cycle

        if (text.isEmpty(directoryToBeChecked.getName())) {
            return PATH_NULLO;
        }// end of if cycle

        if (!directoryToBeChecked.getPath().equals(directoryToBeChecked.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }// end of if/else cycle

        if (directoryToBeChecked.exists()) {
            if (directoryToBeChecked.isDirectory()) {
                return VUOTA;
            } else {
                return NON_E_DIRECTORY;
            }// end of if/else cycle
        } else {
            return NON_ESISTE_DIRECTORY;
        }// end of if/else cycle
    }// end of method


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param absolutePathFileWithSuffixToBeCreated path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return true se il file è stato creato, false se non sono rispettate le condizioni della richiesta
     */
    public boolean creaFile(String absolutePathFileWithSuffixToBeCreated) {
        return creaFileStr(absolutePathFileWithSuffixToBeCreated).equals(VUOTA);
    }// end of method


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param absolutePathFileWithSuffixToBeCreated path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String creaFileStr(String absolutePathFileWithSuffixToBeCreated) {

        if (text.isEmpty(absolutePathFileWithSuffixToBeCreated)) {
            return PATH_NULLO;
        }// end of if cycle

        return creaFileStr(new File(absolutePathFileWithSuffixToBeCreated));
    }// end of method


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param fileToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se il file è stato creato, false se non sono rispettate le condizioni della richiesta
     */
    public boolean creaFile(File fileToBeCreated) {
        return creaFileStr(fileToBeCreated).equals(VUOTA);
    }// end of method


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param fileToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String creaFileStr(File fileToBeCreated) {
        if (fileToBeCreated == null) {
            return PARAMETRO_NULLO;
        }// end of if cycle

        if (text.isEmpty(fileToBeCreated.getName())) {
            return PATH_NULLO;
        }// end of if cycle

        if (!fileToBeCreated.getPath().equals(fileToBeCreated.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }// end of if/else cycle

        if (text.isNotSuffix(fileToBeCreated.getAbsolutePath())) {
            return PATH_SENZA_SUFFIX;
        }// end of if cycle

        try { // prova ad eseguire il codice
            fileToBeCreated.createNewFile();
        } catch (Exception unErrore) { // intercetta l'errore
            return creaDirectoryParentAndFile(fileToBeCreated);
        }// fine del blocco try-catch

        return fileToBeCreated.exists() ? VUOTA : NON_CREATO_FILE;
    }// end of method


    /**
     * Creazioni di una directory 'parent' <br>
     * Se manca il path completo alla creazione di un file, creo la directory 'parent' di quel file <br>
     * Riprovo la creazione del file <br>
     */
    public String creaDirectoryParentAndFile(File unFile) {
        String risposta = NON_CREATO_FILE;
        String parentDirectoryName;
        File parentDirectoryFile = null;
        boolean parentDirectoryCreata = false;

        if (unFile != null) {
            parentDirectoryName = unFile.getParent();
            parentDirectoryFile = new File(parentDirectoryName);
            parentDirectoryCreata = parentDirectoryFile.mkdirs();
        }// end of if cycle

        if (parentDirectoryCreata) {
            try { // prova ad eseguire il codice
                unFile.createNewFile();
                risposta = VUOTA;
            } catch (Exception unErrore) { // intercetta l'errore
                System.out.println("Errore nel path per la creazione di un file");
            }// fine del blocco try-catch
        }// end of if cycle

        return risposta;
    }// end of method


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeCreated path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory è stata creata, false se non sono rispettate le condizioni della richiesta
     */
    public boolean creaDirectory(String absolutePathDirectoryToBeCreated) {
        return creaDirectoryStr(absolutePathDirectoryToBeCreated).equals(VUOTA);
    }// end of method


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeCreated path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String creaDirectoryStr(String absolutePathDirectoryToBeCreated) {
        if (text.isEmpty(absolutePathDirectoryToBeCreated)) {
            return PATH_NULLO;
        }// end of if cycle

        return creaDirectoryStr(new File(absolutePathDirectoryToBeCreated));
    }// end of method


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory è stata creata, false se non sono rispettate le condizioni della richiesta
     */
    public boolean creaDirectory(File directoryToBeCreated) {
        return creaDirectoryStr(directoryToBeCreated).equals(VUOTA);
    }// end of method


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String creaDirectoryStr(File directoryToBeCreated) {
        if (directoryToBeCreated == null) {
            return PARAMETRO_NULLO;
        }// end of if cycle

        if (text.isEmpty(directoryToBeCreated.getName())) {
            return PATH_NULLO;
        }// end of if cycle

        if (!directoryToBeCreated.getPath().equals(directoryToBeCreated.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }// end of if/else cycle

        if (!text.isNotSuffix(directoryToBeCreated.getAbsolutePath())) {
            return NON_E_DIRECTORY;
        }// end of if cycle

        try { // prova ad eseguire il codice
            directoryToBeCreated.mkdirs();
        } catch (Exception unErrore) { // intercetta l'errore
            return NON_CREATA_DIRECTORY;
        }// fine del blocco try-catch

        return directoryToBeCreated.exists() ? VUOTA : NON_CREATA_DIRECTORY;
    }// end of method


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeCanceled path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return true se il file è stato cancellato oppure non esisteva
     */
    public boolean deleteFile(String absolutePathFileWithSuffixToBeCanceled) {
        return deleteFileStr(absolutePathFileWithSuffixToBeCanceled).equals(VUOTA);
    }// end of method


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeCanceled path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return testo di errore, vuoto se il file è stato cancellato
     */
    public String deleteFileStr(String absolutePathFileWithSuffixToBeCanceled) {
        if (text.isEmpty(absolutePathFileWithSuffixToBeCanceled)) {
            return PATH_NULLO;
        }// end of if cycle

        return deleteFileStr(new File(absolutePathFileWithSuffixToBeCanceled));
    }// end of method


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se il file è stato cancellato oppure non esisteva
     */
    public boolean deleteFile(File fileToBeDeleted) {
        return deleteFileStr(fileToBeDeleted).equals(VUOTA);
    }// end of method


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se il file è stato creato
     */
    public String deleteFileStr(File fileToBeDeleted) {

        if (fileToBeDeleted == null) {
            return PARAMETRO_NULLO;
        }// end of if cycle

        if (text.isEmpty(fileToBeDeleted.getName())) {
            return PATH_NULLO;
        }// end of if cycle

        if (!fileToBeDeleted.getPath().equals(fileToBeDeleted.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }// end of if/else cycle

        if (text.isNotSuffix(fileToBeDeleted.getAbsolutePath())) {
            return PATH_SENZA_SUFFIX;
        }// end of if cycle

        if (!fileToBeDeleted.exists()) {
            return NON_ESISTE_FILE;
        }// end of if cycle

        if (fileToBeDeleted.delete()) {
            return VUOTA;
        } else {
            return NON_CANCELLATO_FILE;
        }// end of if/else cycle

    }// end of method


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeDeleted path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory è stato cancellato oppure non esisteva
     */
    public boolean deleteDirectory(String absolutePathDirectoryToBeDeleted) {
        return deleteDirectoryStr(absolutePathDirectoryToBeDeleted).equals(VUOTA);
    }// end of method


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeDeleted path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se la directory è stata cancellata
     */
    public String deleteDirectoryStr(String absolutePathDirectoryToBeDeleted) {
        if (text.isEmpty(absolutePathDirectoryToBeDeleted)) {
            return PATH_NULLO;
        }// end of if cycle

        return deleteDirectoryStr(new File(absolutePathDirectoryToBeDeleted));
    }// end of method


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory è stata cancellata oppure non esisteva
     */
    public boolean deleteDirectory(File directoryToBeDeleted) {
        return deleteDirectoryStr(directoryToBeDeleted).equals(VUOTA);
    }// end of method


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed inziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return testo di errore, vuoto se la directory è stata cancellata
     */
    public String deleteDirectoryStr(File directoryToBeDeleted) {
        if (directoryToBeDeleted == null) {
            return PARAMETRO_NULLO;
        }// end of if cycle

        if (text.isEmpty(directoryToBeDeleted.getName())) {
            return PATH_NULLO;
        }// end of if cycle

        if (!directoryToBeDeleted.getPath().equals(directoryToBeDeleted.getAbsolutePath())) {
            return PATH_NOT_ABSOLUTE;
        }// end of if/else cycle

        if (!directoryToBeDeleted.exists()) {
            return NON_ESISTE_DIRECTORY;
        }// end of if cycle

        if (directoryToBeDeleted.delete()) {
            return VUOTA;
        } else {
            return NON_CANCELLATA_DIRECTORY;
        }// end of if/else cycle
    }// end of method


    /**
     * Copia un file
     * <p>
     * Se manca il file sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste il file destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo del file sorgente
     * @param destPath nome completo del file destinazione
     *
     * @return true se il file è stato copiato
     */
    @Deprecated
    public boolean copyFile(String srcPath, String destPath) {
        return copyFileStr(srcPath, destPath) == VUOTA;
    }// end of method


    /**
     * Copia un file sovrascrivendolo se già esistente
     * <p>
     * Se manca il file sorgente, non fa nulla <br>
     * Se esiste il file destinazione, lo cancella prima di copiarlo <br>
     *
     * @param srcPath  nome completo del file sorgente
     * @param destPath nome completo del file destinazione
     *
     * @return true se il file è stato copiato
     */
    public boolean copyFileDeletingAll(String srcPath, String destPath) {
        if (!isEsisteFile(srcPath)) {
            return false;
        }// end of if cycle

        if (isEsisteFile(destPath)) {
            deleteFile(destPath);
        }// end of if cycle

        return copyFileStr(srcPath, destPath) == VUOTA;
    }// end of method


    /**
     * Copia un file solo se non già esistente
     * <p>
     * Se manca il file sorgente, non fa nulla <br>
     * Se esiste il file destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo del file sorgente
     * @param destPath nome completo del file destinazione
     *
     * @return true se il file è stato copiato
     */
    public boolean copyFileOnlyNotExisting(String srcPath, String destPath) {
        return copyFileStr(srcPath, destPath) == VUOTA;
    }// end of method


    /**
     * Copia un file
     * <p>
     * Se manca il file sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste il file destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo del file sorgente
     * @param destPath nome completo del file destinazione
     *
     * @return testo di errore, vuoto se il file è stato copiato
     */
    public String copyFileStr(String srcPath, String destPath) {
        String risposta = VUOTA;
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);

        if (!isEsisteFile(srcPath)) {
            return NON_ESISTE_FILE;
        }// end of if cycle

        if (isEsisteFile(destPath)) {
            return PATH_FILE_ESISTENTE;
        }// end of if cycle

        try { // prova ad eseguire il codice
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            return NON_COPIATO_FILE;
        }// fine del blocco try-catch

        return risposta;
    }// end of method


    /**
     * Copia una directory
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return true se la directory  è stata copiata
     */
    @Deprecated
    public boolean copyDirectory(String srcPath, String destPath) {
        return copyDirectoryAddingOnly(srcPath, destPath);
    }// end of method


    /**
     * Copia una directory sostituendo integralmente quella eventualmente esistente <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione, la cancella prima di ricopiarla <br>
     * Tutte i files e le subdirectories originali vengono cancellata <br>
     *
     * @param srcPath  nome parziale del path sorgente
     * @param destPath nome parziale del path destinazione
     * @param dirName  nome della directory da copiare
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryDeletingAll(String srcPath, String destPath, String dirName) {
        return copyDirectoryDeletingAll(srcPath + dirName, destPath + dirName);
    }// end of method


    /**
     * Copia una directory sostituendo integralmente quella eventualmente esistente <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione, la cancella prima di ricopiarla <br>
     * Tutte i files e le subdirectories originali vengono cancellata <br>
     *
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryDeletingAll(String srcPath, String destPath) {
        boolean copiata = false;
        File srcDir = new File(srcPath);
        File destDir = new File(destPath);

        if (!isEsisteDirectory(srcPath)) {
            return false;
        }// end of if cycle

        if (isEsisteDirectory(destPath)) {
            try { // prova ad eseguire il codice
                FileUtils.forceDelete(new File(destPath));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        if (isEsisteDirectory(destPath)) {
            return false;
        } else {
            try { // prova ad eseguire il codice
                FileUtils.copyDirectory(srcDir, destDir);
                return true;
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if/else cycle

        return copiata;
    }// end of method


    /**
     * Copia una directory solo se non esisteva <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione, non fa nulla <br>
     *
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryOnlyNotExisting(String srcPath, String destPath) {
        if (!isEsisteDirectory(srcPath)) {
            return false;
        }// end of if cycle

        if (isEsisteDirectory(destPath)) {
            return false;
        }// end of if cycle

        return copyDirectoryDeletingAll(srcPath, destPath);
    }// end of method


    /**
     * Copia una directory aggiungendo files e subdirectories a quelli eventualmente esistenti <br>
     * Lascia inalterate subdirectories e files già esistenti <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory destinazione, aggiunge files e subdirectories <br>
     * Tutti i files e le subdirectories esistenti vengono mantenuti <br>
     * Tutte le aggiunte sono ricorsive nelle subdirectories <br>
     *
     * @param srcPath  nome parziale del path sorgente
     * @param destPath nome parziale del path destinazione
     * @param dirName  nome della directory da copiare
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryAddingOnly(String srcPath, String destPath, String dirName) {
        return copyDirectoryAddingOnly(srcPath + dirName, destPath + dirName);
    }// end of method


    /**
     * Copia una directory aggiungendo files e subdirectories a quelli eventualmente esistenti <br>
     * Lascia inalterate subdirectories e files già esistenti <br>
     * <p>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se manca la directory di destinazione, la crea <br>
     * Se esiste la directory destinazione, aggiunge files e subdirectories <br>
     * Tutti i files e le subdirectories esistenti vengono mantenuti <br>
     * Tutte le aggiunte sono ricorsive nelle subdirectories <br>
     *
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return true se la directory  è stata copiata
     */
    public boolean copyDirectoryAddingOnly(String srcPath, String destPath) {
        boolean copiata = false;
        File srcDir = new File(srcPath);
        File destDir = new File(destPath);

        if (!isEsisteDirectory(srcPath)) {
            return false;
        }// end of if cycle

        try { // prova ad eseguire il codice
            FileUtils.copyDirectory(srcDir, destDir);
            copiata = true;
        } catch (IOException e) {
            e.printStackTrace();
        }// fine del blocco try-catch

        return copiata;
    }// end of method


    /**
     * Scrive un file
     * Se non esiste, non fa nulla
     *
     * @param pathFileToBeWritten nome completo del file
     * @param text                contenuto del file
     */
    public boolean scriveNewFile(String pathFileToBeWritten, String text) {
        return scriveFile(pathFileToBeWritten, text, false);
    }// end of method


    /**
     * Scrive un file
     * Se non esiste, lo crea
     *
     * @param pathFileToBeWritten nome completo del file
     * @param text                contenuto del file
     * @param sovrascrive         anche se esiste già
     */
    public boolean scriveFile(String pathFileToBeWritten, String text, boolean sovrascrive) {
        boolean status = false;
        File fileToBeWritten;
        FileWriter fileWriter;

        if (isEsisteFile(pathFileToBeWritten)) {
            if (sovrascrive) {
                status = sovraScriveFile(pathFileToBeWritten, text);
                System.out.println("Il file " + pathFileToBeWritten + " esisteva già ed è stato aggiornato");
            } else {
                System.out.println("Il file " + pathFileToBeWritten + " esisteva già e non è stato modificato");
                return false;
            }// end of if/else cycle
        } else {
            status = creaFile(pathFileToBeWritten);
            if (status) {
                status = sovraScriveFile(pathFileToBeWritten, text);
                System.out.println("Il file " + pathFileToBeWritten + " non esisteva ed è stato creato");
            } else {
                System.out.println("Il file " + pathFileToBeWritten + " non esisteva e non è stato creato");
                return false;
            }// end of if/else cycle
        }// end of if/else cycle

        return status;
    }// end of method


    /**
     * Sovrascrive un file
     *
     * @param pathFileToBeWritten nome completo del file
     * @param text                contenuto del file
     */
    public boolean sovraScriveFile(String pathFileToBeWritten, String text) {
        boolean status = false;
        File fileToBeWritten;
        FileWriter fileWriter = null;

        if (isEsisteFile(pathFileToBeWritten)) {
            fileToBeWritten = new File(pathFileToBeWritten);
            try { // prova ad eseguire il codice
                fileWriter = new FileWriter(fileToBeWritten);
                fileWriter.write(text);
                fileWriter.flush();
                status = true;
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            } finally {
                try { // prova ad eseguire il codice
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                }// fine del blocco try-catch
            }// fine del blocco try-catch-finally
        } else {
            System.out.println("Il file " + pathFileToBeWritten + " non esiste e non è stato creato");
        }// end of if/else cycle

        return status;
    }// end of method


    /**
     * Legge un file
     *
     * @param pathFileToBeRead nome completo del file
     */
    public String leggeFile(String pathFileToBeRead) {
        String testo = "";
        String aCapo = "\n";
        String currentLine;

        //-- non va, perché se arriva it/algos/Alfa.java becca anche il .java
//        nameFileToBeRead=  nameFileToBeRead.replaceAll("\\.","/");

        try (BufferedReader br = new BufferedReader(new FileReader(pathFileToBeRead))) {
            while ((currentLine = br.readLine()) != null) {
                testo += currentLine;
                testo += "\n";
            }// fine del blocco while

            testo = text.levaCoda(testo, aCapo);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return testo;
    }// end of method


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getSubDirectoriesAbsolutePathName(String pathDirectoryToBeScanned) {
        List<String> subDirectoryName = new ArrayList<>();
        List<File> subDirectory = getSubDirectories(pathDirectoryToBeScanned);

        if (subDirectory != null) {
            for (File file : subDirectory) {
                subDirectoryName.add(file.getAbsolutePath());
            }// end of for cycle
        }// end of if cycle

        return subDirectoryName;
    }// end of method


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getSubDirectoriesName(String pathDirectoryToBeScanned) {
        List<String> subDirectoryName = new ArrayList<>();
        List<File> subDirectory = getSubDirectories(pathDirectoryToBeScanned);

        if (subDirectory != null) {
            for (File file : subDirectory) {
                subDirectoryName.add(file.getName());
            }// end of for cycle
        }// end of if cycle

        return subDirectoryName;
    }// end of method


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<File> getSubDirectories(String pathDirectoryToBeScanned) {
        return getSubDirectories(new File(pathDirectoryToBeScanned));
    }// end of method


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param directoryToBeScanned della directory
     *
     * @return lista di sub-directory SENZA files
     */
    public List<File> getSubDirectories(File directoryToBeScanned) {
        List<File> subDirectory = new ArrayList<>();
        File[] allFiles = null;

        if (directoryToBeScanned != null) {
            allFiles = directoryToBeScanned.listFiles();
        }// end of if cycle

        if (allFiles != null) {
            subDirectory = new ArrayList<>();
            for (File file : allFiles) {
                if (file.isDirectory()) {
                    subDirectory.add(file);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return subDirectory;
    }// end of method


    /**
     * Estrae le sub-directories da un sotto-livello di una directory <br>
     * La dirInterna non è, ovviamente, al primo livello della directory altrimenti chiamerei getSubDirectories <br>
     *
     * @param pathDirectoryToBeScanned della directory
     * @param dirInterna               da scandagliare
     *
     * @return lista di sub-directory SENZA files
     */
    public List<File> getSubSubDirectories(String pathDirectoryToBeScanned, String dirInterna) {
        return getSubSubDirectories(new File(pathDirectoryToBeScanned), dirInterna);
    }// end of method


    /**
     * Estrae le sub-directories da un sotto-livello di una directory <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return lista di sub-directory SENZA files
     */
    public List<File> getSubSubDirectories(File directoryToBeScanned, String dirInterna) {
        String subDir = directoryToBeScanned.getAbsolutePath();

        if (subDir.endsWith(SLASH)) {
            subDir = text.levaCoda(subDir, SLASH);
        }// end of if cycle

        if (dirInterna.startsWith(SLASH)) {
            dirInterna = text.levaTesta(dirInterna, SLASH);
        }// end of if cycle

        String newPath = subDir + SLASH + dirInterna;
        File subFile = new File(newPath);

        return getSubDirectories(subFile);
    }// end of method


    /**
     * Controlla se una sotto-directory è piena <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return true se è piena
     */
    public boolean isEsisteSubDirectory(File directoryToBeScanned, String dirInterna) {
        return array.isValid(getSubSubDirectories(directoryToBeScanned, dirInterna));
    }// end of method


    /**
     * Controlla se una sotto-directory è vuota <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return true se è vuota
     */
    public boolean isVuotaSubDirectory(File directoryToBeScanned, String dirInterna) {
        return array.isEmpty(getSubSubDirectories(directoryToBeScanned, dirInterna));
    }// end of single test


    /**
     * Estrae i files da una directory
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getFiles(String pathDirectoryToBeScanned) {
        List<String> files = null;
        File[] allFiles = null;
        File directory = new File(pathDirectoryToBeScanned);

        if (directory != null) {
            allFiles = directory.listFiles();
        }// end of if cycle

        if (allFiles != null) {
            files = new ArrayList<>();
            for (File file : allFiles) {
                if (!file.isDirectory()) {
                    files.add(file.getName());
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return files;
    }// end of method


    /**
     * Elimina l'ultima directory da un path <br>
     * <p>
     * Esegue solo se il path è valido
     * Elimina spazi vuoti iniziali e finali
     *
     * @param pathIn in ingresso
     *
     * @return path ridotto in uscita
     */
    public String levaDirectoryFinale(final String pathIn) {
        String pathOut = pathIn.trim();

        if (text.isValid(pathOut)) {
            if (pathOut.contains(SLASH)) {
                pathOut = text.levaCoda(pathOut, SLASH);
                pathOut = text.levaCodaDa(pathOut, SLASH) + SLASH;
            }// fine del blocco if
        }// fine del blocco if

        return pathOut.trim();
    }// end of method


    /**
     * Sposta un file da una directoy ad un'altra <br>
     * Esegue solo se il path sorgente esiste <br>
     * Esegue solo se il path destinazione NON esiste <br>
     * Viene cancellato il file sorgente <br>
     *
     * @param pathFileToBeRead  posizione iniziale del file da spostare
     * @param pathFileToBeWrite posizione iniziale del file da spostare
     *
     * @return testo di errore, vuoto se il file è stato spostato
     */
    public boolean spostaFile(String pathFileToBeRead, String pathFileToBeWrite) {
        return spostaFileStr(pathFileToBeRead, pathFileToBeWrite) == VUOTA;
    }// end of method


    /**
     * Sposta un file da una directoy ad un'altra <br>
     * Esegue solo se il path sorgente esiste <br>
     * Esegue solo se il path destinazione NON esiste <br>
     * Viene cancellato il file sorgente <br>
     *
     * @param pathFileToBeRead  posizione iniziale del file da spostare
     * @param pathFileToBeWrite posizione iniziale del file da spostare
     *
     * @return testo di errore, vuoto se il file è stato spostato
     */
    public String spostaFileStr(String pathFileToBeRead, String pathFileToBeWrite) {
        String status = VUOTA;

        if (text.isValid(pathFileToBeRead) && text.isValid(pathFileToBeWrite)) {
            status = copyFileStr(pathFileToBeRead, pathFileToBeWrite);
        } else {
            return PATH_NULLO;
        }// end of if/else cycle

        if (status.equals(VUOTA)) {
            status = deleteFileStr(pathFileToBeRead);
        }// end of if cycle

        return status;
    }// end of method


//    /**
//     * Copia tutta una directory ESCLUSO il file indicato <br>
//     * <p>
//     * Esegue solo se il il file esiste nel srcPath <br>
//     * Esegue solo se il il file esiste nel destPath <br>
//     * Esegue solo se la dir interna del file è la stessa in srcPath e destPath <br>
//     * Viene ignorato il file sorgente srcPath + dirFileToBeKeep <br>
//     * Viene mantenuto il file esistente destPath + dirFileToBeKeep <br>
//     *
//     * @param srcPath         nome completo della directory sorgente
//     * @param destPath        nome completo della directory destinazione
//     * @param dirFileToBeKeep dir interna del file da mantenere - deve essere uguale in srcPath e in destPath
//     *
//     * @return testo di errore, vuoto se la directory è stata copiata ed il il file mantenuto originale
//     */
//    public String copyDirectoryLessFile(String srcPath, String destPath, String dirFileToBeKeep) {
//        String status = VUOTA;
//
//        return status;
//    }// end of method

}// end of class
