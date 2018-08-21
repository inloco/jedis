package redis.clients.jedis.tests.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.tests.utils.GeoTargetingHelper;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class DeleteGeotargetingCommandTest extends JedisCommandTestBase {
    private static final String HOST = "localhost";
    private static final Integer PORT = 6330;
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
        jedis.flushAll();
        GeoTargetingHelper calcGeoTargetingHelper = new GeoTargetingHelper(jedis);

        for (String geoTargeting: geoTargetings) {
            assertEquals("OK", calcGeoTargetingHelper.addGeoTargeting(geoTargeting));
        }
    }

    @After
    public void tearDown() {
        jedis.disconnect();
    }

    @Test
    public void checkDeletedGeoTargetings() {
        Set<String> result = new HashSet<>(jedis.getGeotargetings(BUCKET, -19.75644, -43.94415));
        Set<String> expected = new HashSet<String>() {{
            add("100 -19.769776 -43.954117 2000 100");
            add("200 -19.757717 -43.962016 2000 100");
            add("300 -19.745828 -43.93678 2000 100");
        }};
        assertEquals(expected, result);

        jedis.deleteGeotargeting(BUCKET, 100);
        jedis.deleteGeotargeting(BUCKET, 200);

        result = new HashSet<>(jedis.getGeotargetings(BUCKET, -19.75644, -43.94415));
        Set<String> afterDeletionExpected = new HashSet<String>() {{
            add("300 -19.745828 -43.93678 2000 100");
        }};
        assertEquals(afterDeletionExpected, result);
    }

    @Test
    public void deleteNonExistentItem() {
        assertEquals("OK", jedis.deleteGeotargeting(BUCKET, 666));
    }
}
