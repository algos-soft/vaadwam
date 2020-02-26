package it.algos.vaadwam.integration;

import it.algos.vaadwam.ATest;
import it.algos.vaadwam.WamApplication;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.funzione.Funzione;
import it.algos.vaadwam.modules.funzione.FunzioneService;
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
import java.util.List;
import java.util.Set;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 20-ott-2019
 * Time: 21:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WamApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FunzioneServiceTest extends ATest {


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
     * Lista di ID delle funzioni di un set <br>
     * NON ordinati <br>
     *
     * @return lista IDs delle funzioni del set
     */
    @Test
    public void getIdsFunzioni() {
        Set<Funzione> setSorgente;

        previstoList = new ArrayList<>();
        previstoList.add("gapsVol2");
        previstoList.add("gapsTir");
        setSorgente = funzioneVol.dipendenti;
        ottenutoList = funzioneService.getIdsFunzioni(setSorgente);
        Assert.assertEquals(previstoList, ottenutoList);

        previstoList = new ArrayList<>();
        previstoList.add("crfPri-amb");
        previstoList.add("crfSec-amb");
        previstoList.add("crfTer-amb");
        setSorgente = funzioneAutAmb.dipendenti;
        ottenutoList = funzioneService.getIdsFunzioni(setSorgente);
        Assert.assertEquals(previstoList, ottenutoList);

        previstoList = new ArrayList<>();
        previstoList.add("crfSec-msa");
        setSorgente = funzionePriMsa.dipendenti;
        ottenutoList = funzioneService.getIdsFunzioni(setSorgente);
        Assert.assertEquals(previstoList, ottenutoList);
    }// end of single test


    /**
     * Costruisce una lista ordinata di funzioni dipendenti <br>
     * Le funzioni sono memorizzate come Set <br>
     * La lista risultante è composta dalle funzioni originarie e non da quelle embedded nella funzione stessa <br>
     * Nella lista risultante, vengono ordinate secondo la property 'ordine' <br>
     *
     * @return lista ordinata di funzioni dipendenti
     */
    @Test
    public void getDipendenti() {
        listaFunzioniPreviste = new ArrayList<>();
        listaFunzioniPreviste.add(funzioneVol2);
        listaFunzioniPreviste.add(funzioneTir);
        listaFunzioniOttenute = funzioneService.getDipendenti(funzioneVol);
        Assert.assertEquals(listaFunzioniPreviste, listaFunzioniOttenute);

        listaFunzioniPreviste = new ArrayList<>();
        listaFunzioniPreviste.add(funzionePriAmb);
        listaFunzioniPreviste.add(funzioneSecAmb);
        listaFunzioniPreviste.add(funzioneTerAmb);
        listaFunzioniOttenute = funzioneService.getDipendenti(funzioneAutAmb);
        Assert.assertEquals(listaFunzioniPreviste, listaFunzioniOttenute);

    }// end of single test

}// end of class
