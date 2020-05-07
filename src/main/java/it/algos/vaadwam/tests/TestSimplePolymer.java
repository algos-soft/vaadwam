package it.algos.vaadwam.tests;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * Simple Polymer test
 */
@Route(value = "simplepolymer", layout = AppLayout.class)
//@ParentLayout(AppLayout.class)
@Tag("simple-polymer")
@HtmlImport("src/views/prova/simple-polymer.html")

//@UIScope
//@Qualifier("simplepolymer")
//@AIEntity(company = EACompanyRequired.obbligatoria)
//@AIScript(sovrascrivibile = false)
//@AIView(vaadflow = false, menuName = "tabellone", menuIcon = VaadinIcon.CALENDAR, roleTypeVisibility = EARoleType.user)
//@VaadinSessionScope

public class TestSimplePolymer extends PolymerTemplate<TemplateModel> {

    public TestSimplePolymer() {
    }

}
