package it.algos.vaadwam.tabellonesuperato;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.milite.Milite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: sab, 17-nov-2018
 * Time: 19:59
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class IscrizioneGroupEditor extends VerticalLayout {

    private ArrayList<IscrizioneEditor> iEditors = new ArrayList<>();


    public IscrizioneGroupEditor() {

        setSpacing(true);

        // crea gli editor di iscrizione e li aggiunge
//        for (ServizioFunzione sf : turno.getServizio().getServizioFunzioniOrdine()) {
//
//            Iscrizione i = turno.getIscrizione(sf);
//
//            // se l'iscrizione non esiste la crea ora
//            if (i == null) {
//                i = new Iscrizione(turno, null, sf);
//            }
//
//            IscrizioneEditor ie = new IscrizioneEditor(i, this);
//            iEditors.add(ie);
//            add(ie);
//
//        }
    }


    /**
     * Controlla se un dato volontario è iscritto
     *
     * @param v       il volontario da controllare
     * @param exclude eventuale editor da escludere
     */
    public boolean isIscritto(Milite v, IscrizioneEditor exclude) {
        boolean iscritto = false;
        for (IscrizioneEditor ie : iEditors) {

            boolean skip = false;
            if (exclude != null) {
                if (ie.equals(exclude)) {
                    skip = true;
                }
            }

            if (!skip) {
                Milite iev = ie.getVolontario();
                if (iev != null) {
                    if (iev.equals(v)) {
                        iscritto = true;
                        break;
                    }
                }
            }

        }
        return iscritto;
    }


    /**
     * Ritorna l'elenco delle iscrizioni correntemente presenti
     * (tutte quelle che hanno un volontario)
     *
     * @return l'elenco delle iscrizioni
     */
    public ArrayList<Iscrizione> getIscrizioni() {
        ArrayList<Iscrizione> iscrizioni = new ArrayList();
        for (IscrizioneEditor ie : iEditors) {
            Iscrizione i = ie.getIscrizione();
            if (i != null) {
                iscrizioni.add(i);
            }
        }
        return iscrizioni;
    }


    /**
     * Ritorna l'elenco delle iscrizioni originali
     * (solo quelle con volontario)
     *
     * @return l'elenco delle iscrizioni originali
     */
    public ArrayList<Iscrizione> getIscrizioniOriginali() {
        ArrayList<Iscrizione> iscrizioni = new ArrayList();
        for (IscrizioneEditor ie : iEditors) {
            Iscrizione i = ie.getIscrizioneOriginale();
            if (i != null) {
                if (i.getMilite() != null) {
                    iscrizioni.add(i);
                }
            }
        }
        return iscrizioni;
    }


}// end of class
