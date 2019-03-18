package redis.clients.jedis.tests.commands;

import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.tests.utils.GeoTargetingHelper;

public class GetAllGeotargetingsCommandTest extends JedisCommandTestBase {
    private static final String HOST = "localhost";
    private static final Integer PORT = 6330;
    private static final String BUCKET = "bucket";

    private Jedis jedis;
    private JedisPool jedisPool;
    private GeoTargetingHelper calcGeoTargetingHelper;

    private static final String[] geoTargetings = new String[] {
        BUCKET + " 100 -19.769776 -43.954117 2000 100",
        BUCKET + " 200 -21.757717 -33.962016 2000 100",
        BUCKET + " 300 -31.745828 -22.93678 2000 100",
    };

    @Before
    public void setUp() {
        jedisPool = new JedisPool(new JedisPoolConfig(), HOST, PORT);
        jedis = jedisPool.getResource();
        jedis.flushAll();
        calcGeoTargetingHelper = new GeoTargetingHelper(jedis);

        for (String geoTargeting: geoTargetings) {
            assertEquals("OK", calcGeoTargetingHelper.addGeoTargeting(geoTargeting));
        }
    }

    @After
    public void tearDown() {
        jedis.disconnect();
    }

    @Test
    public void checkGetAllGeoTargetings() {
        Set<String> result = new HashSet<>(jedis.getAllGeotargetings(BUCKET));
        Set<String> expected = new HashSet<String>(){{
            for(String geotargeting : geoTargetings){
                add(getGeoTargetingWithoutBucket(geotargeting));
            }
        }};

        assertEquals(expected, result);
    }

    private String getGeoTargetingWithoutBucket(String geoTargeting) {
        String[] args = geoTargeting.split(" ");
        Long geotargetingId = Long.parseLong(args[1]);
        Double latitude = Double.parseDouble(args[2]);
        Double longitude = Double.parseDouble(args[3]);
        Integer radius = Integer.parseInt(args[4]);
        Integer score = Integer.parseInt(args[5]);
        return geotargetingId + " " + latitude + " " + longitude + " " + radius + " " + score;
    }
}
