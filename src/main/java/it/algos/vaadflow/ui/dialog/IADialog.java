package it.algos.vaadflow.ui.dialog;

import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 05-mag-2018
 * Time: 18:56
 */
public interface IADialog<T> {


//    /**
//     * Opens the given item for editing in the dialog.
//     *
//     * @param entityBean The item to edit; it may be an existing or a newly created instance
//     * @param operation  The operation being performed on the item
//     */
//    public void open(AEntity entityBean, EAOperation operation, BiConsumer<T, EAOperation> itemSaver, Consumer<T> itemDeleter);

//    /**
//     * Opens the given item for editing in the dialog.
//     *
//     * @param item      The item to edit; it may be an existing or a newly created instance
//     * @param operation The operation being performed on the item
//     * @param context   legato alla sessione
//     */
//    @Deprecated
//    public void open(AEntity item, EAOperation operation, AContext context);


//    /**
//     * Opens the given item for editing in the dialog.
//     *
//     * @param item      The item to edit; it may be an existing or a newly created instance
//     * @param operation The operation being performed on the item
//     * @param context   legato alla sessione
//     * @param title     of the window dialog
//     */
//    @Deprecated
//    public void open(AEntity item, EAOperation operation, AContext context, String title);

    /**
     * Opens the given item for editing in the dialog.
     * Crea i fields e visualizza il dialogo <br>
     *
     * @param entityBean  The item to edit; it may be an existing or a newly created instance
     * @param operation   The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param itemSaver   funzione associata al bottone 'accetta' ('registra', 'conferma')
     * @param itemDeleter funzione associata al bottone 'delete'
     */
//    public void open(final AEntity entityBean, EAOperation operation, BiConsumer<T, EAOperation> itemSaver, Consumer<T> itemDeleter);

    /**
     * Close.
     */
    public void close();

}// end of interface
