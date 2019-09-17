package it.algos.vaadflow.ui.dialog;

import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 05-mag-2018
 * Time: 18:56
 */
public interface IADialog<T> {


    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     */
    public void open(AEntity item, EAOperation operation, AContext context);


    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     * @param title     of the window dialog
     */
    public void open(AEntity item, EAOperation operation, AContext context, String title);

    /**
     * Close.
     *
     */
    public void close();

}// end of interface
