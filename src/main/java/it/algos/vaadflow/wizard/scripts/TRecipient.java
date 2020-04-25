package it.algos.vaadflow.wizard.scripts;

import it.algos.vaadflow.wiz.enumeration.Chiave;

import java.util.Map;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mer, 07-mar-2018
 * Time: 08:07
 */
public interface TRecipient {
    public void gotInput(Map<Chiave, Object> mappaInput);
}// end of interface
