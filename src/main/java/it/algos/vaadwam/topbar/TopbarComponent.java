package it.algos.vaadwam.topbar;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Componente che mostra il nome della company e l'utente loggato
 */
public class TopbarComponent extends HorizontalLayout {

    Image image;

    private Label label;

    private MenuItem itemUser;

    private LogoutListener logoutListener;

    private ProfileListener profileListener;


    public TopbarComponent() {

        setWidth("100%");
//        getStyle().set("border", "1px solid #9E9E9E");

        setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        label = new Label();

        MenuBar menuUser = new MenuBar();
        menuUser.setOpenOnHover(true);

        itemUser = menuUser.addItem("");

        SubMenu projectSubMenu = itemUser.getSubMenu();
        MenuItem profile = projectSubMenu.addItem("Profilo", menuItemClickEvent -> {
            if (profileListener != null) {
                profileListener.profile();
            }
        });
        MenuItem logout = projectSubMenu.addItem("Logout", menuItemClickEvent -> {
            if (logoutListener != null) {
                logoutListener.logout();
            }
        });

        // immagine di default
        image = new Image("frontend/images/wam.png", "wam");
        image.setHeight("9mm");

        add(image, label, menuUser);

        setFlexGrow(0, image);
        setFlexGrow(1, label);
        setFlexGrow(0, menuUser);

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


}
