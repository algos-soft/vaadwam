package it.algos.vaadwam.topbar;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.ui.topbar.TopbarComponent;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.wam.WamLogin;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 15-mag-2020
 * Time: 15:18
 */
public class WamTopbar extends TopbarComponent {

    /**
     * Costruttore base con i parametri obbligatori <br>
     *
     * @param titolo della company/applicazione (obbligatorio)
     */
    public WamTopbar(String titolo, String sottotitolo) {
        super(titolo, sottotitolo);
    }// end of constructor


    /**
     * Costruttore con alcuni parametri <br>
     *
     * @param pathImage dell'immagine (facoltativo)
     * @param titolo    della company/applicazione (obbligatorio)
     */
    public WamTopbar(String pathImage, String titolo, String sottotitolo) {
        super(pathImage, titolo, sottotitolo);
    }// end of constructor


    /**
     * Costruttore completo con tutti i parametri <br>
     *
     * @param pathImage dell'immagine (facoltativo)
     * @param titolo    della company/applicazione (obbligatorio)
     * @param nickName  utente loggato se multiCompany (facoltativo)
     */
    public WamTopbar(ALogin login, String pathImage, String titolo, String sottotitolo, String nickName) {
        super(login, pathImage, titolo, sottotitolo, nickName);
    }// end of constructor


    /**
     * Preferenze <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        if (login != null && login.isDeveloper()) {
            super.usaProfile = false;
        }
    }


    protected Icon getIcon() {
        Icon icon = new Icon(VaadinIcon.USER);
        WamLogin wamLogin;
        Milite milite;

        if (login != null) {
            if (login.isDeveloper()) {
                icon = new Icon(VaadinIcon.MAGIC);
            }
            if (login.isAdmin() && login instanceof WamLogin) {
                wamLogin = (WamLogin) login;
                milite = wamLogin.getMilite();
                if (milite != null) {
                    if (milite.managerTabellone) {
                        icon = new Icon(VaadinIcon.SPECIALIST);
                    } else {
                        icon = new Icon(VaadinIcon.USER);
                    }
                }
            }
        }

        return icon;
    }

}
