package it.algos.vaadflow.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
 */
@Service
@Slf4j
public class AFileService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private final property
     */
    private static final AFileService INSTANCE = new AFileService();

//    /**
//     * Private constructor to avoid client applications to use constructor
//     */
//    private AFileService() {
//    }// end of constructor

    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AFileService getInstance() {
        return INSTANCE;
    }// end of static method

    /**
     * Controlla l'esistenza di un file
     *
     * @param pathFileToBeChecked nome completo del file
     *
     * @return true se il file esiste
     * false se non è un file o se non esiste
     */
    public boolean isEsisteFile(String pathFileToBeChecked) {
        return isEsisteFile(new File(pathFileToBeChecked));
    }// end of method


    /**
     * Controlla l'esistenza di un file
     *
     * @param fileToBeChecked file col path completo
     *
     * @return true se il file esiste
     * false se non è un file o se non esiste
     */
    public boolean isEsisteFile(File fileToBeChecked) {
        boolean status = false;

        status = fileToBeChecked.exists();
        if (status) {
            if (fileToBeChecked.isFile()) {
                System.out.println("Il file " + fileToBeChecked + " esiste");
            } else {
                System.out.println(fileToBeChecked + " non è un file");
                status = false;
            }// end of if/else cycle
        } else {
            System.out.println("Il file " + fileToBeChecked + " non esiste");
        }// end of if/else cycle

        return status;
    }// end of method


    /**
     * Controlla l'esistenza di una directory
     *
     * @param pathDirectoryToBeChecked nome completo della directory
     *
     * @return true se la directory esiste
     * false se non è una directory o se non esiste
     */
    public boolean isEsisteDirectory(String pathDirectoryToBeChecked) {
        return isEsisteDirectory(new File(pathDirectoryToBeChecked));
    }// end of method


    /**
     * Controlla l'esistenza di una directory
     *
     * @param directoryToBeChecked file col path completo
     *
     * @return true se la directory esiste
     * false se non è una directory o se non esiste
     */
    public boolean isEsisteDirectory(File directoryToBeChecked) {
        boolean status = false;

        status = directoryToBeChecked.exists();
        if (status) {
            if (directoryToBeChecked.isDirectory()) {
                System.out.println("La directory " + directoryToBeChecked + " esiste");
            } else {
                System.out.println(directoryToBeChecked + " non è una directory");
                status = false;
            }// end of if/else cycle
        } else {
            System.out.println("La directory " + directoryToBeChecked + " non esiste");
        }// end of if/else cycle

        return status;
    }// end of method


    /**
     * Cancella un file
     *
     * @param pathFileToBeDeleted nome completo del file
     */
    public boolean deleteFile(String pathFileToBeDeleted) {
        return deleteFile(new File(pathFileToBeDeleted));
    }// end of method


    /**
     * Cancella un file
     *
     * @param fileToBeDeleted file col path completo
     *
     * @return true se il file è stata cancellata
     * false se non è stato cancellato o se non esiste
     */
    public boolean deleteFile(File fileToBeDeleted) {
        boolean status = false;

        status = fileToBeDeleted.exists();
        if (status) {
            if (fileToBeDeleted.isFile()) {
                status = fileToBeDeleted.delete();
                if (status) {
                    System.out.println("Il file " + fileToBeDeleted + " è stata cancellato");
                } else {
                    System.out.println("Il file " + fileToBeDeleted + " non è stata cancellato, perché non ci sono riuscito");
                }// end of if/else cycle
            } else {
                System.out.println(fileToBeDeleted + " non è stato cancellato, perché non è un file");
                status = false;
            }// end of if/else cycle
        } else {
            System.out.println("Il file " + fileToBeDeleted + " non è stata cancellato, perché non esiste");
        }// end of if/else cycle

        return status;
    }// end of method


    /**
     * Cancella una directory
     *
     * @param pathDirectoryToBeDeleted nome completo della directory
     */
    public boolean deleteDirectory(String pathDirectoryToBeDeleted) {
        return deleteDirectory(new File(pathDirectoryToBeDeleted.toLowerCase()));
    }// end of method


    /**
     * Cancella una directory
     *
     * @param directoryToBeDeleted file col path completo
     *
     * @return true se la directory è stata cancellata
     * false se non è stata cancellata o se non esiste
     */
    public boolean deleteDirectory(File directoryToBeDeleted) {
        boolean status = false;

        status = directoryToBeDeleted.exists();
        if (status) {
            if (directoryToBeDeleted.isDirectory()) {
                status = FileSystemUtils.deleteRecursively(directoryToBeDeleted);
                if (status) {
                    System.out.println("La directory " + directoryToBeDeleted + " è stata cancellata");
                } else {
                    System.out.println("La directory " + directoryToBeDeleted + " non è stata cancellata, perché non ci sono riuscito");
                }// end of if/else cycle
            } else {
                System.out.println(directoryToBeDeleted + " non è stato cancellato, perché non è una directory");
                status = false;
            }// end of if/else cycle
        } else {
            System.out.println("La directory " + directoryToBeDeleted + " non è stata cancellata, perché non esiste");
        }// end of if/else cycle

        return status;
    }// end of method


    /**
     * Crea un file
     *
     * @param pathFileToBeCreated nome completo del file
     */
    public boolean creaFile(String pathFileToBeCreated) {
        return creaFile(new File(pathFileToBeCreated));
    }// end of method


    /**
     * Crea un file
     *
     * @param fileToBeCreated file col path completo
     */
    public boolean creaFile(File fileToBeCreated) {
        boolean status = false;

        status = fileToBeCreated.exists();
        if (status) {
            System.out.println("Il file " + fileToBeCreated + " non è stato creato, perché esiste già");
        } else {
            try { // prova ad eseguire il codice
                status = fileToBeCreated.createNewFile();
                System.out.println("Il file " + fileToBeCreated + " è stato creato");
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                System.out.println("Non è stato possibile creare il file " + fileToBeCreated);
            }// fine del blocco try-catch
        }// end of if/else cycle

        return status;
    }// end of method


    /**
     * Crea una directory
     *
     * @param nomeCompletoDirectory nome completo della directory
     */
    public boolean creaDirectory(String nomeCompletoDirectory) {
        boolean creata = false;

        creata = new File(nomeCompletoDirectory).mkdirs();
        if (creata) {
            System.out.println("La directory " + nomeCompletoDirectory + " è stata creata");
        } else {
            System.out.println("La directory " + nomeCompletoDirectory + " esisteva già, oppure non è stato possibile crearla");
        }// end of if/else cycle

        return creata;
    }// end of method

    /**
     * Copia una directory
     *
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     */
    public boolean copyDirectory(String srcPath, String destPath) {
        boolean copiata = false;
        File srcDir = new File(srcPath);
        File destDir = new File(destPath);

        try { // prova ad eseguire il codice
            FileUtils.copyDirectory(srcDir, destDir);
            copiata = true;
        } catch (IOException e) {
            e.printStackTrace();
        }// fine del blocco try-catch

        return copiata;
    }// end of method

    /**
     * Copia un file
     *
     * @param srcPath  nome completo del file sorgente
     * @param destPath nome completo del file destinazione
     */
    public boolean copyFile(String srcPath, String destPath) {
        boolean copiato = false;
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);

        try { // prova ad eseguire il codice
            FileUtils.copyFile(srcFile, destFile);
            copiato = true;
        } catch (IOException e) {
            e.printStackTrace();
        }// fine del blocco try-catch

        return copiato;
    }// end of method


    /**
     * Scrive un file
     * Se non esiste, lo crea
     *
     * @param pathFileToBeWritten nome completo del file
     * @param text                contenuto del file
     */
    public boolean scriveFile(String pathFileToBeWritten, String text) {
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
     * Legge il contenuto di una directory
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public ArrayList<String> getSubdirectories(String pathDirectoryToBeScanned) {
        ArrayList<String> subDirectory = null;
        File[] allFiles = null;
        File directory = new File(pathDirectoryToBeScanned);

        if (directory != null) {
            allFiles = directory.listFiles();
        }// end of if cycle

        if (allFiles != null) {
            subDirectory = new ArrayList<>();
            for (File file : allFiles) {
                if (file.isDirectory()) {
                    subDirectory.add(file.getName());
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return subDirectory;
    }// end of method

}// end of class
