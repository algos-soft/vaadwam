package it.algos.vaadwam.tabellone;

import it.algos.vaadwam.modules.milite.Milite;
import lombok.Data;

import java.util.Objects;

/**
 * Una versione ridotta di Milite con i soli dati che interessano al Combo
 */
@Data
public class MiliteComboBean {

    private String idMilite;

    private String siglaMilite;

    public MiliteComboBean(Milite milite) {
        idMilite=milite.getId();
        siglaMilite=milite.getSigla();
    }

    @Override
    public String toString() {
        return siglaMilite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiliteComboBean that = (MiliteComboBean) o;
        return Objects.equals(idMilite, that.idMilite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMilite);
    }
}
