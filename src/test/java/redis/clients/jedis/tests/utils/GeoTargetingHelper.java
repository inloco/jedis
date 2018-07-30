package redis.clients.jedis.tests.utils;

import redis.clients.jedis.Jedis;

public class GeoTargetingHelper {
    private Jedis jedis;

    public GeoTargetingHelper(Jedis jedis){
        this.jedis = jedis;
    }

    public String addGeoTargeting(String geoTargeting) {
        String[] args = geoTargeting.split(" ");
        String bucket = args[0];
        long geotargetingId = Integer.valueOf(args[1]);
        double latitude = Double.valueOf(args[2]);
        double longitude = Double.valueOf(args[3]);
        int radius = Integer.valueOf(args[4]);

        return jedis.calcGeotargeting(bucket, geotargetingId, latitude, longitude, radius);
    }
}

