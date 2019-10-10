package it.algos.vaadflow.modules.preferenza;

import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 09-ott-2019
 * Time: 23:18
 */
public interface IAPreferenza {
    public String getCode() ;
    public String getDesc() ;
    public EAPrefType getType() ;
    public Object getValue() ;
}// end of interface
