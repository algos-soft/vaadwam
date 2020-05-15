package it.algos.vaadflow.ui.topbar;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.service.AMenuService;

/**
 * Componente che mostra il nome della company e l'utente loggato <br>
 * <p>
 * Tre oggetti da sinistra a destra (occupano tutto lo spazio disponibile, col componente centrale che si espande):
 * 1 - immagine (opzionale); se manca sostituisce un'immagine di default <br>
 * 2 - descrizione della company/applicazione (obbligatorio);
 * se multiCompany una sigla o una descrizione della company,
 * altrimenti una sigla o una descrizione dell'applicazione stessa
 * 3 - utente loggato (opzionale); se multiCompany il nickname dell'utente loggato
 */
public class TopbarComponent extends HorizontalLayout {

    private static String DEFAULT_IMAGE = "frontend/images/medal.ico";

    //    private Image image;

    //    private Label label;

    public MenuBar menuUser;

    public SubMenu projectSubMenu;

    protected ALogin login;

    private MenuItem itemUser;

    private LogoutListener logoutListener;

    private ProfileListener profileListener;

    //--property
    private String titolo;

    //--property
    private String sottotitolo;

    //--property
    private String pathImage;

    //--property
    private String nickName;

    private AMenuService menuService;


    /**
     * Costruttore base con i parametri obbligatori <br>
     *
     * @param titolo della company/applicazione (obbligatorio)
     */
    public TopbarComponent(String titolo, String sottotitolo) {
        this(null, "", titolo, sottotitolo, "");
    }// end of constructor


    /**
     * Costruttore con alcuni parametri <br>
     *
     * @param pathImage dell'immagine (facoltativo)
     * @param titolo    della company/applicazione (obbligatorio)
     */
    public TopbarComponent(String pathImage, String titolo, String sottotitolo) {
        this(null, pathImage, titolo, sottotitolo, "");
    }// end of constructor


    /**
     * Costruttore completo con tutti i parametri <br>
     *
     * @param pathImage dell'immagine (facoltativo)
     * @param titolo    della company/applicazione (obbligatorio)
     * @param nickName  utente loggato se multiCompany (facoltativo)
     */
    public TopbarComponent(ALogin login, String pathImage, String titolo, String sottotitolo, String nickName) {
        this.login = login;
        this.pathImage = pathImage;
        this.titolo = titolo;
        this.sottotitolo = sottotitolo;
        this.nickName = nickName;

        this.initView();
    }// end of constructor


    /**
     * Creazione dei componenti grafici <br>
     */
    protected void initView() {
        Tab tab;
        setWidth("100%");
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Icon icon = new Icon(VaadinIcon.USER);


        //--immagine eventuale
        Image image;
        if (pathImage != null && !pathImage.isEmpty()) {
            image = new Image(pathImage, "Algos");
        } else {
            image = new Image(DEFAULT_IMAGE, "Algos");
        }
        image.setHeight("9mm");


        //--titolo su 2 righe
        Div divTitolo = new Div();
        divTitolo.getElement().setAttribute("style", "display:flex; flex-direction:column; min-width:2em");

        // (#1676F3 is --lumo-primary-text-color)
        String commonStyle = "line-height:120%; white-space:nowrap; overflow:hidden; color:#1676F3";

        Label label1 = new Label(titolo);
        label1.getElement().setAttribute("style", commonStyle);
        label1.getStyle().set("font-size", "120%");
        label1.getStyle().set("font-weight", "bold");

        Label label2 = new Label(sottotitolo);
        label2.getElement().setAttribute("style", commonStyle);
        label2.getStyle().set("font-size", "70%");

        divTitolo.add(label1);
        divTitolo.add(label2);


        //--menu utente eventuale
        if (nickName != null && !nickName.isEmpty()) {

            menuUser = new MenuBar();
            menuUser.setOpenOnHover(true);
            itemUser = menuUser.addItem(nickName);

            if (login != null) {
                if (login.isDeveloper()) {
                    icon = new Icon(VaadinIcon.MAGIC);
                }
                if (login.isAdmin()) {
                    icon = new Icon(VaadinIcon.SPECIALIST);
                }
            }
            itemUser.addComponentAsFirst(icon);

            projectSubMenu = itemUser.getSubMenu();
            tab = new Tab();
            tab.add(VaadinIcon.EDIT.create(), new Label("Profilo"));
            MenuItem profile = projectSubMenu.addItem(tab, menuItemClickEvent -> {
                if (profileListener != null) {
                    profileListener.profile();
                }
            });

            menuService = StaticContextAccessor.getBean(AMenuService.class);
            tab = menuService.creaMenuLogout();
            MenuItem logout = projectSubMenu.addItem(tab, menuItemClickEvent -> {
                if (logoutListener != null) {
                    logoutListener.logout();
                }
            });
        }


        if (menuUser != null) {
            Div elasticSpacer = new Div();
            elasticSpacer.getStyle().set("flex-grow", "1");
            this.add(image, divTitolo, elasticSpacer, menuUser);
        } else {
            this.add(image, divTitolo);
        }

        //--giustifica a sinistra ed a destra
        //setFlexGrow(0, image);
        //setFlexGrow(1, label);
        //if (menuUser != null) {
        //    setFlexGrow(0, menuUser);
        //}

    }


    //    public void setImage(Image image) {
    //        this.image = image;
    //    }


    //    public void setLabel(String text) {
    //        label.setText(text);
    //    }


    //    public void setUsername(String username) {
    //        itemUser.setText(username);
    //    }


    public void setLogoutListener(LogoutListener listener) {
        this.logoutListener = listener;
    }


    public void setProfileListener(ProfileListener listener) {
        this.profileListener = listener;
    }


    public interface LogoutListener {

        void logout();

    }


    public interface ProfileListener {

        void profile();

    }

}// end of class
