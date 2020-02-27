package it.algos.vaadwam.integration;

import it.algos.vaadwam.ATest;
import it.algos.vaadwam.WamApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * Project vaadwam
 * Created by Algos
 * User: gac
 * Date: mer, 26-feb-2020
 * Time: 22:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WamApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MiliteServizioTest extends ATest {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ApplicationContext appContext;


    /**
     * Viene eseguito PRIMA di OGNI metodo di test <br>
     */
    @Before
    public void setUp() {
        super.setUpTest();

        Assert.assertNotNull(appContext);
    }// end of method


    /**
     * @return lista di code
     */
    @Test
    public void findAllCode() {
        previstoList = new ArrayList<>();
        previstoList.add("vol");
        previstoList.add("vol2");
        previstoList.add("tir");
        previstoList.add("tut");

        ottenutoList = funzioneService.findAllCode(croceGaps);
        Assert.assertEquals(previstoList, ottenutoList);
    }// end of single test


    /**
     * Costruisce una lista ordinata di funzioni <br>
     * Le funzioni sono memorizzate come Set <br>
     * Le funzioni non sono embedded nel milite ma sono un riferimento dinamico CON @DBRef <br>
     * Nella lista risultante, vengono ordinate secondo la property 'ordine' <br>
     *
     * @return lista ordinata di funzioni
     */
    @Test
    public void getListaFunzioni() {
        listaFunzioniPreviste = new ArrayList<>();

        listaFunzioniPreviste = new ArrayList<>();
        listaFunzioniPreviste.add(funzioneVol);
        listaFunzioniPreviste.add(funzioneVol2);
        listaFunzioniOttenute = militeService.getListaFunzioni(militeCeresa);
        Assert.assertEquals(listaFunzioniPreviste, listaFunzioniOttenute);

        listaFunzioniPreviste = new ArrayList<>();
        listaFunzioniPreviste.add(funzioneAutAmb);
        listaFunzioniPreviste.add(funzionePriAmb);
        listaFunzioniPreviste.add(funzioneSecAmb);
        listaFunzioniPreviste.add(funzioneTerAmb);
        listaFunzioniOttenute = militeService.getListaFunzioni(militeAllegri);
        Assert.assertEquals(listaFunzioniPreviste, listaFunzioniOttenute);
    }// end of single test

}// end of class
