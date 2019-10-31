package it.algos.vaadflow.service;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.ui.IAView;
import it.algos.vaadflow.ui.list.AViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 21:58
 * <p>
 * Gestisce le interfacce @Annotation standard di Springs <br>
 * Gestisce le interfacce specifiche di VaadFlow: AIColumn, AIField, AIEntity, AIForm, AIList <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 2) AAnnotationService.getInstance(); <br>
 * 3) @Autowired private AAnnotationService annotationService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Slf4j
public class AAnnotationService extends AbstractService {

    public final static String TESTO_NULL = " non può essere vuoto";

    public final static String INT_NULL = " deve contenere solo caratteri numerici";

    public final static String INT_ZERO = " deve essere maggiore di zero";

    public final static String TAG_EM = "em";

    public final static String TAG_PX = "px";

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Private final property
     */
    private static final AAnnotationService INSTANCE = new AAnnotationService();


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private AAnnotationService() {
    }// end of constructor


    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AAnnotationService getInstance() {
        return INSTANCE;
    }// end of static method


    /**
     * Get the specific annotation of the class.
     * Entity class
     *
     * @param entityClazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public AIEntity getAIEntity(final Class<? extends AEntity> entityClazz) {
        return entityClazz != null ? entityClazz.getAnnotation(AIEntity.class) : null;
    }// end of method


    /**
     * Get the specific annotation of the class.
     * Entity class
     *
     * @param entityClazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public Document getDocument(final Class<? extends AEntity> entityClazz) {
        return entityClazz.getAnnotation(Document.class);
    }// end of method


    /**
     * Get the specific annotation of the class.
     * View class
     *
     * @param viewClazz the view class
     *
     * @return the Annotation for the specific class
     */
    public Qualifier getQualifier(final Class<? extends AViewList> viewClazz) {
        return viewClazz.getAnnotation(Qualifier.class);
    }// end of method


    /**
     * Get the name of the mongo collection name.
     * Cerca nella classe l'annotation @Document
     * Se non la trova, di default usa il nome (minuscolo) della classe AEntity
     *
     * @param entityClazz the entity class
     *
     * @return the name of the spring-view
     */
    public String getCollectionName(final Class<? extends AEntity> entityClazz) {
        String name = "";
        String entityName = entityClazz.getSimpleName().toLowerCase();
        Document annotation = getDocument(entityClazz);

        if (annotation != null) {
            name = annotation.collection();
        }// end of if cycle

        return text.isValid(name) ? name : entityName;
    }// end of method


    /**
     * Get the name of the route.
     *
     * @param viewClazz the view class
     *
     * @return the name of the vaadin-view
     */
    public String getRouteName(final Class<? extends AViewList> viewClazz) {
        String name = "";
        Route annotation = getRoute(viewClazz);

        if (annotation != null) {
            name = annotation.value();
        }// end of if cycle

        return name;
    }// end of method


    /**
     * Get the name of the qualifier.
     *
     * @param viewClazz the view class
     *
     * @return the qualifier of the spring-view
     */
    public String getQualifierName(final Class<? extends AViewList> viewClazz) {
        String name = "";
        Qualifier annotation = getQualifier(viewClazz);

        if (annotation != null) {
            name = annotation.value();
        }// end of if cycle

        return name;
    }// end of method


    /**
     * Get the specific annotation of the class.
     * Entity class
     *
     * @param entityClazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public AIForm getAIForm(final Class<? extends AEntity> entityClazz) {
        return entityClazz.getAnnotation(AIForm.class);
    }// end of method


    /**
     * Get the specific annotation of the class.
     * Entity class
     *
     * @param entityClazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public AIList getAIList(final Class<? extends AEntity> entityClazz) {
        return entityClazz.getAnnotation(AIList.class);
    }// end of method


    /**
     * Get the specific annotation of the class.
     * Entity class
     *
     * @param entityClazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public AIView getAIView(final Class<? extends IAView> entityClazz) {
        return entityClazz.getAnnotation(AIView.class);
    }// end of method


    /**
     * Get the specific annotation of the class.
     * View class
     *
     * @param entityClazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public Route getRoute(final Class<? extends IAView> entityClazz) {
        return entityClazz.getAnnotation(Route.class);
    }// end of method


    /**
     * Get the specific annotation of the field.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the Annotation for the specific field
     */
    public AIColumn getAIColumn(final Field reflectionJavaField) {
        if (reflectionJavaField != null) {
            return reflectionJavaField.getAnnotation(AIColumn.class);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Get the specific annotation of the field.
     *
     * @param entityClazz  the entity class
     * @param propertyName the property name
     *
     * @return the Annotation for the specific field
     */
    public String getFieldKeyMongo(Class<? extends AEntity> entityClazz, String propertyName) {
        String fieldKeyMongo = propertyName;
        org.springframework.data.mongodb.core.mapping.Field fieldAnnotation = null;
        Field reflectionJavaField = reflection.getField(entityClazz, propertyName);

        if (reflectionJavaField != null) {
            fieldAnnotation = reflectionJavaField.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
            if (fieldAnnotation != null) {
                fieldKeyMongo = fieldAnnotation.value();
            }// end of if cycle
        }// end of if cycle

        return fieldKeyMongo;
    }// end of method


    /**
     * Get the specific annotation of the field.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the Annotation for the specific field
     */
    public AIField getAIField(final Field reflectionJavaField) {
        if (reflectionJavaField != null) {
            return reflectionJavaField.getAnnotation(AIField.class);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Get the specific annotation of the field.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the Annotation for the specific field
     */
    public AIField getAIField(Class<? extends AEntity> entityClazz, String fieldName) {
        AIField annotation = null;
        Field reflectionJavaField;

        try { // prova ad eseguire il codice
            reflectionJavaField = entityClazz.getDeclaredField(fieldName);
            annotation = getAIField(reflectionJavaField);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return annotation;
    }// end of method


    /**
     * Get the specific annotation of the field.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the Annotation for the specific field
     */
    public AIColumn getAIColumn(Class<? extends AEntity> entityClazz, String fieldName) {
        AIColumn annotation = null;
        ArrayList<Field> listaFields;

        try { // prova ad eseguire il codice
            listaFields = reflection.getAllFields(entityClazz);

            if (array.isValid(listaFields)) {
                for (Field reflectionJavaField : listaFields) {
                    if (reflectionJavaField.getName().equals(fieldName)) {
                        annotation = getAIColumn(reflectionJavaField);
                        break;
                    }// end of if cycle
                }// end of for cycle

            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return annotation;
    }// end of method


    /**
     * Get the specific annotation of the field.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the Annotation for the specific field
     */
    public NotNull getNotNull(final Field reflectionJavaField) {
        if (reflectionJavaField != null) {
            return reflectionJavaField.getAnnotation(NotNull.class);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Get the specific annotation of the field.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the Annotation for the specific field
     */
    public Indexed getUnique(final Field reflectionJavaField) {
        if (reflectionJavaField != null) {
            return reflectionJavaField.getAnnotation(Indexed.class);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Get the specific annotation of the field.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the Annotation for the specific field
     */
    public Size getSize(final Field reflectionJavaField) {
        if (reflectionJavaField != null) {
            return reflectionJavaField.getAnnotation(Size.class);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Get the specific annotation of the field.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the Annotation for the specific field
     */
    public int getSizeMin(final Field reflectionJavaField) {
        int min = 0;
        Size annotation = this.getSize(reflectionJavaField);

        if (annotation != null) {
            min = annotation.min();
        }// end of if cycle

        return min;
    }// end of method


    /**
     * Restituisce il nome del menu
     * 1) Cerca in @interface AIView della classe AViewList la property menuName
     * 2) Se non la trova, cerca nella classe la property statica MENU_NAME
     * 3) Se non la trova, di default usa la property 'value' di @interface Route
     *
     * @param viewClazz the view class
     *
     * @return the name of the spring-view
     */
    public String getMenuName(final Class<? extends IAView> viewClazz) {
        String menuName = "";
        AIView annotationView = null;
        Route annotationRoute = null;

        /**
         * 1) Cerca in @interface AIView della classe la property menuName
         */
        annotationView = this.getAIView(viewClazz);
        if (annotationView != null) {
            menuName = annotationView.menuName();
        }// end of if cycle


        /**
         * 2) Se non la trova, cerca nella classe la property statica MENU_NAME
         */
        if (text.isEmpty(menuName)) {
            menuName = reflection.getMenuName(viewClazz);
        }// end of if cycle

        /**
         * 3) Se non la trova, di default usa la property 'value' di @interface Route
         */
        if (text.isEmpty(menuName)) {
            annotationRoute = this.getRoute(viewClazz);
        }// end of if cycle

        if (annotationRoute != null) {
            menuName = annotationRoute.value();
        }// end of if cycle

        menuName = text.isValid(menuName) ? text.primaMaiuscola(menuName) : "Home";

        return menuName;
    }// end of method


    /**
     * Valore della VaadinIcon di una view
     *
     * @param viewClazz classe view su cui operare la riflessione
     */
    public VaadinIcon getMenuIcon(final Class<? extends IAView> viewClazz) {
        VaadinIcon menuIcon = null;
        AIView annotationView = null;

        /**
         * 1) Cerca in @interface AIView della classe la property menuIcon
         */
        annotationView = this.getAIView(viewClazz);
        if (annotationView != null) {
            menuIcon = annotationView.menuIcon();
        }// end of if cycle

        return menuIcon;
    }// end of method


    /**
     * Restituisce il nome del record (da usare nel Dialog)
     * 1) Cerca in @interface AIEntity della classe AEntity la property recordName
     * 2) Se non lo trova, cerca in @interface Document della classe AEntity la property collection
     *
     * @param entityClazz the entity class
     *
     * @return the name of the recordName
     */
    public String getRecordName(final Class<? extends AEntity> entityClazz) {
        String recordName = "";
        AIEntity annotationEntity;

        /**
         * 1) Cerca in @interface AIEntity della classe AEntity la property recordName
         */
        annotationEntity = this.getAIEntity(entityClazz);
        if (annotationEntity != null) {
            recordName = annotationEntity.recordName();
        }// end of if cycle


        /**
         * 2) Se non la trova, cerca in @interface Document della classe AEntity la property collection
         */
        if (text.isEmpty(recordName)) {
            recordName = getCollectionName(entityClazz);
        }// end of if cycle

        return recordName;
    }// end of method


    /**
     * Restituisce il nome della property per le ricerche con searchField <br>
     *
     * @param viewClazz the view class
     *
     * @return the name of the property
     */
    public String getSearchPropertyName(final Class<? extends IAView> viewClazz) {
        String searchProperty = "";
        AIView annotationView = null;

        annotationView = this.getAIView(viewClazz);
        if (annotationView != null) {
            searchProperty = annotationView.searchProperty();
        }// end of if cycle

        return searchProperty;
    }// end of method


    /**
     * Nomi delle properties della Grid, estratti dalle @Annotation della Entity
     * Se la classe AEntity->@AIList prevede una lista specifica, usa quella lista (con o senza ID)
     * Se l'annotation @AIList non esiste od è vuota,
     * restituisce tutte le colonne (properties della classe e superclasse) //@todo da implementare
     * Sovrascrivibile
     *
     * @param entityClazz the entity class
     *
     * @return lista di nomi di property, oppure null se non esiste l'Annotation specifica @AIList() nella Entity
     */
    public ArrayList<String> getGridPropertiesName(final Class<? extends AEntity> entityClazz) {
        ArrayList<String> lista = null;
        String[] properties = null;
        AIList annotation = this.getAIList(entityClazz);

        if (annotation != null) {
            properties = annotation.fields();
        }// end of if cycle

        if (array.isValid(properties)) {
            lista = new ArrayList<String>(Arrays.asList(properties));
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Nomi delle properties del, estratti dalle @Annotation della Entity
     * Se la classe AEntity->@AIForm prevede una lista specifica, usa quella lista (con o senza ID)
     * Se l'annotation @AIForm non esiste od è vuota,
     * restituisce tutti i campi (properties della classe e superclasse)
     * Sovrascrivibile
     *
     * @return lista di nomi di property, oppure null se non esiste l'Annotation specifica @AIForm() nella Entity
     */
    public ArrayList<String> getFormPropertiesName(final Class<? extends AEntity> entityClazz) {
        ArrayList<String> lista = null;
        String[] properties = null;
        AIForm annotation = this.getAIForm(entityClazz);

        if (annotation != null) {
            properties = annotation.fields();
        }// end of if cycle

        if (array.isValid(properties)) {
            lista = new ArrayList<String>(Arrays.asList(properties));
        }// end of if cycle

        if (array.isEmpty(lista)) {
            lista = reflection.getAllFieldsNameNoCrono(entityClazz);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Nomi dei fields da considerare per estrarre i Java Reflected Field dalle @Annotation della Entity
     * Se la classe AEntity->@AIForm prevede una lista specifica, usa quella lista (con o senza ID)
     * Sovrascrivibile
     *
     * @return nomi dei fields, oppure null se non esiste l'Annotation specifica @AIForm() nella Entity
     */
    @SuppressWarnings("all")
    public ArrayList<String> getFormFieldsName(final Class<? extends AEntity> clazz) {
        ArrayList<String> lista = null;
        String[] fields = null;
        AIForm annotation = this.getAIForm(clazz);

        if (annotation != null) {
            fields = annotation.fields();
        }// end of if cycle

        if (array.isValid(fields)) {
            lista = new ArrayList(Arrays.asList(fields));
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Get the status 'nonUsata, facoltativa, obbligatoria' of the class.
     *
     * @param clazz the entity class
     */
    @SuppressWarnings("all")
    public EACompanyRequired getCompanyRequired(final Class<? extends AEntity> entityClazz) {
        EACompanyRequired companyRequired = EACompanyRequired.nonUsata;
        AIEntity annotation = getAIEntity(entityClazz);

        if (annotation != null) {
            companyRequired = annotation.company();
        }// end of if cycle

        return companyRequired;
    }// end of method


//    /**
//     * Get the roleTypeVisibility of the Entity class.
//     * Viene usata come default, se manca il valore specifico del singolo field
//     * La Annotation @AIEntity ha un suo valore di default per la property @AIEntity.roleTypeVisibility()
//     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
//     *
//     * @param clazz the entity class
//     *
//     * @return the roleTypeVisibility of the class
//     */
//    @SuppressWarnings("all")
//    public EARoleType getEntityRoleType(final Class<? extends AEntity> clazz) {
//        EARoleType roleTypeVisibility = null;
//        AIEntity annotation = this.getAIEntity(clazz);
//
//        if (annotation != null) {
//            roleTypeVisibility = annotation.roleTypeVisibility();
//        }// end of if cycle
//
//        return roleTypeVisibility != null ? roleTypeVisibility : EARoleType.guest;
//    }// end of method


    /**
     * Get the roleTypeVisibility of the View class.
     * La Annotation @AIView ha un suo valore di default per la property @AIView.roleTypeVisibility()
     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
     *
     * @param clazz the view class
     *
     * @return the roleTypeVisibility of the class
     */
    @SuppressWarnings("all")
    public EARoleType getViewRoleType(final Class<? extends IAView> clazz) {
        EARoleType roleTypeVisibility = null;
        AIView annotation = this.getAIView(clazz);

        if (annotation != null) {
            roleTypeVisibility = annotation.roleTypeVisibility();
        }// end of if cycle

        return roleTypeVisibility != null ? roleTypeVisibility : EARoleType.user;
    }// end of method


    public boolean isMenuProgettoBase(final Class<? extends IAView> clazz) {
        boolean status = false;
        AIView annotation = this.getAIView(clazz);

        if (annotation != null) {
            status = annotation.vaadflow();
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Get the accessibility status of the class for the developer login.
     *
     * @param clazz the view class
     *
     * @return true if the class is visible
     */
    @SuppressWarnings("all")
    public boolean getViewAccessibilityDev(final Class<? extends IAView> clazz) {
        EARoleType roleTypeVisibility = getViewRoleType(clazz);
        return (roleTypeVisibility != null && roleTypeVisibility == EARoleType.developer);
    }// end of method


//    /**
//     * Get the accessibility status of the class for the developer login.
//     * Viene usata come default, se manca il valore specifico del singolo field
//     * La Annotation @AIForm ha un suo valore di default per la property @AIForm.fieldsDev()
//     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
//     *
//     * @param clazz the entity class
//     *
//     * @return accessibilità del Form
//     */
//    @SuppressWarnings("all")
//    public EAFieldAccessibility getFormAccessibilityDev(Class clazz) {
//        EAFieldAccessibility formAccessibility = null;
//        AIForm annotation = this.getAIForm(clazz);
//
//        if (annotation != null) {
//            formAccessibility = annotation.fieldsDev();
//        }// end of if cycle
//
//        return formAccessibility != null ? formAccessibility : EAFieldAccessibility.allways;
//    }// end of method


//    /**
//     * Get the accessibility status of the class for the admin login.
//     * Viene usata come default, se manca il valore specifico del singolo field
//     * La Annotation @AIForm ha un suo valore di default per la property @AIForm.fieldsAdmin()
//     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
//     *
//     * @param clazz the entity class
//     *
//     * @return accessibilità del Form
//     */
//    @SuppressWarnings("all")
//    public EAFieldAccessibility getFormAccessibilityAdmin(Class clazz) {
//        EAFieldAccessibility formAccessibility = null;
//        AIForm annotation = this.getAIForm(clazz);
//
//        if (annotation != null) {
//            formAccessibility = annotation.fieldsAdmin();
//        }// end of if cycle
//
//        return formAccessibility != null ? formAccessibility : EAFieldAccessibility.showOnly;
//    }// end of method
//

//    /**
//     * Get the accessibility status of the class for the user login.
//     * Viene usata come default, se manca il valore specifico del singolo field
//     * La Annotation @AIForm ha un suo valore di default per la property @AIForm.fieldsUser()
//     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
//     *
//     * @param clazz the entity class
//     *
//     * @return accessibilità del Form
//     */
//    @SuppressWarnings("all")
//    public EAFieldAccessibility getFormAccessibilityUser(Class clazz) {
//        EAFieldAccessibility formAccessibility = null;
//        AIForm annotation = this.getAIForm(clazz);
//
//        if (annotation != null) {
//            formAccessibility = annotation.fieldsUser();
//        }// end of if cycle
//
//        return formAccessibility != null ? formAccessibility : EAFieldAccessibility.never;
//    }// end of method


    /**
     * Get the name (columnService) of the property.
     * Se manca, rimane vuoto
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the name (columnService) of the field
     */
    public String getExplicitColumnName(final Field reflectionJavaField) {
        String name = "";
        AIColumn annotation = this.getAIColumn(reflectionJavaField);

        if (annotation != null) {
            name = annotation.name();
        }// end of if cycle

        return name;
    }// end of method


    /**
     * Get the name (columnService) of the property.
     * Se manca, usa il nome del Field
     * Se manca, usa il nome della property
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the name (columnService) of the field
     */
    public String getColumnNameProperty(final Field reflectionJavaField) {
        String name = "";
        AIColumn annotation = this.getAIColumn(reflectionJavaField);

        if (annotation != null) {
            name = annotation.name();
        }// end of if cycle

        if (text.isEmpty(name)) {
            name = this.getFormFieldName(reflectionJavaField);
        }// end of if cycle

        return name;
    }// end of method


    /**
     * Get the name (columnService) of the property.
     * Se manca, rimane vuoto
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the name (columnService) of the field
     */
    public String getExplicitColumnName(Class<? extends AEntity> entityClazz, String fieldName) {
        String name = "";
        Field field = reflection.getField(entityClazz, fieldName);

        if (field != null) {
            name = getExplicitColumnName(field);
        }// end of if cycle

        return name;
    }// end of method


    /**
     * Get the name (columnService) of the property.
     * Se manca, usa il nome del Field
     * Se manca, usa il nome della property
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the name (columnService) of the field
     */
    public String getColumnNameProperty(Class<? extends AEntity> entityClazz, String fieldName) {
        String name = "";
        Field field = reflection.getField(entityClazz, fieldName);

        if (field != null) {
            name = getColumnNameProperty(field);
        }// end of if cycle

        return name;
    }// end of method


    /**
     * Get the visibility of the columnService.
     * Di default true
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the visibility of the columnService
     */
    @Deprecated
    public boolean isColumnVisibile(final Field reflectionJavaField) {
        boolean visibile = false;
//        EARoleType roleTypeVisibility = EARoleType.nobody;
//        AIColumn annotation = this.getAIColumn(reflectionJavaField);
//
//        if (annotation != null) {
//            roleTypeVisibility = annotation.roleTypeVisibility();
//        }// end of if cycle
//
//        switch (roleTypeVisibility) {
//            case nobody:
//                visibile = false;
//                break;
//            case developer:
//                //@todo RIMETTERE
//
////                if (LibSession.isDeveloper()) {
//                visibile = true;
////                }// end of if cycle
//                break;
//            case admin:
//                //@todo RIMETTERE
//
//                //                if (LibSession.isAdmin()) {
//                visibile = true;
////                }// end of if cycle
//                break;
//            case user:
//                visibile = true;
//                break;
//            case guest:
//                visibile = true;
//                break;
//            default:
//                visibile = true;
//                break;
//        } // end of switch statement

        return visibile;
    }// end of method


    /**
     * Get the width of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the name (columnService) of the field
     */
    public String getColumnWithEM(Class<? extends AEntity> entityClazz, String fieldName) {
        String widthTxt = "";
        int widthInt = 0;
        AIColumn annotation = this.getAIColumn(entityClazz, fieldName);

        if (annotation != null) {
            widthInt = annotation.widthEM();
        }// end of if cycle

        if (widthInt > 0) {
            widthTxt = widthInt + TAG_EM;
        }// end of if cycle

        return widthTxt;
    }// end of method


    /**
     * Get the status flexibility of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return status of field
     */
    public boolean isFlexGrow(Class<? extends AEntity> entityClazz, String fieldName) {
        boolean status = false;
        Field field = reflection.getField(entityClazz, fieldName);

        if (field != null) {
            status = isFlexGrow(field);
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Get the status flexibility of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return status of field
     */
    public boolean isFlexGrow(Field reflectionJavaField) {
        boolean status = false;
        AIColumn annotation = this.getAIColumn(reflectionJavaField);

        if (annotation != null) {
            status = annotation.flexGrow();
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Get the status sortable of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return status of field
     */
    public boolean isSortable(Class<? extends AEntity> entityClazz, String fieldName) {
        boolean status = false;
        Field field = reflection.getField(entityClazz, fieldName);

        if (field != null) {
            status = isSortable(field);
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Get the status flexibility of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return status of field
     */
    public boolean isSortable(Field reflectionJavaField) {
        boolean status = false;
        AIColumn annotation = this.getAIColumn(reflectionJavaField);

        if (annotation != null) {
            status = annotation.sortable();
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Get the color of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the color (columnService) of the field
     */
    public String getColumnColor(Class<? extends AEntity> entityClazz, String fieldName) {
        String color = "";
        AIColumn annotation = this.getAIColumn(entityClazz, fieldName);

        if (annotation != null) {
            color = annotation.color();
        }// end of if cycle

        return color;
    }// end of method


    /**
     * Get the color of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the color of the field
     */
    public String getFieldColor(final Field reflectionJavaField) {
        String color = "";
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            color = annotation.color();
        }// end of if cycle

        return color;
    }// end of method


    /**
     * Get the type (field) of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the type for the specific field
     */
    public EAFieldType getFormType(final Field reflectionJavaField) {
        EAFieldType type = null;
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            type = annotation.type();
        }// end of if cycle

        return type;
    }// end of method


    /**
     * Get the type (field) of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the type for the specific field
     */
    public EAFieldType getFormType(Class<? extends AEntity> entityClazz, String fieldName) {
        EAFieldType type = null;

        Field field = reflection.getField(entityClazz, fieldName);
        type = getFormType(field);

        return type;
    }// end of method


    /**
     * Get the type (field) of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the type for the specific column
     */
    public EAFieldType getColumnType(final Field reflectionJavaField) {
        EAFieldType type = null;
        AIColumn annotation = this.getAIColumn(reflectionJavaField);

        if (annotation != null) {
            type = annotation.type();
        }// end of if cycle

        if (type == EAFieldType.ugualeAlForm) {
            type = getFormType(reflectionJavaField);
        }// end of if cycle

        if (type == null) {
            type = getFormType(reflectionJavaField);
        }// end of if cycle

        return type;
    }// end of method


    /**
     * Get the type (field) of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the type for the specific column
     */
    public EAFieldType getColumnType(Class<? extends AEntity> entityClazz, String fieldName) {
        EAFieldType type = null;

        Field field = reflection.getField(entityClazz, fieldName);
        type = getColumnType(field);

        if (type == EAFieldType.ugualeAlForm) {
            type = getFormType(entityClazz, fieldName);
        }// end of if cycle

        return type;
    }// end of method


    /**
     * Get the items (String) of the enum.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the items
     */
    public List<String> getEnumItems(final Field reflectionJavaField) {
        List<String> items = null;
        String value = "";
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            value = annotation.items();
        }// end of if cycle

        if (text.isValid(value)) {
            items = array.getList(value);
        }// end of if cycle

        return items;
    }// end of method


    /**
     * Get the clazz of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the type for the specific columnService
     */
    public Class getClazz(final Field reflectionJavaField) {
        Class clazz = null;
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            clazz = annotation.serviceClazz();
        }// end of if cycle

        return clazz;
    }// end of method


    /**
     * Get the clazz of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the type for the specific columnService
     */
    public Class getClazz(Class<? extends AEntity> entityClazz, String fieldName) {
        Class clazz = null;
        Field field = reflection.getField(entityClazz, fieldName);

        if (field != null) {
            clazz = getClazz(field);
        }// end of if cycle

        return clazz;
    }// end of method


    /**
     * Get the name (field) of the property.
     * Se manca, usa il nome della property
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the name (rows) of the field
     */
    public String getFormFieldName(final Field reflectionJavaField) {
        String name = null;
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            name = annotation.name();
        }// end of if cycle

        if (text.isEmpty(name)) {
            name = reflectionJavaField.getName();
        }// end of if cycle

//        return text.primaMaiuscola(name);
        return name;
    }// end of method


    /**
     * Get the name (field) of the property.
     * Se manca, usa il nome della property
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the capitalized name (rows) of the field
     */
    public String getFormFieldNameCapital(final Field reflectionJavaField) {
        return text.primaMaiuscola(getFormFieldName(reflectionJavaField));
    }// end of method


    /**
     * Get the status focus of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return status of field
     */
    public boolean isFocus(Field reflectionJavaField) {
        boolean status = true;
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            status = annotation.focus();
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Get the status focus of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return status of field
     */
    public boolean isFocus(Class<? extends AEntity> entityClazz, String fieldName) {
        boolean status = true;
        Field field = reflection.getField(entityClazz, fieldName);

        if (field != null) {
            status = isFocus(field);
        }// end of if cycle

        return status;
    }// end of method


//    /**
//     * Get the class of the property.
//     *
//     * @param reflectionJavaField di riferimento per estrarre la Annotation
//     *
//     * @return the class for the specific columnService
//     */
//    @SuppressWarnings("all")
//    public Class getComboClass(Field reflectionJavaField) {
//        Class linkClazz = null;
//        AIField annotation = this.getAIField(reflectionJavaField);
//
//        if (annotation != null) {
//            linkClazz = annotation.serviceClazz();
//        }// end of if cycle
//
//        return linkClazz;
//    }// end of method
//
//
//    /**
//     * Get the class of the property.
//     *
//     * @param reflectionJavaField di riferimento per estrarre la Annotation
//     *
//     * @return the class for the specific columnService
//     */
//    @SuppressWarnings("all")
//    public Class getComboClass(Class<? extends AEntity> entityClazz, String fieldName) {
//        Class linkClazz = null;
//        Field field = reflection.getField(entityClazz, fieldName);
//
//        if (field != null) {
//            linkClazz = getComboClass(field);
//        }// end of if cycle
//
//        return linkClazz;
//    }// end of method


    /**
     * Get the class of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the class for the specific columnService
     */
    @SuppressWarnings("all")
    public Class getEnumClass(Field reflectionJavaField) {
        Class enumClazz = null;
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            enumClazz = annotation.enumClazz();
        }// end of if cycle

        return enumClazz == Object.class ? null : enumClazz;
    }// end of method


    /**
     * Get the class of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the class for the specific columnService
     */
    @SuppressWarnings("all")
    public Class getLinkClass(Field reflectionJavaField) {
        Class linkClazz = null;
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            linkClazz = annotation.linkClazz();
        }// end of if cycle

        return linkClazz == Object.class ? null : linkClazz;
    }// end of method


    /**
     * Get the class of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the class for the specific columnService
     */
    @SuppressWarnings("all")
    public Class getServiceClass(Field reflectionJavaField) {
        Class serviceClazz = null;
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            serviceClazz = annotation.serviceClazz();
        }// end of if cycle

        return serviceClazz == Object.class ? null : serviceClazz;
    }// end of method


    /**
     * Get the class of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the class for the specific columnService
     */
    @SuppressWarnings("all")
    public Class getEnumClass(Class<? extends AEntity> entityClazz, String fieldName) {
        Class linkClazz = null;
        Field field = reflection.getField(entityClazz, fieldName);

        if (field != null) {
            linkClazz = getEnumClass(field);
        }// end of if cycle

        return linkClazz;
    }// end of method


    /**
     * Get the class of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the class for the specific columnService
     */
    @SuppressWarnings("all")
    public Class getLinkClass(Class<? extends AEntity> entityClazz, String fieldName) {
        Class linkClazz = null;
        Field field = reflection.getField(entityClazz, fieldName);

        if (field != null) {
            linkClazz = getLinkClass(field);
        }// end of if cycle

        return linkClazz;
    }// end of method


    /**
     * Get the class of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the class for the specific columnService
     */
    @SuppressWarnings("all")
    public Class getServiceClass(Class<? extends AEntity> entityClazz, String fieldName) {
        Class linkClazz = null;
        Field field = reflection.getField(entityClazz, fieldName);

        if (field != null) {
            linkClazz = getServiceClass(field);
        }// end of if cycle

        return linkClazz;
    }// end of method


    /**
     * Get the width of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the width of the field expressed in em
     */
    public int getWidth(Field reflectionJavaField) {
        int widthInt = 0;
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            widthInt = annotation.widthEM();
        }// end of if cycle

        return widthInt;
    }// end of method


    /**
     * Get the widthEM of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the width of the field expressed in em
     */
    public String getWidthEM(Field reflectionJavaField) {
        String width = "";
        int widthInt = this.getWidth(reflectionJavaField);
        String tag = "em";

        if (widthInt > 0) {
            width = widthInt + tag;
        }// end of if cycle

        return width;
    }// end of method


    /**
     * Get the alert message from @NotNull
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the alert message
     */
    public String getMessageNull(Field reflectionJavaField) {
        String message = "";
        NotNull annotation = this.getNotNull(reflectionJavaField);
        EAFieldType type;

        if (annotation != null) {
            message = annotation.message();
        }// end of if cycle

        if (message.equals("{javax.validation.constraints.NotNull.message}")) {
            message = "";
            type = getFormType(reflectionJavaField);
            if (type == EAFieldType.text) {
                message = text.primaMaiuscola(reflectionJavaField.getName()) + TESTO_NULL;
            }// end of if cycle
            if (type == EAFieldType.integer) {
                message = text.primaMaiuscola(reflectionJavaField.getName()) + INT_NULL;
            }// end of if cycle
        }// end of if cycle

        return message;
    }// end of method


    /**
     * Get the alert message from @Size
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the alert message
     */
    public String getMessageSize(Field reflectionJavaField) {
        String message = "";
        Size annotation = this.getSize(reflectionJavaField);
        EAFieldType type = getFormType(reflectionJavaField);
        int min = 0;

        if (type != EAFieldType.text) {
            return "";
        }// end of if cycle

        if (annotation == null) {
            message = this.getMessage(reflectionJavaField);
        } else {
            message = annotation.message();
            if (message.equals("{javax.validation.constraints.Size.message}")) {
                min = annotation.min();
                if (min > 0) {
                    message = text.primaMaiuscola(reflectionJavaField.getName()) + " deve contenere almeno " + min + " caratteri";
                }// end of if cycle
            }// end of if cycle
        }// end of if/else cycle


        return message;
    }// end of method


    /**
     * Get the alert message from @NotNull or from @Size
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return the alert message
     */
    public String getMessage(Field reflectionJavaField) {
        String message = "";

        message = getMessageNull(reflectionJavaField);

//        if (text.isEmpty(message)) {
//            message = getMessageSize(reflectionJavaField);
//        }// end of if cycle

        return message;
    }// end of method


    /**
     * Get the status required of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return status of field
     */
    public boolean isRequired(Field reflectionJavaField) {
        boolean status = false;
        AIField annotation = this.getAIField(reflectionJavaField);

        if (annotation != null) {
            status = annotation.required();
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Get the status required of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return status of field
     */
    public boolean isNotNull(Field reflectionJavaField) {
        return getNotNull(reflectionJavaField) != null;
    }// end of method


    /**
     * Get the status required of the property.
     *
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return status of field
     */
    public boolean isUnique(Field reflectionJavaField) {
        boolean status = false;
        Indexed annotation = this.getUnique(reflectionJavaField);

        if (annotation != null) {
            status = annotation.unique();
        }// end of if cycle

        return status;

    }// end of method


//    /**
//     * Get the roleTypeVisibility of the field.
//     * La Annotation @AIField ha un suo valore di default per la property @AIField.roleTypeVisibility()
//     * Se il field lo prevede (valore di default) ci si rifà al valore generico del Form
//     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
//     *
//     * @param reflectionJavaField di riferimento per estrarre le Annotation
//     *
//     * @return the ARoleType of the field
//     */
//    @SuppressWarnings("all")
//    public EARoleType getFieldRoleType(final Field reflectionJavaField) {
//        EARoleType roleTypeVisibility = null;
//        AIField annotation = this.getAIField(reflectionJavaField);
//
//        if (annotation != null) {
//            roleTypeVisibility = annotation.roleTypeVisibility();
//        }// end of if cycle
//
//        if (roleTypeVisibility == EARoleType.asEntity) {
//            Class clazz = reflectionJavaField.getDeclaringClass();
//            if (AEntity.class.isAssignableFrom(clazz)) {
//                roleTypeVisibility = this.getEntityRoleType(clazz);
//            }// end of if cycle
//        }// end of if cycle
//
//        return roleTypeVisibility;
//    }// end of method
//

//    /**
//     * Get the visibility of the field.
//     * Controlla il ruolo del login connesso
//     *
//     * @param reflectionJavaField di riferimento per estrarre le Annotation
//     *
//     * @return the visibility of the field
//     */
//    @SuppressWarnings("all")
//    public boolean isFieldVisibileRole(Field reflectionJavaField) {
//        boolean visibile = false;
//        EARoleType roleTypeVisibility = this.getFieldRoleType(reflectionJavaField);
//
//        if (roleTypeVisibility == EARoleType.asEntity) {
//            Class clazz = reflectionJavaField.getDeclaringClass();
//            if (AEntity.class.isAssignableFrom(clazz)) {
//                roleTypeVisibility = this.getEntityRoleType(clazz);
//            }// end of if cycle
//        }// end of if cycle
//
//        if (roleTypeVisibility!=null) {
//            switch (roleTypeVisibility) {
//                case nobody:
//                    visibile = false;
//                    break;
//                case developer:
//                    if (session.isDeveloper()) {
//                        visibile = true;
//                    }// end of if cycle
//                    break;
//                case admin:
//                    if (session.isAdmin() || session.isDeveloper()) {
//                        visibile = true;
//                    }// end of if cycle
//                    break;
//                case user:
//                    if (session.isUser() || session.isAdmin() || session.isDeveloper()) {
//                        visibile = true;
//                    }// end of if cycle
//                    break;
//                case guest:
//                    visibile = false;
//                    break;
//                default:
//                    visibile = false;
//                    log.warn("Switch - caso non definito");
//                    break;
//            } // end of switch statement
//        }// end of if cycle
//
//        return visibile;
//    }// end of method


//    /**
//     * Get the enabled state of the field.
//     * Controlla la visibilità del field
//     * Controlla il grado di accesso consentito
//     * Di default true
//     *
//     * @param reflectionJavaField di riferimento per estrarre la Annotation
//     *
//     * @return the visibility of the field
//     */
//    @SuppressWarnings("all")
//    public boolean isFieldEnabled(Field reflectionJavaField, boolean nuovaEntity) {
//        boolean enabled = true;
//        boolean visibile = isFieldVisibileRole(reflectionJavaField);
//
//        if (visibile) {
//            enabled = isFieldEnabledAccess(reflectionJavaField, nuovaEntity);
//        }// end of if cycle
//
//        return enabled;
//    }// end of method


//    /**
//     * Get the enabled state of the field.
//     * Controlla il grado di accesso consentito
//     *
//     * @param reflectionJavaField di riferimento per estrarre la Annotation
//     *
//     * @return the visibility of the field
//     */
//    @SuppressWarnings("all")
//    public boolean isFieldEnabledAccess(Field reflectionField, boolean nuovaEntity) {
//        boolean enabled = true;
//        EAFieldAccessibility fieldAccessibility = this.getFieldAccessibility(reflectionField);
//
//        switch (fieldAccessibility) {
//            case allways:
//                enabled = true;
//                break;
//            case newOnly:
//                enabled = nuovaEntity;
//                break;
//            case showOnly:
//                enabled = false;
//                break;
//            case never:
//                enabled = false;
//                break;
//            default:
//                enabled = true;
//                break;
//        } // end of switch statement
//
//        return enabled;
//    }// end of method

//    /**
//     * Get the accessibility status of the field for the developer login.
//     * La Annotation @AIField ha un suo valore di default per la property @AIField.dev()
//     * Se il field lo prevede (valore di default) ci si rifà al valore generico del Form
//     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
//     *
//     * @param reflectionJavaField di riferimento per estrarre la Annotation
//     *
//     * @return accessibilità del field
//     */
//    @SuppressWarnings("all")
//    public EAFieldAccessibility getFieldAccessibilityDev(Field reflectionJavaField) {
//        EAFieldAccessibility fieldAccessibility = null;
//        AIField annotation = this.getAIField(reflectionJavaField);
//
//        if (annotation != null) {
//            fieldAccessibility = annotation.dev();
//        }// end of if cycle
//
//        if (fieldAccessibility == EAFieldAccessibility.asForm) {
//            fieldAccessibility = this.getFormAccessibilityDev(reflectionJavaField.getClass());
//        }// end of if cycle
//
//        return fieldAccessibility != null ? fieldAccessibility : EAFieldAccessibility.allways;
//    }// end of method


//    /**
//     * Get the accessibility status of the field for the admin login.
//     * La Annotation @AIField ha un suo valore di default per la property @AIField.admin()
//     * Se il field lo prevede (valore di default) ci si rifà al valore generico del Form
//     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
//     *
//     * @param reflectionJavaField di riferimento per estrarre la Annotation
//     *
//     * @return accessibilità del field
//     */
//    @SuppressWarnings("all")
//    public EAFieldAccessibility getFieldAccessibilityAdmin(Field reflectionJavaField) {
//        EAFieldAccessibility fieldAccessibility = null;
//        AIField annotation = this.getAIField(reflectionJavaField);
//
//        if (annotation != null) {
//            fieldAccessibility = annotation.admin();
//        }// end of if cycle
//
//        if (fieldAccessibility == EAFieldAccessibility.asForm) {
//            fieldAccessibility = getFormAccessibilityAdmin(reflectionJavaField.getClass());
//        }// end of if cycle
//
//        return fieldAccessibility != null ? fieldAccessibility : EAFieldAccessibility.showOnly;
//    }// end of method


//    /**
//     * Get the accessibility status of the field for the user login.
//     * La Annotation @AIField ha un suo valore di default per la property @AIField.user()
//     * Se il field lo prevede (valore di default) ci si rifà al valore generico del Form
//     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
//     *
//     * @param reflectionJavaField di riferimento per estrarre la Annotation
//     *
//     * @return accessibilità del field
//     */
//    @SuppressWarnings("all")
//    public EAFieldAccessibility getFieldAccessibilityUser(Field reflectionJavaField) {
//        EAFieldAccessibility fieldAccessibility = null;
//        AIField annotation = this.getAIField(reflectionJavaField);
//
//        if (annotation != null) {
//            fieldAccessibility = annotation.user();
//        }// end of if cycle
//
//        if (fieldAccessibility == EAFieldAccessibility.asForm) {
//            fieldAccessibility = getFormAccessibilityUser(reflectionJavaField.getClass());
//        }// end of if cycle
//
//        return fieldAccessibility != null ? fieldAccessibility : EAFieldAccessibility.never;
//    }// end of method


//    /**
//     * Get the accessibility status of the field for the current login.
//     * Se manca completamente l'annotation, inserisco qui un valore di default (per evitare comunque un nullo)
//     *
//     * @param reflectionJavaField di riferimento per estrarre la Annotation
//     *
//     * @return accessibilità del field
//     */
//    @SuppressWarnings("all")
//    public EAFieldAccessibility getFieldAccessibility(Field reflectionJavaField) {
//        EAFieldAccessibility fieldAccessibility = EAFieldAccessibility.never;
//
//        if (session.isDeveloper()) {
//            fieldAccessibility = getFieldAccessibilityDev(reflectionJavaField);
//        } else {
//            if (session.isAdmin()) {
//                fieldAccessibility = getFieldAccessibilityAdmin(reflectionJavaField);
//            } else {
//                if (session.isUser()) {
//                    fieldAccessibility = getFieldAccessibilityUser(reflectionJavaField);
//                }// end of if cycle
//            }// end of if/else cycle
//        }// end of if/else cycle
//
//        return fieldAccessibility;
//    }// end of method


//    /**
//     * Get the status of visibility for the field of ACompanyEntity.
//     * <p>
//     * Controlla se l'applicazione usa le company2 - flag  AlgosApp.USE_MULTI_COMPANY=true
//     * Controlla se la collection (table) usa la company2
//     * Controlla se l'buttonUser collegato è un developer
//     *
//     * @param clazz the entity class
//     *
//     * @return status - default true
//     */
//    public boolean isCompanyFieldVisible(final Class<? extends AEntity> clazz) {
//        boolean status = true;
//
//        //@todo RIMETTERE
//
////        if (!AlgosApp.USE_MULTI_COMPANY) {
////            return false;
////        }// end of if cycle
////
////        if (LibAnnotation.companyType(clazz) == ACompanyRequired.nonUsata) {
////            return false;
////        }// end of if cycle
////
////        if (!LibSession.isDeveloper()) {
////            return false;
////        }// end of if cycle
//
//        return status;
//    }// end of method


//    /**
//     * Tipo di lista (EAListButton) indicata nella AEntity class per la view AList
//     *
//     * @return valore della enumeration
//     */
//    @SuppressWarnings("all")
//    public EAListButton getListBotton(final Class<? extends AEntity> clazz) {
//        EAListButton listaNomi = EAListButton.standard;
//
//        //@todo RIMETTERE
//
////        if (LibSession.isDeveloper()) {
////            listaNomi = getListBottonDev(clazz);
////        } else {
////            if (LibSession.isAdmin()) {
////                listaNomi = getListBottonAdmin(clazz);
////            } else {
////                if (true) {
////                    listaNomi = getListBottonUser(clazz);
////                }// end of if cycle
////            }// end of if/else cycle
////        }// end of if/else cycle
//
//        return listaNomi;
//    }// end of method


    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public EAFormButton getFormBottonDev(final Class<? extends AEntity> clazz) {
        EAFormButton listaNomiBottoni = EAFormButton.standard;
        AIForm annotation = this.getAIForm(clazz);

        if (annotation != null) {
            listaNomiBottoni = annotation.buttonsDev();
        }// end of if cycle

        return listaNomiBottoni;
    }// end of method


    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public EAFormButton getFormBottonAdmin(final Class<? extends AEntity> clazz) {
        EAFormButton listaNomiBottoni = EAFormButton.standard;
        AIForm annotation = this.getAIForm(clazz);

        if (annotation != null) {
            listaNomiBottoni = annotation.buttonsAdmin();
        }// end of if cycle

        return listaNomiBottoni;
    }// end of method


    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public EAFormButton getFormBottonUser(final Class<? extends AEntity> clazz) {
        EAFormButton listaNomiBottoni = EAFormButton.standard;
        AIForm annotation = this.getAIForm(clazz);

        if (annotation != null) {
            listaNomiBottoni = annotation.buttonsUser();
        }// end of if cycle

        return listaNomiBottoni;
    }// end of method


//    /**
//     * Tipo di lista (EAFormButton) indicata nella AEntity class per la view AForm
//     *
//     * @return valore della enumeration
//     */
//    @SuppressWarnings("all")
//    public EAFormButton getFormBotton(final Class<? extends AEntity> clazz) {
//        EAFormButton listaNomi = EAFormButton.standard;
//
//        if (login.isDeveloper()) {
//            listaNomi = getFormBottonDev(clazz);
//        } else {
//            if (login.isAdmin()) {
//                listaNomi = getFormBottonAdmin(clazz);
//            } else {
//                if (true) {
//                    listaNomi = getFormBottonUser(clazz);
//                }// end of if cycle
//            }// end of if/else cycle
//        }// end of if/else cycle
//
//        return listaNomi;
//    }// end of method


    /**
     * Get the icon of the property.
     * Default a VaadinIcon.YOUTUBE che sicuramente non voglio usare
     * e posso quindi escluderlo
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the icon of the field
     */
    public VaadinIcon getHeaderIcon(Class<? extends AEntity> entityClazz, String fieldName) {
        VaadinIcon icon = null;
        AIColumn annotation = this.getAIColumn(entityClazz, fieldName);

        if (annotation != null) {
            icon = annotation.headerIcon();
        }// end of if cycle

        if (icon == VaadinIcon.YOUTUBE) {
            icon = null;
        }// end of if cycle

        return icon;
    }// end of method


    /**
     * Get the size of the icon of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the size of the icon
     */
    public String getHeaderIconSizePX(Class<? extends AEntity> entityClazz, String fieldName) {
        int widthInt = 0;
        int standard = 20;
        AIColumn annotation = this.getAIColumn(entityClazz, fieldName);

        if (annotation != null) {
            widthInt = annotation.headerIconSizePX();
        }// end of if cycle

        if (widthInt == 0) {
            widthInt = standard;
        }// end of if cycle

        return widthInt + TAG_PX;
    }// end of method


    /**
     * Get the color of the property.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the color of the icon
     */
    public String getHeaderIconColor(Class<? extends AEntity> entityClazz, String fieldName) {
        String color = "";
        AIColumn annotation = this.getAIColumn(entityClazz, fieldName);

        if (annotation != null) {
            color = annotation.headerIconColor();
        }// end of if cycle

        return color;
    }// end of method


    /**
     * Get the method name for reflection.
     *
     * @param entityClazz the entity class
     * @param fieldName   the property name
     *
     * @return the method name
     */
    public String getMethodName(Class<? extends AEntity> entityClazz, String fieldName) {
        String methodName = "";
        AIColumn annotation = this.getAIColumn(entityClazz, fieldName);

        if (annotation != null) {
            methodName = annotation.methodName();
        }// end of if cycle

        return methodName;
    }// end of method

}// end of class
