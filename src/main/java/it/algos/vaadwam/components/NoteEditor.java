package it.algos.vaadwam.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextArea;
import org.apache.commons.lang3.StringUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Componnte visualizzatore/editor per un campo di tipo Note.
 * <p>
 * Se il testo è vuoto mostra un bottone altrimenti mostra il testo.<br>
 * Il testo si setta e si recupera con setText() e getText().<br>
 * Premendo sul testo o sul bottone si apre un dialogo per editare il testo.<br>
 * Se il testo viene modificato invoca il listener NoteChangedListener.<br>
 * Usa lo stile "noteEditor" che viene cercato nella pagina html che ospita il componente.
 * Se il componente è disabilitato (setEnabled(false)) non mostra il bottone note
 */
public class NoteEditor extends Div {

    private Button noteButton;
    private Label noteLabel;
    private List<NoteChangedListener> noteChangedListeners=new ArrayList<>();

    public NoteEditor() {
        setClassName("noteEditor");

        this.noteButton = new Button("note...");
        noteButton.setWidthFull();
        this.noteLabel = new Label();

        noteLabel.getElement().addEventListener("click", e -> edit());

        noteButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> edit());

    }

    /**
     * Mostra un dialogo di editing delle note
     */
    private void edit() {

        final ConfirmDialog dialog = ConfirmDialog.create();

        TextArea textArea = new TextArea();
        textArea.setValue(getNote());

        Button bConferma = new Button();
        bConferma.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> setNote(textArea.getValue()));

        dialog.withMessage(textArea).withButton(new Button(), ButtonOption.caption("Annulla")).withButton(bConferma, ButtonOption.caption("Conferma"));
        dialog.open();

    }

    public String getNote() {
        return noteLabel.getText();
    }

    public void setNote(String text) {
        String oldText=getNote();
        noteLabel.setText(text);

        for(NoteChangedListener listener : noteChangedListeners){
            listener.onNoteChanged(text, oldText);
        }

        sync();

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        sync();
    }


    private void sync() {
        removeAll();
        if (StringUtils.isEmpty(getNote())) {
            if(isEnabled()){
                noteButton.setEnabled(true);
                add(noteButton);
            }
        } else {
            add(noteLabel);
        }
    }

    public void addNoteChangedListener(NoteChangedListener listener){
        noteChangedListeners.add(listener);
    }

    public interface NoteChangedListener {
        void onNoteChanged(String newText, String oldText);
    }


}
