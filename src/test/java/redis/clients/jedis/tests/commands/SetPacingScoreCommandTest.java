package redis.clients.jedis.tests.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.tests.utils.GeoTargetingHelper;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SetPacingScoreCommandTest extends JedisCommandTestBase {
    private static final String HOST = "localhost";
    private static final Integer PORT = 6379;
    private static final String BUCKET = "bucket";

    private Jedis jedis;

    private static final String[] geoTargetings = new String[] {
            BUCKET + " 100 -19.769776 -43.954117 2000",
            BUCKET + " 200 -19.757717 -43.962016 2000",
            BUCKET + " 300 -19.745828 -43.93678 2000",
    };

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
    public void updateValueOnExistentTargetings() {
        Set<String> result = new HashSet<>(jedis.getGeotargetings(BUCKET, -19.75644, -43.94415));
        Set<String> expected = new HashSet<String>() {{
            add("100 -19.769776 -43.954117 2000 100");
            add("200 -19.757717 -43.962016 2000 100");
            add("300 -19.745828 -43.93678 2000 100");
        }};
        assertEquals(expected, result);

        jedis.setPacingScore(BUCKET, 100, (short)10);
        jedis.setPacingScore(BUCKET, 200, (short)20);
        jedis.setPacingScore(BUCKET, 300, (short)30);

        result = new HashSet<>(jedis.getGeotargetings(BUCKET, -19.75644, -43.94415));
        Set<String> afterSettingPaceExpected = new HashSet<String>() {{
            add("100 -19.769776 -43.954117 2000 10");
            add("200 -19.757717 -43.962016 2000 20");
            add("300 -19.745828 -43.93678 2000 30");
        }};
        assertEquals(afterSettingPaceExpected, result);
    }

    @Test(expected = JedisDataException.class)
    public void updateOnNonExistentItems() {
        jedis.setPacingScore(BUCKET, 666, (short)10);
    }
}
