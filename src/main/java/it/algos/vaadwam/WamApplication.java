package it.algos.vaadwam;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import it.algos.vaadflow.annotation.AIScript;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 8-mag-2018
 * <p>
 * Spring boot web application initializer.
 * The entry point of the Spring Boot application.
 * <p>
 * Questa classe contiene il metodo 'main' che è il punto di ingresso dell'applicazione Java
 * In fase di sviluppo si possono avere diverse configurazioni, ognuna delle quali punta un ''main' diverso
 * Nel JAR finale (runtime) si può avere una sola classe col metodo 'main'.
 * Nel WAR finale (runtime) occorre (credo) inserire dei servlet di context diversi
 * Senza @ComponentScan, SpringBoot non 'vede' le classi con @SpringView
 * che sono in una directory diversa da questo package
 * <p>
 * Questa classe non fa praticamente niente se non avere le Annotation riportate qui
 * Annotated with @SpringBootApplication (obbligatorio)
 * Annotated with @ComponentScan (obbligatorio, se non già specificato il path in @SpringBootApplication)
 * Annotated with @EntityScan (obbligatorio, se non già specificato il path in @SpringBootApplication)
 * Annotated with @EnableMongoRepositories (obbligatorio, se non già specificato il path in @SpringBootApplication)
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * <p>
 * Tutte le view devono essere comprese nel path di questa classe (directory interne incluse)
 * Una sola view può avere @Route("")
 * The @SpringBootApplication annotation is equivalent to using @Configuration, @EnableAutoConfiguration and @ComponentScan with their default attributes:
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script di algos <br>
 */
@SpringBootApplication(scanBasePackages = {"it.algos.vaadflow", "it.algos.vaadwam"})
@EnableVaadin({
        "it.algos.vaadflow.modules",
        "it.algos.vaadflow.wizard",
        "it.algos.vaadflow.developer",
        "it.algos.vaadflow.ui",
        "it.algos.vaadwam"
})
@EntityScan({"it.algos.vaadflow.modules", "it.algos.vaadwam.modules"})
@EnableMongoRepositories({"it.algos"})
@AIScript(sovrascrivibile = false)
public class WamApplication extends SpringBootServletInitializer {

    /**
     * Constructor
     *
     * @param args eventuali parametri in ingresso
     */
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WamApplication.class, args);
    }// end of constructor


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(WamApplication.class);
    }// end of method

}// end of main class