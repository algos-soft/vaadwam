package it.algos.vaadflow.ui.topbar;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import it.algos.vaadflow.application.StaticContextAccessor;
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

    private Image image;

    private Label label;

    private MenuItem itemUser;

    public MenuBar menuUser;

    private LogoutListener logoutListener;

    private ProfileListener profileListener;

    //--property
    private String descrizione;

    //--property
    private String pathImage;

    //--property
    private String nickName;

    private AMenuService menuService;

    public SubMenu projectSubMenu;

    /**
     * Costruttore base con i parametri obbligatori <br>
     *
     * @param descrizione della company/applicazione (obbligatorio)
     */
    public TopbarComponent(String descrizione) {
        this("", descrizione, "");
    }// end of constructor


    /**
     * Costruttore con alcuni parametri <br>
     *
     * @param pathImage   dell'immagine (facoltativo)
     * @param descrizione della company/applicazione (obbligatorio)
     */
    public TopbarComponent(String pathImage, String descrizione) {
        this(pathImage, descrizione, "");
    }// end of constructor


    /**
     * Costruttore completo con tutti i parametri <br>
     *
     * @param pathImage   dell'immagine (facoltativo)
     * @param descrizione della company/applicazione (obbligatorio)
     * @param nickName    utente loggato se multiCompany (facoltativo)
     */
    public TopbarComponent(String pathImage, String descrizione, String nickName) {
        this.pathImage = pathImage;
        this.descrizione = descrizione;
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


        //--immagine eventuale
        if (pathImage != null && !pathImage.isEmpty()) {
            image = new Image(pathImage, "Algos");
        } else {
            image = new Image(DEFAULT_IMAGE, "Algos");
        }
        image.setHeight("9mm");


        //--descrizione oggligatoria
        label = new Label(descrizione);
        label.getStyle().set("font-size", "large");
        label.getStyle().set("font-weight", "bold");
        //label.getStyle().set("color", "blue");



        //--menu utente eventuale
        if (nickName != null && !nickName.isEmpty()) {

            menuUser = new MenuBar();
            menuUser.setOpenOnHover(true);
            itemUser = menuUser.addItem(nickName);

            projectSubMenu = itemUser.getSubMenu();
            tab = new Tab();
            tab.add(VaadinIcon.EDIT.create(),new Label("Profilo"));
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
            elasticSpacer.getStyle().set("flex-grow","1");
            this.add(image, label, elasticSpacer, menuUser);
        } else {
            this.add(image, label);
        }

        //--giustifica a sinistra ed a destra
        //setFlexGrow(0, image);
        //setFlexGrow(1, label);
        //if (menuUser != null) {
        //    setFlexGrow(0, menuUser);
        //}

    }


    public void setImage(Image image) {
        this.image = image;
    }


    public void setLabel(String text) {
        label.setText(text);
    }


    public void setUsername(String username) {
        itemUser.setText(username);
    }


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
