package redis.clients.jedis.tests.utils;

import java.util.ArrayList;
import java.util.List;
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

    public List<String> getAllGeotargetings(String bucket) {
        List<String> returnedGeotargetings = jedis.getAllGeotargetings(bucket);
        List<String> geotargetings = new ArrayList<>();

        for (String geotargeting : returnedGeotargetings) {
            String[] args = geotargeting.split(" ");
            Long geotargetingId = Long.parseLong(args[0]);
            Double latitude = Double.parseDouble(args[1]);
            Double longitude = Double.parseDouble(args[2]);
            Integer radius = Integer.parseInt(args[3]);
            geotargetings.add(geotargetingId + " " + latitude + " " + longitude + " " + radius);
        }

        return geotargetings;
    }
}

