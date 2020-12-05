package it.algos.vaadwam;

import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwam.enumeration.Festivi;
import it.algos.vaadwam.modules.turno.NuoviTurniService;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: ven, 04-dic-2020
 * Time: 21:13
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("NuoviTurniServiceTest")
@DisplayName("Test di unit")
public class NuoviTurniTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    NuoviTurniService service;

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    ADateService date;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        Assertions.assertNotNull(service);
        MockitoAnnotations.initMocks(date);
        Assertions.assertNotNull(date);
        service.text = text;
        service.array = array;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
    }

    @Test
    @DisplayName("Primo test")
    void all2021() {
        List<Integer> lista;
        lista = Festivi.all2021();
        Assert.assertNotNull(lista);

        for (int anno : lista) {
            System.out.println(anno);
        }
    }

    @Test
    @DisplayName("Secondo test")
    void festivi2021() {
        List<LocalDate> lista;
        lista = Festivi.festivi2021();
        Assert.assertNotNull(lista);

        for (LocalDate giorno : lista) {
            System.out.println(date.getDataCompleta(giorno));
        }
    }


    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterEach
    void tearDownAll() {
    }

}