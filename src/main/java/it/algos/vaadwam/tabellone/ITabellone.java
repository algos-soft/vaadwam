package it.algos.vaadwam.tabellone;

import com.vaadin.flow.component.dialog.Dialog;
import it.algos.vaadwam.broadcast.BroadcastMsg;
import it.algos.vaadwam.broadcast.Broadcaster;
import it.algos.vaadwam.modules.iscrizione.Iscrizione;
import it.algos.vaadwam.modules.servizio.Servizio;
import it.algos.vaadwam.modules.turno.Turno;

import java.time.LocalDate;

public interface ITabellone {

    void cellClicked(Turno turno, LocalDate giorno, Servizio servizio, String codFunzione);

    void annullaDialogoTurno(Dialog dialogo);

    void confermaDialogoTurno(Dialog dialogo, Turno turno, Turno oldTurno);

    void confermaDialogoIscrizione(Dialog dialog, Turno turno, Iscrizione iscrizione, boolean ripeti, int numSettimane);

    void eliminaTurno(Dialog dialog, Turno turno);
}
