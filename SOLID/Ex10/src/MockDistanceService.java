public class MockDistanceService implements DistanceService {
    public double km(GeoPoint a, GeoPoint b) {
        return 10.0;
    }
}
