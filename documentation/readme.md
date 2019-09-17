Icons:
https://github.com/vaadin/vaadin-icons/blob/master/docs/vaadin-icons-overview.adoc

Crono
http://www.sauronsoftware.it/projects/cron4j/manual.php








11. Creare una classe xxxUI
	- la classe DEVE essere nella stessa directory della @SpringBootApplication che contiene il metodo main iniziale
	- la classe deve avere l’Annotation: @SpringUI()
	- la classe deve extends UI
	- la classe deve sovrascrivere il metodo init(VaadinRequest request) e inserire il contenuto con setContent(new Label("Fatto!"));
	- lanciare la configurazione ed aprire il browser (non si apre da solo, perché il server Tomcat è embedded) al 8080
	- per modificare la porta VM options —> -Dserver.port=8090


12. Controllare le directory e le suddivisioni come desiderate.
	La struttura generata dal pom di Maven ha la sua tipicità
	Vedi: https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html
	Creare una directory (a livello base nella cartella java) ‘’webapp’’ e metterci una directory (maiuscola) VAADIN con dentro i ‘themes’
	ATTENZIONE: Vaadin legge automaticamente come baseDir quella dove c’è la cartella VAADIN. Le immagini ed i dati demo, vanno messi qui.




9. In Project Settings -> Libraries
    - aggiungere New Project Library (tipo java), selezionando ~/Documents/IdeaProjects/webbase/out/artifacts/webbase
    - ATTENZIONE - selezionare l'intera cartella e NON il JAR
    - selezionando la CARTELLA, a destra apparirà il path per i Classes
    - se in Project Setting appare in basso a sinistra la scritte Problems, cliccare su Fix e selezionare Add webbase_jar to the artifact

10. Aprire il plugin Webbase e lanciare (in Ant) lo script templates.script.progetto:
    - nel primo dialogo, inserire (obbligatorio) il nome (Maiuscolo) del nuovo progetto
    - nel secondo dialogo, inserire (facoltativo, default 'test') il nome del database mysql

11. È stato creato il package minimo:
    - creata (sotto src) la directory base del progetto -> it.algos.nomeProgetto
    - creato il file (sotto src) META-INF.persistence.xml: parametri di regolazione del database. Elenco di Entity
    - creato il file ivy.xml per le dependencies (modificabile)
    - creato il file ivysettings.xml per le location dove recuperare le dependencies (modificabile)
    - creata la classe xxxApp: repository di costanti generali del programma
    - creata la classe xxxBootStrap: prima classe in esecuzione del programma.
    - creata la classe xxxServlet: punto di partenza della sessione nel server.
    - creata la classe xxxUI: punto di partenza quando si accede dal browser
    - creata la directory vaadin.themes.algos, con anche le icone (theme non ancora funzionante)
    - sostituito il file web.WEB-INF.web.xml
    - sostituito il file web.index.jsp (che non viene utilizzato)
    - senza necessità di ulteriori interventi, selezionando Run l'applicazione funziona con già installato il modulo ereditato
        dal plugin Webbase: Versione

12. L'applicazione funziona usando il theme 'algos' (standard). L'eventuale selezione si effettua nella classe xxxUI
    - in Project Settings -> Artifatcs selezionare l'artifact web:xxx exploded
    - a destra nel tab Output Layout selezionare l'icona della directory (a sinistra) e crearne una nuova directory
        dal titolo (obbligatorio) VAADIN
    - selezionare la directory appena creata e col tasto destro selezionare Add Copy of -> Directory Content
    - individuare il path della cartella 'vaadin' del progetto appena creato
    - il file vaadin.themes.algos.algos.scss è liberamente modificabile per personalizzare l'applicazopne
    - il file vaadin.themes.algos.algos.scss potrebbe presentare un errore in @import "../valo/valo.scss";
        È un BUG di IDEA che NON influenza la compilazione ed il corretto funzionamento dell'applicazione
    - senza necessità di ulteriori interventi, selezionando Run l'applicazione funziona con installato e funzionante il theme algos

13. (facoltativo) In Project Settings -> Modules
    - cliccare sul simbolo + per creare un nuovo modulo
    - selezionare un framework di tipo iviIDEA
    - nel dialogo che si apre a destra selezionare in alto la posizione del file ivy.xml appena creato
    - cliccare nel box 'use module specific ivy settings' e selezionare la posizione del file ivysettings.xml appena creato

// 13. In Project Settings -> Artifatcs in <output root> deve esserci webbase_war_exploded (come Project Library) per visualizzare le icone
 