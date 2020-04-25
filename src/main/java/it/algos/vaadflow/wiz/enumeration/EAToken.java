package it.algos.vaadflow.wiz.enumeration;


import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 06-mar-2018
 * Time: 19:46
 */
public enum EAToken {

    nameTargetProject("PROJECT", true),
    pathTargetProjet(VUOTA, true),
    projectNameUpper("PROJECTALLUPPER", true),
    moduleNameMinuscolo("MODULELOWER", true),
    moduleNameMaiuscolo("MODULEUPPER", true),
    first("FIRSTCHARPROJECT", true),
    pathVaadFlowWizTxtSources(VUOTA, true),
    packageName("PACKAGE"),
    projectCost("COST"),
    user("USER"),
    today("TODAY"),
    qualifier("QUALIFIER"),
    tagView("TAG_VIEW"),
    entity("ENTITY"),
    estendeEntity("ESTENDEENTITY"),
    superClassEntity("SUPERCLASSENTITY"),
    parametersFind("PARAMETERSFIND"),
    parameters("PARAMETERS"),
    parametersDoc("PARAMETERSDOC"),
    parametersNewEntity("PARAMETERSNEWENTITY"),
    methodFind("FIND"),
    methodNewOrdine("NEWORDINE"),
    methodIdKeySpecifica("IDKEYSPECIFICA"),
    keyUnica("KEYUNICA"),
    builder("BUILDER"),
    query("QUERY"),
    findAll("FINDALL"),
    properties("PROPERTIES"),
    propertyOrdine("ORDINE"),
    propertyCode("CODE"),
    propertyDescrizione("DESCRIZIONE"),
    toString("TOSTRING"),
    usaCompany("USACOMPANY"),
    usaSecurity("USASECURITY"),
    readCompany("READCOMPANY"),
    grid("GRID"),
    creaGrid("CREAGRID"),
    postConstruct("POSTCONSTRUCT"),
    setParameter("SETPARAMETER"),
    beforeEnter("BEFOREENTER"),
    fixPreferenze("FIXPREFERENZE"),
    fixLayout("FIXLAYOUT"),
    creaAlertLayout("CREAALERTLAYOUT"),
    creaTopLayout("CREATOPLAYOUT"),
    creaPopupFiltro("CREAPOPUPFILTRO"),
    creaFiltri("CREAFILTRI"),
    updateFiltri("UPDATEFILTRI"),
    addListeners("ADDLISTENERS"),
    ;

    private static String DELIMITER = "@";

    private String tokenTag;

    private boolean usaValue;

    private String value;


    EAToken(String tokenTag) {
        this(tokenTag, false);
    }// fine del costruttore


    EAToken(String tokenTag, boolean usaValue) {
        this(tokenTag, usaValue, VUOTA);
    }// fine del costruttore


    EAToken(String tokenTag, boolean usaValue, String value) {
        this.setTokenTag(tokenTag);
        this.setUsaValue(usaValue);
        this.setValue(value);
    }// fine del costruttore


    public static void reset() {
        for (EAToken EAToken : EAToken.values()) {
            EAToken.setValue(VUOTA);
        }// end of for cycle
    }// end of method


    public static String replace(EAToken EAToken, String textReplacing, String value) {
        return textReplacing.replaceAll(DELIMITER + EAToken.tokenTag + DELIMITER, value);
    }// end of static method


    public static String replaceAll(String textReplacing, Map<EAToken, String> valueMap) {

        for (EAToken EAToken : values()) {
            if (valueMap.containsKey(EAToken)) {
                textReplacing = replace(EAToken, textReplacing, valueMap.get(EAToken));
//                textReplacing = textReplacing.replaceAll(DELIMITER + token.tokenTag + DELIMITER, valueMap.get(token));
            }// end of if cycle
        }// end of for cycle

        return textReplacing;
    }// end of static method


    public String replace(String textReplacing, String currentTag) {
        return textReplacing.replaceAll(tokenTag, currentTag);
    }// end of method


    public String getTokenTag() {
        return tokenTag;
    }// end of method


    public void setTokenTag(String tokenTag) {
        this.tokenTag = tokenTag;
    }// end of method


    public boolean isUsaValue() {
        return usaValue;
    }// end of method


    public void setUsaValue(boolean usaValue) {
        this.usaValue = usaValue;
    }// end of method


    public String getValue() {
        return value;
    }// end of method


    public void setValue(String value) {
        this.value = value;
    }// end of method

}// end of enumeration class
