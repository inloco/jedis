package redis.clients.jedis.tests.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;
import redis.clients.jedis.tests.utils.GeoTargetingHelper;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GetGeotargetingsCommandTest extends JedisCommandTestBase {
    private static final String HOST = "localhost";
    private static final Integer PORT = 6379;
    private static final String BUCKET = "bucket";

    private Jedis jedis;

    private static final String[] geoTargetings = new String[] {
            BUCKET + " 3471 -19.769776 -43.954117 2000",
            BUCKET + " 3472 -19.777717 -43.982016 2000",
            BUCKET + " 3473 -19.745828 -43.93678 2000",
            BUCKET + " 3474 -19.155954 -45.449168 2000",
            BUCKET + " 3475 -18.487074 -47.402992 2000",
            BUCKET + " 3476 -21.768856 -42.534428 2000",
            BUCKET + " 3477 -21.011259 -42.837521 2000",
            BUCKET + " 3478 -18.474505 -42.308141 2000",
            BUCKET + " 3479 -18.823608 -42.705867 2000",
            BUCKET + " 3470 -22.332888 -45.09229 2000",
            BUCKET + " 3416 -16.804675 -42.341585 2000",
            BUCKET + " 3426 -20.866416 -42.245419 2000",
            BUCKET + " 3436 -20.036476 -42.26668 2000",
            BUCKET + " 3446 -19.66699 -48.308671 2000",
            BUCKET + " 3456 -17.397881 -42.731132 2000",
            BUCKET + " 3466 -15.588396 -43.612954 2000 ",
            BUCKET + " 3476 -17.98742 -46.907232 2000 ",
            BUCKET + " 3486 -15.702906 -44.028745 2000",
            BUCKET + " 3496 -17.597964 -44.730935 2000",
            BUCKET + " 3406 -17.592505 -44.734022 2000",
            BUCKET + " 3176 -21.553928 -45.439342 2000",
            BUCKET + " 3276 -15.404546 -42.309829 2000",
            BUCKET + " 3376 -20.329418 -46.367387 2000",
            BUCKET + " 4100 -16.4932162 -39.071703 10000",
            BUCKET + " 4101 -9.0147345 -42.688726 10000",
            BUCKET + " 4002 -12.934989 -38.3925794 10000",
            BUCKET + " 4102 -12.983474 -38.5129732 10000",
            BUCKET + " 4103 -23.0326476 -45.5609444 10000",
            BUCKET + " 4104 -12.5776839 -38.0070462 10000",
            BUCKET + " 4000 -12.9371574 -38.4104438 10000",
            BUCKET + " 4105 -13.0127006 -38.4824597 10000",
            BUCKET + " 4004 -12.9817592 -38.4642495 10000"
    };

    @Before
    public void saveGeoTargetings() {
   }

    @Before
    public void setUp() {
        jedis = new Jedis(HOST, PORT);
        GeoTargetingHelper calcGeoTargetingHelper = new GeoTargetingHelper(HOST, PORT, jedis);

        for (String geoTargeting: geoTargetings) {
            assertEquals("OK", calcGeoTargetingHelper.addGeoTargeting(geoTargeting));
        }
    }

    @After
    public void tearDown() {
        jedis.disconnect();
    }

    @Test
    public void firstQuery() {
        Set<String> result = new HashSet<>(jedis.getGeotargetings(BUCKET, -19.75644, -43.94415));
        Set<String> expected = new HashSet<String>() {{
            add("3473 -19.745828 -43.93678 2000 100");
            add("3471 -19.769776 -43.954117 2000 100");
        }};
        assertEquals(expected, result);
    }

    @Test
    public void secondQuery() {
        Set<String> result = new HashSet<>(jedis.getGeotargetings(BUCKET, -19.77369, -43.96599));
        Set<String> expected = new HashSet<String>() {{
            add("3471 -19.769776 -43.954117 2000 100");
            add("3472 -19.777717 -43.982016 2000 100");
        }};
        assertEquals(expected, result);
    }

    @Test
    public void thirdQuery() {
        Set<String> result = new HashSet<>(jedis.getGeotargetings(BUCKET, -12.93715, -38.41044));
        Set<String> expected = new HashSet<String>() {{
            add("4004 -12.981759 -38.46425 10000 100");
            add("4002 -12.934989 -38.392579 10000 100");
            add("4000 -12.937157 -38.410444 10000 100");
        }};
        assertEquals(expected, result);
    }
}
