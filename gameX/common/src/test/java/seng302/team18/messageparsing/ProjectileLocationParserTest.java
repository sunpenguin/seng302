package seng302.team18.messageparsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerType;
import seng302.team18.message.PowerUpMessage;
import seng302.team18.message.ProjectileLocationMessage;

/**
 * Created by cslaven on 7/09/17.
 */
public class ProjectileLocationParserTest {


        private byte[] message;
        private ProjectileLocationMessage expected;
        private MessageBodyParser parser;


        @Before
        public void setUp() {
            parser = new ProjectileParser();
            this.message = new byte[] { 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -24, 3, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0 };
            expected = new ProjectileLocationMessage(1, 5, 5, 5, 5);
        }


        @Test
        public void parseTest() {
            ProjectileLocationMessage actual = (ProjectileLocationMessage) parser.parse(message);
            Assert.assertEquals(expected, actual);
        }


}

