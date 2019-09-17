package it.algos.vaadwam.migration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.HashSet;
import java.util.Set;

public class EM {

    /**
     * Valore standard suggerito per ogni progetto
     * Se necessario il singolo progetto può modificarlo nel metodo setPersistenceEntity()
     */
    public static final String DEFAULT_PERSISTENCE_UNIT = "MySqlUnit";

    public static String PERSISTENCE_UNIT = DEFAULT_PERSISTENCE_UNIT;

    private static final EntityManagerFactory EMFACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);


    public static Set<Attribute<?, ?>> getAttrs(Class<?> clazz) {
        EntityType<?> type = getEntityType(clazz);
        Set<Attribute<?, ?>> attrs = new HashSet<Attribute<?, ?>>();
        if (type != null) {
            Set<?> aSet = type.getAttributes();
            for (Object ogg : aSet) {
                if ((ogg != null) && (ogg instanceof Attribute)) {
                    Attribute<?, ?> attr = (Attribute<?, ?>) ogg;
                    attrs.add(attr);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle
        return attrs;
    }// end of method


    public static EntityType<?> getEntityType(Class<?> clazz) {
        EntityManager manager = EM.createEntityManager();
        Metamodel metamodel = manager.getMetamodel();
        Set<EntityType<?>> entities = metamodel.getEntities();
        EntityType<?> entity = null;
        for (EntityType<?> e : entities) {
            Class<?> type = e.getJavaType();
            if (type.equals(clazz)) {
                entity = e;
                break;
            }// end of if cycle
        }// end of for cycle
        manager.close();
        return entity;
    }// end of method


    /**
     * Creates a new EntityManager on the current persistence unit.
     * <p>
     *
     * @return the EntityManager
     */
    public static EntityManager createEntityManager() {
        return EMFACTORY.createEntityManager();
    }

}// end of class
