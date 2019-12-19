package it.algos.vaadflow.modules.preferenza;

import it.algos.vaadflow.enumeration.EAPrefType;
import it.algos.vaadflow.modules.role.EARole;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 09-ott-2019
 * Time: 23:18
 */
public interface IAPreferenza {

    public String getCode();

    public String getDesc();

    public EAPrefType getType();

    public EARole getShow();

    public boolean isCompanySpecifica();

    public Object getValue();

}// end of interface
