package it.algos.vaadwam.migration;


import javax.persistence.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Classi di tipo JavaBean
 * <p>
 * 1) le sottoclassi concrete devono avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolenai al posto di get)
 * 3) le classi deve implementare l'interfaccia Serializable (tramite questa superclasse astratta)
 * 4) le classi non devono contenere nessun metodo per la gestione degli eventi
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static Logger logger = Logger.getLogger(BaseEntity.class.getName());

    private static ArrayList<PrePersistListener> prePersistListeners = new ArrayList();

    private static ArrayList<PostPersistListener> postPersistListeners = new ArrayList();

    private static ArrayList<PostUpdateListener> postUpdateListeners = new ArrayList();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * Adds a listener invoked when the entity is persisted.
     * WARNING: ALWAYS REMOVE THE LISTENER WHEN FINISHED
     * this collection is static and holds the references forever!
     */
    public static PostPersistListener addPostPersistListener(PostPersistListener l) {
        postPersistListeners.add(l);
        return l;
    }// end of method


    public static void removePostPersistListener(PostPersistListener l) {
        postPersistListeners.remove(l);
    }// end of method


    /**
     * Adds a listener invoked when the entity is updated.
     * WARNING: ALWAYS REMOVE THE LISTENER WHEN FINISHED
     * this collection is static and holds the references forever!
     */
    public static PostUpdateListener addPostUpdateListener(PostUpdateListener l) {
        postUpdateListeners.add(l);
        return l;
    }// end of method


    public static void removePostUpdateListener(PostUpdateListener l) {
        postUpdateListeners.remove(l);
    }// end of method


    public static Object createBean(Class<?> clazz) {
        Object bean = null;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return bean;
    }// end of method


    /**
     * Returns the table name for a given entity type in the {@link EntityManager}.
     *
     * @param em
     * @param entityClass
     *
     * @return
     */
    public static <T> String getTableName(EntityManager em, Class<T> entityClass) {
        /*
         * Check if the specified class is present in the metamodel.
         * Throws IllegalArgumentException if not.
         */
        Metamodel meta = em.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        //Check whether @Table annotation is present on the class.
        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null)
                ? entityType.getName().toUpperCase()
                : t.name();
        return tableName;
    }


    protected void prePersist(Class<?> entityClass) {
        for (PrePersistListener l : prePersistListeners) {
            l.prePersist(entityClass);
        }// end of for cycle
    }// end of method


    protected void postPersist(Class<?> entityClass, long id) {
        for (PostPersistListener l : postPersistListeners) {
            l.postPersist(entityClass, id);
        }// end of for cycle
    }// end of method


    protected void postUpdate(Class<?> entityClass, long id) {
        for (PostUpdateListener l : postUpdateListeners) {
            l.postUpdate(entityClass, id);
        }// end of for cycle
    }// end of method


    protected void preRemove(Class<?> entityClass, long id) {
    }// end of method


    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }// end of method


    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }// end of method

//    /**
//     * Saves this entity to the database using a local EntityManager
//     *
//     * @return the merged Entity (new entity, unmanaged, has the id)
//     */
//    public BaseEntity save() {
//        return save((EntityManager) null);
//    }// end of method

//    /**
//     * Saves this entity to the database.
//     * <p>
//     * If the provided EntityManager has an active transaction, the operation is performed
//     * inside the transaction.<br>
//     * Otherwise, a new transaction is used to save this single entity.
//     *
//     * @param manager the entity manager to use (if null, a new one is created on the fly)
//     * @return the merged Entity (new entity, unmanaged, has the id)
//     */
//    @SuppressWarnings("rawtypes")
//    public BaseEntity save(EntityManager manager) {
//        BaseEntity mergedEntity = null;
//        boolean usaManagerLocale = false;
//        boolean createTransaction = false;
//
//        // se non specificato l'EntityManager, ne crea uno locale
//        if (manager == null) {
//            manager = EM.createEntityManager();
//            usaManagerLocale = true;
//        }// end of if cycle
//
//        //--controlla se la transazione è attiva
//        createTransaction = LibQuery.isTransactionNotActive(manager);
//
//        try {
//            if (createTransaction) {
//                manager.getTransaction().begin();
//            }// end of if cycle
//
//            mergedEntity = manager.merge(this);
//
//            if (createTransaction) {
//                manager.getTransaction().commit();
//            }// end of if cycle
//
//            // assign the new id to this entity
//            if (this.id == null) {
//                this.setId(mergedEntity.getId());
//            }// end of if cycle
//        } catch (ConstraintViolationException e) {
//            // rollback transaction
//            if (createTransaction) {
//                manager.getTransaction().rollback();
//            }// end of if cycle
//
//            // log the error
//            String violations = "";
//            for (ConstraintViolation v : e.getConstraintViolations()) {
//                if (!violations.equals("")) {
//                    violations += "\n";
//                }// end of if cycle
//                violations += "- " + v.toString();
//            }
//            logger.log(Level.SEVERE, "The record cannot be saved due to the following constraint violations:\n"
//                    + violations, e);
//        } catch (Exception e) {
//            // log the error
//            manager.getTransaction().rollback();
//            logger.log(Level.SEVERE, "The record cannot be saved ", e);
//        }// fine del blocco try-catch
//
//        // eventualmente chiude l'EntityManager locale
//        if (usaManagerLocale) {
//            manager.close();
//        }// end of if cycle
//
//        return mergedEntity;
//    }// end of method


//	/**
//	 * Refreshes the contents from the database
//	 */
//	public void refresh(){
//		EntityManager manager = EM.createEntityManager();
//		manager.merge(this);
//		//BaseEntity entity = manager.find(this.getClass(), this.getId());
//		manager.refresh(this);
//		manager.close();
//	}

//    /**
//     * Removes this entity from the database using a local EntityManager
//     *
//     * @return true if the entity was successfully removed
//     */
//    public boolean delete() {
//        return delete((EntityManager) null);
//    }// end of method


//    /**
//     * Removes this entity from the database.
//     * <p>
//     * If the provided EntityManager has an active transaction, the operation is performed
//     * inside the transaction.<br>
//     * Otherwise, a new transaction is used to delete this single entity.
//     *
//     * @param manager the entity manager to use (if null, a new one is created on the fly)
//     * @return true if the entity was successful removed
//     */
//    public boolean delete(EntityManager manager) {
//        boolean successfullyDeleted = false;
//        BaseEntity entityToBeRemoved;
//        boolean usaManagerLocale = false;
//        boolean createTransaction = false;
//
//        // se non specificato l'EntityManager, ne crea uno locale
//        if (manager == null) {
//            manager = EM.createEntityManager();
//            usaManagerLocale = true;
//        }// end of if cycle
//
//        //--controlla se la transazione è attiva
//        createTransaction = LibQuery.isTransactionNotActive(manager);
//
//        try {
//            if (createTransaction) {
//                manager.getTransaction().begin();
//            }// end of if cycle
//
//            entityToBeRemoved = manager.getReference(this.getClass(), this.getId());
//            manager.remove(entityToBeRemoved);
//
//            if (createTransaction) {
//                manager.getTransaction().commit();
//            }// end of if cycle
//
//            successfullyDeleted = true;
//        } catch (Exception e) {
//            // rollback transaction
//            if (createTransaction) {
//                manager.getTransaction().rollback();
//            }// end of if cycle
//
//            logger.log(Level.WARNING, "The record cannot be deleted ", e);
//        }// fine del blocco try-catch
//
//        // eventualmente chiude l'EntityManager locale
//        if (usaManagerLocale) {
//            manager.close();
//        }// end of if cycle
//
//        return successfullyDeleted;
//    }// end of method


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }


    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public interface PrePersistListener {

        public void prePersist(Class<?> entityClass);

    }


    public interface PostPersistListener {

        public void postPersist(Class<?> entityClass, long id);

    }


    public interface PostUpdateListener {

        public void postUpdate(Class<?> entityClass, long id);

    }

}