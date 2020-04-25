package it.algos.vaadflow.wiz.scripts;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 19-apr-2020
 * Time: 09:55
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class WizElaboraUpdateProject extends WizElabora {

    @Override
    public void esegue() {
        super.isNuovoProgetto = false;
        super.esegue();

        super.copiaDirectoryDocumentation();
        super.copiaDirectoryLinks();
        super.copiaDirectorySnippets();

        super.copiaCartellaVaadFlow();

        super.copiaMetaInf();
        super.scriveFileProperties();

        super.scriveFileRead();
        super.copiaFileGit();
        super.scriveFilePom();
    }// end of method

}// end of class
