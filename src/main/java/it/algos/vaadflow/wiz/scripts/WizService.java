package it.algos.vaadflow.wiz.scripts;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AFileService;
import it.algos.vaadflow.wiz.enumeration.EAToken;
import it.algos.vaadflow.wiz.enumeration.EAWiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.wiz.scripts.WizCost.JAVA_SUFFIX;
import static it.algos.vaadflow.wiz.scripts.WizCost.TXT_SUFFIX;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 20-apr-2020
 * Time: 14:39
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class WizService {


    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con @Autowired <br>
     * Disponibile al termine del costruttore di questa classe <br>
     */
    @Autowired
    protected AFileService file;


    /**
     * Crea un nuovo file java <br>
     * <p>
     * Costruisce il testo prendendolo dalla directory wiz.sources del VaadFlow <br>
     * Elabora il testo sostituendo i 'tag' coi valori attuali <br>
     * Scrive il file col path della directory designata <br>
     * Scrive il file col nome finale uguale al nome sorgente col prefisso moduleNameMaiuscolo <br>
     * Scrive il file col suffisso standard .java <br>
     *
     * @param nomeFileSrc nome del file presente in wiz.sources
     * @param destPath    directory in cui costruire il file
     */
    public void scriveNewFileCreatoDaWizSource(String nomeFileSrc, String destPath) {
        scriveNewFileCreatoDaWizSource(nomeFileSrc, destPath, EAToken.moduleNameMaiuscolo.getValue() + nomeFileSrc, JAVA_SUFFIX);
    }// end of method


    /**
     * Crea un nuovo file <br>
     * <p>
     * Costruisce il testo prendendolo dalla directory wiz.sources del VaadFlow <br>
     * Elabora il testo sostituendo i 'tag' coi valori attuali <br>
     * Scrive il file col path della directory designata <br>
     * Scrive il file col nome finale (potrebbe esser diverso dal nome del file presente in wiz.sources) <br>
     * Scrive il file col suffisso indicato <br>
     *
     * @param nomeFileSrc nome del file presente in wiz.sources
     * @param destPath    directory in cui costruire il file
     * @param suffix      nome del file destinazione che potrebbe essere diverso da .java o .text
     */
    public void scriveNewFileCreatoDaWizSource(String nomeFileSrc, String destPath, String suffix) {
        scriveNewFileCreatoDaWizSource(nomeFileSrc, destPath, nomeFileSrc, suffix);
    }// end of method


    /**
     * Crea un nuovo file <br>
     * <p>
     * Costruisce il testo prendendolo dalla directory wiz.sources del VaadFlow <br>
     * Elabora il testo sostituendo i 'tag' coi valori attuali <br>
     * Scrive il file col path della directory designata <br>
     * Scrive il file col nome finale (potrebbe esser diverso dal nome del file presente in wiz.sources) <br>
     * Scrive il file col suffisso indicato <br>
     *
     * @param nomeFileSrc  nome del file presente in wiz.sources
     * @param destPath     directory in cui costruire il file
     * @param nomeFileDest nome del file destinazione che potrebbe essere diverso da nomeFileSrc
     * @param suffix       nome del file destinazione che potrebbe essere diverso da .java o .text
     */
    public void scriveNewFileCreatoDaWizSource(String nomeFileSrc, String destPath, String nomeFileDest, String suffix) {
        String sourceText = leggeFile(nomeFileSrc);
        sourceText = elaboraFileCreatoDaWizSource(sourceText);

        String pathFileToBeWritten = destPath + nomeFileDest + suffix;
        file.scriveFile(pathFileToBeWritten, sourceText, true);
    }// end of method


    public String leggeFile(String nomeFileTextSorgente) {
        String nomeFileTxt = nomeFileTextSorgente;

        if (!nomeFileTxt.endsWith(TXT_SUFFIX)) {
            nomeFileTxt += TXT_SUFFIX;
        }// end of if cycle

        return file.leggeFile(EAToken.pathVaadFlowWizTxtSources.getValue() + nomeFileTxt);
    }// end of method


    /**
     * Elabora un nuovo file <br>
     * <p>
     * Elabora il testo sostituendo i 'tag' coi valori attuali <br>
     *
     * @param testoGrezzoDaElaborare proveniente da wiz.sources
     *
     * @return testo elaborato
     */
    protected String elaboraFileCreatoDaWizSource(String testoGrezzoDaElaborare) {
        String testoFinaleElaborato = testoGrezzoDaElaborare;

        testoFinaleElaborato = EAToken.replace(EAToken.projectNameUpper, testoFinaleElaborato, EAToken.projectNameUpper.getValue());
        testoFinaleElaborato = EAToken.replace(EAToken.moduleNameMinuscolo, testoFinaleElaborato, EAToken.moduleNameMinuscolo.getValue());
        testoFinaleElaborato = EAToken.replace(EAToken.moduleNameMaiuscolo, testoFinaleElaborato, EAToken.moduleNameMaiuscolo.getValue());

        return testoFinaleElaborato;
    }// end of method


    /**
     * Sovrascrive o aggiunge a seconda del flag 'flagSovrascriveDirectory' <br>
     */
    public void copyCartellaRootProject(String dirName) {
        String srcPath = EAWiz.pathVaadFlow.getValue() + dirName;
        String destPath = EAWiz.pathTargetProjet.getValue() + dirName;

        if (EAWiz.flagDirectory.isAbilitato()) {
            file.copyDirectoryDeletingAll(srcPath, destPath);
        } else {
            file.copyDirectoryAddingOnly(srcPath, destPath);
        }// end of if/else cycle
    }// end of method


    /**
     * Crea o modifica a seconda del flag 'flagSovrascriveFile' <br>
     */
    public void copyFileRootProject(String fileName) {
        String srcPath = EAWiz.pathVaadFlow.getValue() + fileName;
        String destPath = EAWiz.pathTargetProjet.getValue() + fileName;

        file.copyFileDeletingAll(srcPath, destPath);
    }// end of method

}// end of class
