package it.algos.vaadwam.tabellone;

import com.vaadin.flow.templatemodel.AllowClientUpdates;
import lombok.Data;

public class TurnoGenFlag {

    private boolean on;

    private int row;
    private int column;

    public TurnoGenFlag() {
    }

    public TurnoGenFlag(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public boolean isOn() {
        return on;
    }

    @AllowClientUpdates
    public void setOn(boolean on) {
        this.on = on;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
