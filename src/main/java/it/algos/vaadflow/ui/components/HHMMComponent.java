package it.algos.vaadflow.ui.components;


import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import it.algos.vaadflow.ui.fields.AIntegerField;

/**
 * Editor di ore e minuti
 */
public class HHMMComponent extends HorizontalLayout {

    private AIntegerField fHours;

    private AIntegerField fMinutes;


    public HHMMComponent(String caption, int h, int m) {
        setSpacing(true);

        fHours = new AIntegerField();
        fHours.setWidth("2.5em");
        setHours(h);

        fMinutes = new AIntegerField();
        fMinutes.setWidth("2.5em");
        setMinutes(m);

        add(fHours);
        Label label = new Label(":");
        add(label);
        add(fMinutes);

//        setComponentAlignment(label, Alignment.MIDDLE_CENTER);
//
//        setCaption(caption);

    }// end of method


    public HHMMComponent(String caption) {
        this(caption, 0, 0);
    }// end of method


    public HHMMComponent(int h, int m) {
        this(null, h, m);
    }// end of method


    public HHMMComponent() {
        this(null, 0, 0);
    }// end of method


    public int getHours() {
        return new Integer(fHours.getValue());
    }// end of method


    public void setHours(int hours) {
        fHours.setValue(hours + "");
    }// end of method


    public int getMinutes() {
        return new Integer(fMinutes.getValue());
    }// end of method


    public void setMinutes(int minutes) {
        fMinutes.setValue(minutes + "");
    }// end of method


    public void setHoursMinutes(int minutes) {
        int hours = (int) (minutes / 60);
        int mins = minutes % 60;
        setHours(hours);
        setMinutes(mins);
    }


    public int getTotalMinutes() {
        return (getHours() * 60) + getMinutes();
    }// end of method


}// end of class
