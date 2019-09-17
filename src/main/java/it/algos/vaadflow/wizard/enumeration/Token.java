package it.algos.vaadflow.wizard.enumeration;


import java.util.Map;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 06-mar-2018
 * Time: 19:46
 */
public enum Token {

    projectName("PROJECT"),
    moduleNameMinuscolo("MODULELOWER"),
    moduleNameMaiuscolo("MODULEUPPER"),
    first("FIRSTCHARPROJECT"),
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
    readCompany("READCOMPANY"),
    ;

    private String tokenTag;

    private static String DELIMITER = "@";

    Token(String tokenTag) {
        this.setTokenTag(tokenTag);
    }// fine del costruttore

    public String replace(String textReplacing, String currentTag) {
        return textReplacing.replaceAll(tokenTag, currentTag);
    }// end of method

    public String getTokenTag() {
        return tokenTag;
    }// end of method

    public void setTokenTag(String tokenTag) {
        this.tokenTag = tokenTag;
    }// end of method


    public static String replace(Token token, String textReplacing, String value) {
        return textReplacing.replaceAll(DELIMITER + token.tokenTag + DELIMITER, value);
    }// end of static method


    public static String replaceAll(String textReplacing, Map<Token, String> valueMap) {

        for (Token token : values()) {
            if (valueMap.containsKey(token)) {
                textReplacing = replace(token, textReplacing, valueMap.get(token));
//                textReplacing = textReplacing.replaceAll(DELIMITER + token.tokenTag + DELIMITER, valueMap.get(token));
            }// end of if cycle
        }// end of for cycle

        return textReplacing;
    }// end of static method

}// end of enumeration class
