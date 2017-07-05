package seng302.team18.test_mock.ac35_xml_encoding;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35XMLBoatMessage;

import javax.xml.transform.dom.DOMSource;

import static org.junit.Assert.*;

/**
 * Created by afj19 on 1/07/17.
 */
public class BoatsXmlEncoderTest {

    private DOMSource domSource;
    private AC35XMLBoatMessage message;


    private void setUpMessage() {

    }

    @Before
    public void setUp() throws Exception {
        setUpMessage();
        domSource = (new BoatsXmlEncoder()).getDomSource(message);
    }

    @Test
    public void encodeModifiedTimeTest() throws Exception {

    }

    @Test
    public void encodeVersionTest() throws Exception {


    }

    @Test
    public void encodeSettingsTest_raceBoatType() throws Exception {


    }

    @Test
    public void encodeSettingsTest_boatDimension() throws Exception {


    }

    @Test
    public void encodeSettingsTest_ZoneSize() throws Exception {


    }

    @Test
    public void encodeSettingsTest_ZoneLimits() throws Exception {


    }

    @Test
    public void encodeBoatTest_type() throws Exception {


    }

    @Test
    public void encodeBoatTest_sourceId() throws Exception {


    }

    @Test
    public void encodeBoatTest_hullNumber() throws Exception {


    }

    @Test
    public void encodeBoatTest_stoweName() throws Exception {


    }

    @Test
    public void encodeBoatTest_shortName() throws Exception {


    }

    @Test
    public void encodeBoatTest_boatName() throws Exception {


    }

    @Test
    public void encodeBoatTest_gps() throws Exception {


    }

    @Test
    public void encodeBoatTest_flag() throws Exception {


    }
}