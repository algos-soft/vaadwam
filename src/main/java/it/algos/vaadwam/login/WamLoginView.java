package it.algos.vaadwam.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.util.Collections;

import static it.algos.vaadwam.login.WamLoginView.ROUTE;

/**
 * The Login view
 */
@Tag("sa-login-view")
@Route(value = ROUTE)
@PageTitle("Wam Login")
public class WamLoginView extends VerticalLayout implements BeforeEnterObserver {

	// SpringSecurity configuration redirige a questo url
	public static final String ROUTE = "wamlogin";

	//--componente di Vaadin flow invocato dall'Annotation @Tag("sa-login-view")
	private LoginOverlay login = new LoginOverlay();

	@PostConstruct
	protected void postConstruct() {

		login.setAction(ROUTE);

		// personalizza il branding
		login.setTitle(new WamLoginBranding());
		login.setDescription(null);

		// non mostra bottone lost password
		login.setForgotPasswordButtonVisible(false);

		// personalizza i messaggi
		LoginI18n i18n = LoginI18n.createDefault();
		LoginI18n.ErrorMessage errore = new LoginI18n.ErrorMessage();
		errore.setTitle("Riprova");
		errore.setMessage("Username o password non corretti");
		i18n.setErrorMessage(errore);
		login.setI18n(i18n);

		// apre l'overlay
		login.setOpened(true);

	}



	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// inform the user about an authentication error
		// (yes, the API for resolving query parameters is annoying...)
		if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()) {
			login.setError(true);
		}
	}


}
