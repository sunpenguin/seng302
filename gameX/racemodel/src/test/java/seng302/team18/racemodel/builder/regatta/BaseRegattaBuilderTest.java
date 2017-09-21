package seng302.team18.racemodel.builder.regatta;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.Regatta;

import static org.junit.Assert.assertEquals;


public class BaseRegattaBuilderTest {

    private static final int REGATTA_ID = 1234;
    private static final String REGATTA_NAME = "this regatta";

    private Regatta regatta;


    @Before
    public void setUp() throws Exception {
        AbstractRegattaBuilder regattaBuilder = new ConcreteRegattaBuilder();
        regatta = regattaBuilder.buildRegatta();
    }


    @Test
    public void buildRegattaTest_id() throws Exception {
        assertEquals(REGATTA_ID, regatta.getRegattaID());
    }


    @Test
    public void buildRegattaTest_name() throws Exception {
        assertEquals(REGATTA_NAME, regatta.getName());
    }


    private class ConcreteRegattaBuilder extends AbstractRegattaBuilder {
        @Override
        protected int getRegattaId() {
            return REGATTA_ID;
        }

        @Override
        protected String getRegattaName() {
            return REGATTA_NAME;
        }
    }

}