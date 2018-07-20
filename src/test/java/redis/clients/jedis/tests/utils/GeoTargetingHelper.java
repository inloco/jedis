package redis.clients.jedis.tests.utils;

import redis.clients.jedis.Jedis;

public class GeoTargetingHelper {
    private String host;
    private int port;
    private Jedis jedis;

    public GeoTargetingHelper(String host, int port, Jedis jedis){
        this.host = host;
        this.port = port;
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

