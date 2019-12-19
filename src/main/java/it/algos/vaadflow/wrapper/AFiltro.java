package it.algos.vaadflow.wrapper;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 19-dic-2019
 * Time: 11:01
 */
public class AFiltro {

    private CriteriaDefinition criteria;

    private Sort sort;


    public AFiltro(CriteriaDefinition criteria) {
        this.criteria = criteria;
        this.sort = new Sort(Sort.Direction.ASC, criteria.getKey());
    }// end of constructor


    public AFiltro(CriteriaDefinition criteria, Sort sort) {
        this.criteria = criteria;
        this.sort = sort;
    }// end of constructor


    public CriteriaDefinition getCriteria() {
        return criteria;
    }// end of method


    public Sort getSort() {
        return sort;
    }// end of method

}// end of class
