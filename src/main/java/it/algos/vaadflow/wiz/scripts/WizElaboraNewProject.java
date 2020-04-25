package it.algos.vaadflow.wiz.scripts;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.wiz.enumeration.EAToken;
import it.algos.vaadflow.wiz.enumeration.EAWiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadflow.wiz.scripts.WizCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 13-apr-2020
 * Time: 05:31
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class WizElaboraNewProject extends WizElabora {


    @Override
    public void esegue() {
        super.isNuovoProgetto = true;
        super.esegue();

        super.copiaDirectoryDocumentation();
        super.copiaDirectoryLinks();
        super.copiaDirectorySnippets();

        this.copiaCartellaVaadFlow();
        this.creaModuloNuovoProgetto();

        //--banner deve essere chiamato PRIMA di copiaMetaInf
        super.scriveFileBanner();

        super.copiaMetaInf();
        super.scriveFileProperties();

        super.scriveFileRead();
        super.copiaFileGit();
        super.scriveFilePom();
    }// end of method


    public void creaModuloNuovoProgetto() {
        if (EAWiz.flagProject.isAbilitato()) {

            //--creaDirectoryProjectModulo
            file.creaDirectory(pathProjectModulo);

            //--classe principale dell'applicazione col metodo 'main'
            creaFileApplicationMainClass();

            //--creaDirectoryApplication (empty)
            file.creaDirectory(pathProjectDirApplication);

            //--creaDirectoryModules (empty)
            file.creaDirectory(pathProjectDirModules);

            //--crea contenuto della directory Application
            scriveFileCost();
            scriveFileBoot();
//        scriveFileVers();
            scriveFileHome();

            creaDirectorySecurity();
        }// end of if cycle
    }// end of method


    public void creaFileApplicationMainClass() {
        String mainApp = newProjectName + text.primaMaiuscola(APP_NAME);
        mainApp = text.primaMaiuscola(mainApp);
        String destPath = pathProjectModulo + mainApp + JAVA_SUFFIX;
        String testoApp = leggeFile(APP_NAME + TXT_SUFFIX);

        testoApp = EAToken.replace(EAToken.moduleNameMinuscolo, testoApp, newProjectName);
        testoApp = EAToken.replace(EAToken.moduleNameMaiuscolo, testoApp, text.primaMaiuscola(newProjectName));

        if (EAWiz.flagSecurity.isAbilitato()) {
            testoApp = EAToken.replace(EAToken.usaSecurity, testoApp, VUOTA);
        } else {
            testoApp = EAToken.replace(EAToken.usaSecurity, testoApp, ", exclude = {SecurityAutoConfiguration.class}");
        }// end of if/else cycle

        file.scriveFile(destPath, testoApp, true);
    }// end of method


    protected void scriveFileCost() {
        wizService.scriveNewFileCreatoDaWizSource(FILE_COST, pathProjectDirApplication);
    }// end of method


    public void scriveFileBoot() {
        wizService.scriveNewFileCreatoDaWizSource(FILE_BOOT, pathProjectDirApplication);
    }// end of method


    public void scriveFileVers() {
        wizService.scriveNewFileCreatoDaWizSource(FILE_VERS, pathProjectDirApplication);
    }// end of method


    public void scriveFileHome() {
        wizService.scriveNewFileCreatoDaWizSource(FILE_HOME, pathProjectDirApplication);
    }// end of method


    public void creaDirectorySecurity() {
    }// end of method

}// end of class
