package it.algos.vaadflow.presenter;


import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.IAView;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 07:28
 */
public interface IAPresenter {


    public Class<? extends AEntity> getEntityClazz();

    public IAService getService();

    public IAView getView();
    public void setView(IAView view);


}// end of interface
