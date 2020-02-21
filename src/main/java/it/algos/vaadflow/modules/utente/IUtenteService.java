package it.algos.vaadflow.modules.utente;

import it.algos.vaadflow.modules.company.Company;
import it.algos.vaadflow.modules.role.EARoleType;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 14-ago-2019
 * Time: 07:13
 */
public interface IUtenteService {

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param userName (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Utente findByKeyUnica(String userName);

    public Company getCompany();

    public boolean isAdmin(Utente utente);

    /**
     * Restituisce il massimo ruolo abilitatao <br>
     * <p>
     * L'ordine è:
     * developer
     * admin
     * user
     * guest
     *
     * @return ruolo massimo abilitato
     */
    public EARoleType getRoleType(Utente utente);

}// end of interface
