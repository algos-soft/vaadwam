package it.algos.vaadflow.service;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.preferenza.EAPrefType;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.ui.fields.ACheckBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 23-set-2018
 * Time: 21:18
 * <p>
 * Gestisce la creazione delle colonne della Grid nel tipo adeguato <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AColumnService.class); <br>
 * 2) AColumnService.getInstance(); <br>
 * 3) @Autowired private AColumnService columnService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Slf4j
public class AColumnService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Private final property
     */
    private static final AColumnService INSTANCE = new AColumnService();


    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public PreferenzaService pref;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private AColumnService() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AColumnService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Create a single columnService.
     * The columnService type is chosen according to the annotation @AIColumn or, if is not present, a @AIField.
     *
     * @param grid         a cui aggiungere la colonna
     * @param entityClazz  modello-dati specifico
     * @param propertyName della property
     */
    public void create(ApplicationContext appContext, Grid<AEntity> grid, Class<? extends AEntity> entityClazz, String propertyName) {
        pref = StaticContextAccessor.getBean(PreferenzaService.class);
        Grid.Column<AEntity> colonna = null;
        EAFieldType type = annotation.getColumnType(entityClazz, propertyName);
        String headerNotNull = annotation.getColumnNameProperty(entityClazz, propertyName);
        String explicitHader = annotation.getExplicitColumnName(entityClazz, propertyName);
        String width = annotation.getColumnWithEM(entityClazz, propertyName);
        boolean isFlexGrow = annotation.isFlexGrow(entityClazz, propertyName);
        Class linkClazz = annotation.getLinkClass(entityClazz, propertyName);
        Class enumClazz = annotation.getEnumClass(entityClazz, propertyName);
        Class serviceClazz = annotation.getServiceClass(entityClazz, propertyName);
        String colorColumnName = annotation.getColumnColor(entityClazz, propertyName);
        boolean sortable = annotation.isSortable(entityClazz, propertyName);
        this.enumService = AEnumerationService.getInstance();
        VaadinIcon headerIcon = annotation.getHeaderIcon(entityClazz, propertyName);
        String widthHeaderIcon = annotation.getHeaderIconSizePX(entityClazz, propertyName);
        String colorHeaderIcon = annotation.getHeaderIconColor(entityClazz, propertyName);
        String methodName = annotation.getMethodName(entityClazz, propertyName);

        if (type == null) {
            try { // prova ad eseguire il codice
                colonna = grid.addColumn(propertyName);
                colonna.setSortProperty(propertyName);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
            return;
        }// end of if cycle

        switch (type) {
            case text://@todo in futuro vanno differenziati
            case email:
            case textarea:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    String testo = "";

                    try { // prova ad eseguire il codice
                        if (field.get(entity) instanceof String) {
                            testo = (String) field.get(entity);
                        }// end of if cycle
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case integer:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    String testo = "";
                    int value;

                    try { // prova ad eseguire il codice
                        value = field.getInt(entity);
                        testo = text.format(value);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case lungo:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    String testo = "";
                    long value;

                    try { // prova ad eseguire il codice
                        value = field.getLong(entity);
                        testo = text.format(value);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case checkbox:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    boolean status = false;
                    Icon icon;

                    try { // prova ad eseguire il codice
                        status = field.getBoolean(entity);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    if (status) {
                        icon = new Icon(VaadinIcon.CHECK);
                        icon.setColor("green");
                    } else {
                        icon = new Icon(VaadinIcon.CLOSE);
                        icon.setColor("red");
                    }// end of if/else cycle
                    icon.setSize("1em");

                    return icon;
                }));//end of lambda expressions and anonymous inner class
                break;
            case booleano:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    boolean status = false;

                    try { // prova ad eseguire il codice
                        status = field.getBoolean(entity);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new ACheckBox(status);
                }));//end of lambda expressions and anonymous inner class
                break;
            case yesno:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    Label label = new Label();
                    String testo = "";
                    boolean status = false;

                    try { // prova ad eseguire il codice
                        status = field.getBoolean(entity);
                        testo = status ? "si" : "no";
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    if (text.isValid(testo)) {
                        label.setText(testo);
                        if (status) {
                            label.getStyle().set("color", "green");
                        } else {
                            label.getStyle().set("color", "red");
                        }// end of if/else cycle
                    }// end of if cycle

                    return label;
                }));//end of lambda expressions and anonymous inner class
                break;
            case yesnobold:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    Label label = new Label();
                    String testo = "";
                    boolean status = false;

                    try { // prova ad eseguire il codice
                        status = field.getBoolean(entity);
                        testo = status ? "si" : "no";
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    if (text.isValid(testo)) {
                        label.setText(testo);
                        label.getStyle().set("font-weight", "bold");
                        if (status) {
                            label.getStyle().set("color", "green");
                        } else {
                            label.getStyle().set("color", "red");
                        }// end of if/else cycle
                    }// end of if cycle

                    return label;
                }));//end of lambda expressions and anonymous inner class
                break;
            case enumeration:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Object obj = reflection.getPropertyValue(entity, propertyName);
                    return new Label(obj != null ? obj.toString() : "");
                }));//end of lambda expressions and anonymous inner class
                break;
            case multicombo:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    String testo = "";
                    List valueList;

                    try { // prova ad eseguire il codice
                        if (field.get(entity) instanceof List) {
                            valueList = (List) field.get(entity);
                            if (array.isValid(valueList)) {
                                for (Object singleValue : valueList) {
                                    testo += singleValue.toString();
                                    testo += VIRGOLA + SPAZIO;
                                }// end of for cycle
                                testo = text.levaCoda(testo.trim(), VIRGOLA).trim();
                            }// end of if cycle
                        }// end of if cycle
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case combo:
//                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
//                    ComboBox combo = new ComboBox();
//                    Object entityBean = reflection.getPropertyValue(entity, propertyName);
//                    IAService service = (IAService) StaticContextAccessor.getBean(clazz);
//                    List items = ((IAService) service).findAll();
//                    if (array.isValid(items)) {
//                        combo.setItems(items);
//                        combo.setValue(entityBean);
//                    }// end of if cycle
//                    combo.setEnabled(false);
//                    return combo;
//                }));
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    String testo = "";

                    try { // prova ad eseguire il codice
                        testo = field.get(entity).toString();
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case monthdate:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    LocalDate data;
                    String testo = "";

                    try { // prova ad eseguire il codice
                        data = (LocalDate) field.get(entity);
                        testo = date.getMonthLong(data);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case weekdate:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    LocalDate data;
                    String testo = "";

                    try { // prova ad eseguire il codice
                        data = (LocalDate) field.get(entity);
                        testo = date.getDayWeekShort(data);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case localdate:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    LocalDate data;
                    String testo = "";

                    try { // prova ad eseguire il codice
                        data = (LocalDate) field.get(entity);
                        testo = date.getDate(data);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case localdatetime:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    Object obj;
                    LocalDateTime timeStamp;
                    String testo = "";

                    try { // prova ad eseguire il codice
                        obj = field.get(entity);
                        if (obj instanceof LocalDateTime) {
                            timeStamp = (LocalDateTime) obj;
                            testo = date.getTime(timeStamp); //@todo aggiungere un selettore per modificare il format dalla annotation
                        } else {
                            log.warn("localdatetime non definito");
                        }// end of if/else cycle
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case localtime:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Field field = reflection.getField(entityClazz, propertyName);
                    Object obj;
                    LocalTime timeStamp;
                    String testo = "";

                    try { // prova ad eseguire il codice
                        obj = field.get(entity);
                        if (obj instanceof LocalTime) {
                            timeStamp = (LocalTime) obj;
                            testo = date.getOrario(timeStamp); //@todo aggiungere un selettore per modificare il format dalla annotation
                        } else {
                            log.warn("localtime non definito");
                        }// end of if/else cycle
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return new Label(testo);
                }));//end of lambda expressions and anonymous inner class
                break;
            case vaadinIcon:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Icon icon = null;
                    VaadinIcon vaadinIcon;
                    Field field = reflection.getField(entityClazz, propertyName);

                    try { // prova ad eseguire il codice
                        vaadinIcon = (VaadinIcon) field.get(entity);
                        icon = vaadinIcon.create();
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch
                    if (text.isValid(colorColumnName) && icon != null) {
                        icon.getElement().setAttribute("style", "color: " + colorColumnName);
                    }// end of if cycle

                    return icon != null ? icon : new Label("");
                }));//end of lambda expressions and anonymous inner class
                break;
            case color:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Label label = new Label();
                    String htmlCode = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    label.getElement().setProperty("innerHTML", htmlCode);
                    label.getElement().getStyle().set("background-color", (String)reflection.getPropertyValue(entity,propertyName));

                    return label;
                }));//end of lambda expressions and anonymous inner class
                headerIcon = headerIcon != null ? headerIcon : VaadinIcon.PALETE;
                break;
            case link:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Object obj = null;
                    Field field = reflection.getField(entityClazz, propertyName);

                    try { // prova ad eseguire il codice
                        obj = field.get(entity);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch
                    return new Label(obj != null ? obj.toString() : "");
                }));//end of lambda expressions and anonymous inner class
                break;
            case pref:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    EAPrefType typePref = (EAPrefType) reflection.getPropertyValue(entity, "type");
                    byte[] bytes = null;
                    Object value = null;
                    Field field = reflection.getField(entityClazz, propertyName);
                    Label label = null;
                    String message = "";

                    try { // prova ad eseguire il codice
                        bytes = (byte[]) field.get(entity);
                        value = typePref.bytesToObject(bytes);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    label = new Label();
                    switch (typePref) {
                        case string:
                            message = (String) value;
                            break;
                        case bool:
                            boolean status = (boolean) value;
                            message = status ? "si" : "no";
                            if (status) {
                                label.getStyle().set("color", "green");
                            } else {
                                label.getStyle().set("color", "red");
                            }// end of if/else cycle
                            break;
                        case integer:
                            message = text.format(value);
                            break;
                        case date:
                            break;
                        case email:
                            break;
                        case enumeration:
                            label.getStyle().set("color", "green");
                            message = enumService.convertToPresentation((String) value);
                            break;
                        default:
                            log.warn("Switch - caso non definito");
                            break;
                    } // end of switch statement
                    label.setText(message);
                    label.getStyle().set("font-weight", "bold");
                    return label;

                }));//end of lambda expressions and anonymous inner class
                break;
            case calculated:
                colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
                    Label label = new Label();
                    Method metodo = null;
                    Object serviceInstance = null;
                    String value = "";

                    if (appContext == null) {
                        log.error("Manca il valore di appContext");
                        return label;
                    }// end of if cycle

                    if (text.isEmpty(methodName)) {
                        log.error("Colonna calcolata '" + propertyName + "' - manca il methodName = ... nell'annotation @AIColumn della Entity " + entity.getClass().getSimpleName());
                        return label;
                    }// end of if cycle

                    try { // prova ad eseguire il codice
                        //--il metodo DEVE avere un solo parametro e di tipo AEntity
                        metodo = serviceClazz.getDeclaredMethod(methodName, AEntity.class);
                        serviceInstance = appContext.getBean(serviceClazz);
                        value = (String) metodo.invoke(serviceInstance, entity);
                        label.setText(value);
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch

                    return label;
                }));//end of lambda expressions and anonymous inner class
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement


        switch (type) {
            case text://@todo in futuro vanno differenziati
            case textarea:
                //--larghezza di default per un testo = 7em
                //--per larghezze minori o maggiori, inserire widthEM = ... nell'annotation @AIColumn della Entity
                width = text.isValid(width) ? width : "7em";
                break;
            case email:
                //--larghezza di default per un mail = 18em
                //--per larghezze diverse, inserire widthEM = ... nell'annotation @AIColumn della Entity
                width = text.isValid(width) ? width : "18em";
                break;
            case integer:
                //--larghezza di default per un intero = 3em
                //--gestisce numeri fino a 999
                //--per numeri più grandi, inserire widthEM = ... nell'annotation @AIColumn della Entity
                //--oppure usare EAFieldType.lungo nell'annotation @AIField della Entity
                width = text.isValid(width) ? width : "3em";
                break;
            case lungo:
                //--larghezza di default per un lungo = 7em
                //--gestisce numeri fino a 9.999.999
                width = text.isValid(width) ? width : "7em";
                break;
            case checkbox:
            case booleano:
            case yesno:
            case yesnobold:
                //--larghezza fissa per un booleano reso come checkbox oppure come testo si/no = 2.5em
                //--può essere aumentata con widthEM = ... nell'annotation @AIColumn della Entity
                //  se si vuole usare un header con un testo più lungo di 2.5em
                width = text.isValid(width) ? width : "2.5em";
                break;
            case enumeration:
                break;
            case multicombo:
                //--larghezza di default per un multicombo = 20em
                //--vale per la formattazione standard della colonna
                //--per modificare, inserire widthEM = ... nell'annotation @AIColumn della Entity
                width = text.isValid(width) ? width : "20em";
                break;
            case combo:
                break;
            case monthdate:
                //--larghezza di default per un data = 6em
                //--vale per la formattazione standard della data
                //--per modificare, inserire widthEM = ... nell'annotation @AIColumn della Entity
                width = text.isValid(width) ? width : "6em";
                break;
            case weekdate:
            case localdate:
                //--larghezza di default per un data = 7em
                //--vale per la formattazione standard della data
                //--per modificare, inserire widthEM = ... nell'annotation @AIColumn della Entity
                width = text.isValid(width) ? width : "7em";
                break;
            case localdatetime:
                //--larghezza di default per un data+tempo = 10em
                //--vale per la formattazione standard della data
                //--per modificare, inserire widthEM = ... nell'annotation @AIColumn della Entity
                width = text.isValid(width) ? width : "10em";
                break;
            case localtime:
                //--larghezza di default per il solo tempo = 5em
                //--vale per la formattazione standard della data
                //--per modificare, inserire widthEM = ... nell'annotation @AIColumn della Entity
                width = text.isValid(width) ? width : "5em";
                break;
            case vaadinIcon:
                //--larghezza di default per una icona = 3em
                //--per modificare, inserire widthEM = ... nell'annotation @AIColumn della Entity
                width = text.isValid(width) ? width : "3em";
                break;
            case color:
                //--larghezza di default per un rettangolo colorato = 3em
                //--per modificare, inserire widthEM = ... nell'annotation @AIColumn della Entity
                width = text.isValid(width) ? width : "3em";
                break;
            case link:
                break;
            case pref:
                break;
            case calculated:
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        if (colonna != null) {
            String headerText = "";
            //--l'header di default viene sempre uguale al nome della property
            //--può essere minuscolo o con la prima maiuscola, a seconda del flag di preferenza
            //--può essere modificato con name = "Xyz" nell'annotation @AIColumn della Entity
            if (pref.isBool(USA_GRID_HEADER_PRIMA_MAIUSCOLA)) {
                headerNotNull = text.primaMaiuscola(headerNotNull);
            } else {
                headerNotNull = headerNotNull.toLowerCase();
            }// end of if/else cycle

            //--eventuale aggiunta di una icona e l'header non è una String ma diventa un Component
            //--se c'è l'icona e manca il testo della annotation, NON usa il nome della property ma solo l'icona
            if (headerIcon != null) {
                Icon icon = new Icon(headerIcon);
                icon.setSize(widthHeaderIcon);
                icon.setColor(colorHeaderIcon);
                Label label = new Label(explicitHader);
                label.add(icon);
                colonna.setHeader(label);
            } else {
                colonna.setHeader(headerNotNull);
            }// end of if/else cycle

            //--di default le colonne NON sono sortable
            //--può essere modificata con sortable = true, nell'annotation @AIColumn della Entity
            colonna.setSortable(sortable);
//            colonna.setSortProperty(propertyName);

//            if (property.equals("id")) {
//                colonna.setWidth("1px");
//            }// end of if cycle

            if (isFlexGrow) {
                colonna.setFlexGrow(1);
                colonna.setWidth(width);
            } else {
                if (text.isValid(width)) {
                    colonna.setWidth(width);
                    colonna.setFlexGrow(0);
                }// end of if cycle
            }// end of if/else cycle

        }// end of if cycle

    }// end of method


    /**
     * Regola una singola colonna <br>
     * The columnService type is chosen according to the annotation @AIColumn or, if is not present, a @AIField <br>
     *
     * @param colonna      da regolare
     * @param entityClazz  modello-dati specifico
     * @param propertyName della property
     */
    public void fixColumn(Grid.Column colonna, Class<? extends AEntity> entityClazz, String propertyName) {
        String header = annotation.getColumnNameProperty(entityClazz, propertyName);
        String width = annotation.getColumnWithEM(entityClazz, propertyName);
        boolean isFlexGrow = annotation.isFlexGrow(entityClazz, propertyName);
        boolean sortable = annotation.isSortable(entityClazz, propertyName);

        colonna.setHeader(text.isValid(header) ? header : propertyName);
        colonna.setSortProperty(propertyName);
        colonna.setSortable(sortable);

        if (isFlexGrow) {
            colonna.setFlexGrow(1);
        } else {
            if (text.isValid(width)) {
                colonna.setWidth(width);
                colonna.setFlexGrow(0);
            }// end of if cycle
        }// end of if/else cycle

    }// end of method

}// end of class
