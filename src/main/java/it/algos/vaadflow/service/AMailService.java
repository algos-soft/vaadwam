package it.algos.vaadflow.service;

import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.InetAddress;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 05-nov-2018
 * Time: 06:30
 * Utility per la posta <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AMailService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;

    private JavaMailSender mailSender;


    /**
     * Spring constructor
     */
    @Autowired
    public AMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }// end of constructor


    /**
     * Spedizione senza mittente e senza destinatario
     * Aggiunge ID e nome del server <br>
     *
     * @param title soggetto
     */
    public boolean sendIP(String title) {
        return sendIP(title, "", 0);
    }// end of method


    /**
     * Spedizione senza mittente e senza destinatario
     * Aggiunge ID e nome del server <br>
     *
     * @param title soggetto
     * @param body  testo originario della mail
     */
    public boolean sendIP(String title, String body) {
        return sendIP(title, body, 0);
    }// end of method


    /**
     * Spedizione senza mittente e senza destinatario
     * Aggiunge ID e nome del server <br>
     *
     * @param title  soggetto
     * @param inizio della operazione
     */
    public boolean sendIP(String title, long inizio) {
        return sendIP(title, "", inizio);
    }// end of method


    /**
     * Spedizione senza mittente e senza destinatario
     * Aggiunge ID e nome del server <br>
     *
     * @param title  soggetto
     * @param body   testo originario della mail
     * @param inizio della operazione
     */
    public boolean sendIP(String title, String body, long inizio) {
        InetAddress inetAddress = null;
        String message = "";
        String fullMessage = "";

        try { // prova ad eseguire il codice
            inetAddress = InetAddress.getLocalHost();
            message += "IP Address:- " + inetAddress.getHostAddress();
            message += A_CAPO;
            message += "Host Name:- " + inetAddress.getHostName();
            message += A_CAPO;
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        fullMessage = text.isValid(body) ? message + body : message;
        if (inizio > 0) {
            fullMessage += A_CAPO;
            fullMessage += "In " + date.deltaText(inizio);
        }// end of if cycle

        return send(pref.getStr(MAIL_TO), title, fullMessage);
    }// end of method


    /**
     * Spedizione standard senza mittente e senza destinatario
     *
     * @param title soggetto
     * @param body  testo della mail
     */
    public boolean send(String title, String body) {
        return send(pref.getStr(MAIL_TO), title, body);
    }// end of method


    /**
     * Spedizione standard col solo destinatario
     *
     * @param to    destinatario
     * @param title soggetto
     * @param body  testo della mail
     */
    public boolean send(String to, String title, String body) {
        return send(pref.getStr(MAIL_FROM), to, title, body);
    }// end of method


    /**
     * Spedizione completa con mittente e destinatario
     *
     * @param from  mittente
     * @param to    destinatario
     * @param title soggetto
     * @param body  testo della mail
     */
    public boolean send(String from, String to, String title, String body) {
        boolean status = false;
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        try {
            if (from != null) {
                mimeMessageHelper.setFrom(from);
            }
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(body);
            mimeMessageHelper.setTo(to);
            this.mailSender.send(message);
            status = true;
        } catch (MessagingException messageException) {
            // You could also 'throw' this exception. I am not a fan of checked exceptions.
            // If you want to go that route, then just update this method and the interface.
            throw new RuntimeException(messageException);
        }// fine del blocco try-catch

        return status;
    }// end of method


}// end of class
