package it.algos.vaadwam.migration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: gio, 17-mag-2018
 * Time: 11:57
 */
@Slf4j
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AmbEntity {

    protected final static String SEP = "'";

    protected final static String MY_URL_BASE = "jdbc:mysql://localhost:3306/amb";

    protected final static String MY_SSL = "?useSSL=false";

    protected final static String MY_TIME = "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    protected final static String MY_URL = MY_URL_BASE + MY_SSL + MY_TIME;

    @Value("${wam.mysql.user}")
    private String mysqlUser;

    @Value("${wam.mysql.password}")
    private String mysqlPassword;

    protected String myDriver = "com.mysql.jdbc.Driver";


    public AmbEntity find(String dbName, String whereText) {
        String myUrl;
        AmbEntity entity = null;
        String where = " WHERE " + whereText;

        try { // prova ad eseguire il codice
            Connection connection=getMysqlConnection();
            String query = "SELECT * FROM " + dbName + where;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                entity = singoloRS(resultSet);
            }// end of if cycle

            statement.close();
            connection.close();
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore);
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method


    public AmbEntity findByID(String dbName, long keyID) {
        AmbEntity entity = null;
        String where = " WHERE id=" + keyID;

        try { // prova ad eseguire il codice
            Connection connection=getMysqlConnection();
            String query = "SELECT * FROM " + dbName + where;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                entity = singoloRS(resultSet);
            }// end of if cycle

            statement.close();
            connection.close();
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore);
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return entity;
    }// end of method


    public List findAll(String dbName) {
        return findAll(dbName, "", "");
    }// end of method


    public List findAll(String dbName, int codeCroce) {
        return findAll(dbName, "croce_id=" + codeCroce, "");
    }// end of method


    public List findAll(String dbName, int codeCroce, String orderText) {
        return findAll(dbName, "croce_id=" + codeCroce, orderText);
    }// end of method


    public List findAll(String dbName, String whereText) {
        return findAll(dbName, whereText, "");
    }// end of method


//    public List findAll(String dbName, String whereText) {
//        return findAll(dbName, whereText + "");
//    }// end of method


    public List findAll(String dbName, String whereText, String orderText)  {
        List lista = new ArrayList<>();
        String where = whereText.equals("") ? "" : " WHERE " + whereText;
        String order = orderText.equals("") ? "" : " ORDER BY " + orderText;

        try { // prova ad eseguire il codice
            Connection connection=getMysqlConnection();
            String query = "SELECT * FROM " + dbName + where + order;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.isBeforeFirst()) {
                lista = this.spazzolaResultSet(resultSet);
            }// end of if cycle

            statement.close();
            connection.close();
        } catch (Exception unErrore) { // intercetta l'errore
            log.error("errore di connessione",unErrore);
        }// fine del blocco try-catch

        return lista;
    }// end of method


    protected List spazzolaResultSet(ResultSet rs) {
        List resultList = new ArrayList<>();

        try { // prova ad eseguire il codice
            while (rs.next()) {
                resultList.add(singoloRS(rs));
            }// end of while cycle

        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return resultList;
    }// end of method


    protected AmbEntity singoloRS(ResultSet rs) {
        return null;
    }// end of method

    /**
     * Returns a new connection to MySql
     */
    private Connection getMysqlConnection() throws SQLException {
        String user = "root";
        String password="";

        if(!StringUtils.isEmpty(mysqlUser)) {
            user=mysqlUser;
        }

        if(!StringUtils.isEmpty(mysqlPassword)) {
            password=mysqlPassword;
        }

        Connection connection = DriverManager.getConnection(MY_URL, user, password);
        return connection;
    }

}// end of class
