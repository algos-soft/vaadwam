package it.algos.vaadflow.enumeration;

import it.algos.vaadwam.application.*;

import java.util.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Thu, 16-May-2019
 * Time: 16:37
 */
public enum EAColor {
    white("White", "#FFFFFF"),
    lightgray("Lightgray", "#D3D3D3"),
    lightslategray("Lightslategray", "#778899"),
    silver("Silver", "#C0C0C0"),
    gray("Gray", "#808080"),
    black("Black", "#000000"),
    red("Red", "#FF0000"),
    green("Green", "#008000"),
    blue("Blue", "#0000FF"),
    lightcyan("Lightcyan", "#e0ffff"),
    lightgreen("Lightgreen", "#90EE90"),
    lightblue("Lightblue", "#ADD8E6"),
    lightskyblue("Lightskyblue", "#87cefa"),
    lightpink("Lightpink", "#FFB6C1"),
    lightsalmon("Lightsalmon", "#ffa07a"),
    maroon("Maroon", "#800000"),
    yellow("Yellow", "#FFFF00"),
    olive("Olive", "#808000"),
    lime("Lime", "#00FF00"),
    aqua("Aqua", "#00FFFF"),
    teal("Teal", "#008080"),
    navy("Navy", "#000080"),
    fuchsia("Fuchsia", "#FF00FF"),
    purple("Purple", "#800080"),
    palegreen("Palegreen", "#98FB98"),
    aquamarine("Aquamarine", "#7fffd4"),
    bisque("Bisque", "#ffe4c4"),
    gainsboro("Gainsboro", "#dcdcdc"),
    pink("Pink", "#ffc0cb"),
    lavender("Lavender", "#e6e6fa"),
    salmon("Salmon", "#fa8072"),
    yellowgreen("Yellowgreen", "#9acd32"),
    wheat("Wheat", "#f5deb3"),
    turquoise("Turquoise", "#40e0d0"),
    grigio1("grigio1", "#eeeeee"),
    grigio2("grigio2", "#dddddd"),
    grigio3("grigio3", "#cccccc"),
    grigio4("grigio4", "#bbbbbb"),
    grigio5("grigio5", "#aaaaaa"),

    // colori tabellone creati con https://coolors.co/
    storicoTabellone("storicoTabellone", "#136f63ff"),
    storicoTabelloneContrasto("storicoTabelloneContrasto", "#fffdf0"),

    criticoTabellone("criticoTabellone", "#c80000ff"),
    criticoTabelloneContrasto("criticoTabelloneContrasto", "#fffdf0"),

    urgenteTabellone("urgenteTabellone", "#ff7700ff"),
    urgenteTabelloneContrasto("urgenteTabelloneContrasto", "#fffdf0"),

    normaleTabellone("normaleTabellone", "#8ea604ff"),
    normaleTabelloneContrasto("normaleTabelloneContrasto", "#fffdf0"),

    previstoTabellone("previstoTabellone", "#2e86abff"),
    previstoTabelloneContrasto("previstoTabelloneContrasto", "#fffdf0"),

    creabileTabellone("creabileTabellone", "#d3d4d9ff"),
    creabileTabelloneContrasto("creabileTabelloneContrasto", "#000000"),

    disponibileTabellone("creabileTabellone", "#439A86"),
    disponibileTabelloneContrasto("creabileTabelloneContrasto", "#fffdf0"),

    // colori per footer app
    footerApp("footerAdmin", WamCost.LUMO_PRIMARY_COLOR),

    ;

    private String tag;

    private String esadecimale;


    EAColor(String tag, String esadecimale) {
        this.tag = tag;
        this.esadecimale = esadecimale;
    }// end of constructor


    public static ArrayList<EAColor> getColors() {
        ArrayList<EAColor> lista = new ArrayList<>();

        for (EAColor color : EAColor.values()) {
            lista.add(color);
        }// end of for cycle

        return lista;
    }// end of static method


    public static EAColor getColor(String tag) {
        EAColor color=null;
        for (EAColor c : EAColor.values()) {
            if(c.tag.equals(tag)){
                color=c;
                break;
            }
        }
        return color;
    }



    public String getTag() {
        return tag;
    }// end of method


    public String getEsadecimale() {
        return esadecimale;
    }
//    ArrayList<String> items = new ArrayList();
//        for (EAColor color : EAColor.values()) {
//        items.add(color.name());
//    }// end of for cycle

}// end of enum class
