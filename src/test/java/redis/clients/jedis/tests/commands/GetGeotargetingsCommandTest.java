package redis.clients.jedis.tests.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GetGeotargetingsCommandTest extends JedisCommandTestBase {
    private static final String HOST = "localhost";
    private static final Integer PORT = 6330;
    private static final String BUCKET = "bucket";

    private Jedis jedis;

    private static final String[] geoTargetings = new String[] {
            BUCKET + " 5ab02d2bda651c000c003471 5ab02d2bda651c000c00344e AD_ID -19.769776 -43.954117 2000",
            BUCKET + " 5ab02d2bda651c000c003472 5ab02d2bda651c000c00344e AD_ID -19.777717 -43.982016 2000",
            BUCKET + " 5ab02d2bda651c000c003473 5ab02d2bda651c000c00344e AD_ID -19.745828 -43.93678 2000",
            BUCKET + " 5ab02d2bda651c000c003474 5ab02d2bda651c000c00344e AD_ID -19.155954 -45.449168 2000",
            BUCKET + " 5ab02d2bda651c000c003475 5ab02d2bda651c000c00344e AD_ID -18.487074 -47.402992 2000",
            BUCKET + " 5ab02d2bda651c000c003476 5ab02d2bda651c000c00344e AD_ID -21.768856 -42.534428 2000",
            BUCKET + " 5ab02d2bda651c000c003477 5ab02d2bda651c000c00344e AD_ID -21.011259 -42.837521 2000",
            BUCKET + " 5ab02d2bda651c000c003478 5ab02d2bda651c000c00344e AD_ID -18.474505 -42.308141 2000",
            BUCKET + " 5ab02d2bda651c000c003479 5ab02d2bda651c000c00344e AD_ID -18.823608 -42.705867 2000",
            BUCKET + " 5ab02d2bda651c000c003470 5ab02d2bda651c000c00344e AD_ID -22.332888 -45.09229 2000",
            BUCKET + " 5ab02d2bda651c000c003416 5ab02d2bda651c000c00344e AD_ID -16.804675 -42.341585 2000",
            BUCKET + " 5ab02d2bda651c000c003426 5ab02d2bda651c000c00344e AD_ID -20.866416 -42.245419 2000",
            BUCKET + " 5ab02d2bda651c000c003436 5ab02d2bda651c000c00344e AD_ID -20.036476 -42.26668 2000",
            BUCKET + " 5ab02d2bda651c000c003446 5ab02d2bda651c000c00344e AD_ID -19.66699 -48.308671 2000",
            BUCKET + " 5ab02d2bda651c000c003456 5ab02d2bda651c000c00344e AD_ID -17.397881 -42.731132 2000",
            BUCKET + " 5ab02d2bda651c000c003466 5ab02d2bda651c000c00344e AD_ID -15.588396 -43.612954 2000",
            BUCKET + " 5ab02d2bda651c000c003476 5ab02d2bda651c000c00344e AD_ID -17.98742 -46.907232 2000",
            BUCKET + " 5ab02d2bda651c000c003486 5ab02d2bda651c000c00344e AD_ID -15.702906 -44.028745 2000",
            BUCKET + " 5ab02d2bda651c000c003496 5ab02d2bda651c000c00344e AD_ID -17.597964 -44.730935 2000",
            BUCKET + " 5ab02d2bda651c000c003406 5ab02d2bda651c000c00344e AD_ID -17.592505 -44.734022 2000",
            BUCKET + " 5ab02d2bda651c000c003176 5ab02d2bda651c000c00344e AD_ID -21.553928 -45.439342 2000",
            BUCKET + " 5ab02d2bda651c000c003276 5ab02d2bda651c000c00344e AD_ID -15.404546 -42.309829 2000",
            BUCKET + " 5ab02d2bda651c000c003376 5ab02d2bda651c000c00344e AD_ID -20.329418 -46.367387 2000",
            BUCKET + " 5ac404323b3cb4000c00eace 5ac4042f3b3cb4000c00e9b7 AD_ID -16.4932162 -39.071703 10000",
            BUCKET + " 5ac404323b3cb4000c00ead0 5ac4042f3b3cb4000c00e9b7 AD_ID -9.0147345 -42.688726 10000",
            BUCKET + " 5ac404323b3cb4000c00ead2 5ac4042f3b3cb4000c00e9b7 AD_ID -12.934989 -38.3925794 10000",
            BUCKET + " 5ac404323b3cb4000c00ead4 5ac4042f3b3cb4000c00e9b7 AD_ID -12.983474 -38.5129732 10000",
            BUCKET + " 5ac404323b3cb4000c00ead6 5ac4042f3b3cb4000c00e9b7 AD_ID -23.0326476 -45.5609444 10000",
            BUCKET + " 5ac404343b3cb4000c00ec9d 5ac404343b3cb4000c00ec9e AD_ID -12.5776839 -38.0070462 10000",
            BUCKET + " 5ac404343b3cb4000c00eca0 5ac404343b3cb4000c00ec9e AD_ID -12.9371574 -38.4104438 10000",
            BUCKET + " 5ac404343b3cb4000c00eca2 5ac404343b3cb4000c00ec9e AD_ID -13.0127006 -38.4824597 10000",
            BUCKET + " 5ac404343b3cb4000c00eca4 5ac404343b3cb4000c00ec9e AD_ID -12.9817592 -38.4642495 10000"
    };

    @Before
    public void saveGeoTargetings() {
        try (TestGeotargetingClient testGeotargetingClient = new TestGeotargetingClient(HOST, PORT)) {
            for (String geoTargeting: geoTargetings) {
                assertEquals("OK", testGeotargetingClient.addGeoTargeting(geoTargeting));
            }
        }
    }

    @Before
    public void setUp() {
        jedis = new Jedis(HOST, PORT);
    }

    @After
    public void tearDown() {
        jedis.disconnect();
    }

    @Test
    public void firstQuery() {
        Set<String> result = new HashSet<>(jedis.getGeotargetings(BUCKET, -19.75644, -43.94415));
        Set<String> expected = new HashSet<String>() {{
            add("5ab02d2bda651c000c003473 5ab02d2bda651c000c00344e AD_ID -19.745828 -43.93678 2000");
            add("5ab02d2bda651c000c003471 5ab02d2bda651c000c00344e AD_ID -19.769776 -43.954117 2000");
        }};
        assertEquals(expected, result);
    }

    @Test
    public void secondQuery() {
        Set<String> result = new HashSet<>(jedis.getGeotargetings(BUCKET, -19.77369, -43.96599));
        Set<String> expected = new HashSet<String>() {{
            add("5ab02d2bda651c000c003471 5ab02d2bda651c000c00344e AD_ID -19.769776 -43.954117 2000");
            add("5ab02d2bda651c000c003472 5ab02d2bda651c000c00344e AD_ID -19.777717 -43.982016 2000");
        }};
        assertEquals(expected, result);
    }

    @Test
    public void thirdQuery() {
        Set<String> result = new HashSet<>(jedis.getGeotargetings(BUCKET, -12.93715, -38.41044));
        Set<String> expected = new HashSet<String>() {{
            add("5ac404343b3cb4000c00eca4 5ac404343b3cb4000c00ec9e AD_ID -12.9817592 -38.4642495 10000");
            add("5ac404323b3cb4000c00ead2 5ac4042f3b3cb4000c00e9b7 AD_ID -12.934989 -38.3925794 10000");
            add("5ac404343b3cb4000c00eca0 5ac404343b3cb4000c00ec9e AD_ID -12.9371574 -38.4104438 10000");
        }};
        assertEquals(expected, result);
    }

    private static class TestGeotargetingClient extends Connection {
        private TestGeotargetingClient(String host, Integer port) {
            super(host, port);
        }

        String addGeoTargeting(String geoTargeting) {
            sendCommand(Protocol.Command.CALCGEOTARGETING, geoTargeting.split(" "));
            return getStatusCodeReply();
        }
    }
}
