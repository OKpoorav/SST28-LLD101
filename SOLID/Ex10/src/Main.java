public class Main {
    public static void main(String[] args) {
        System.out.println("=== Transport Booking ===");

        DistanceService distance = new DistanceCalculator();
        AllocationService allocator = new DriverAllocator();
        PaymentService payment = new PaymentGateway();

        TransportBookingService svc = new TransportBookingService(distance, allocator, payment);

        TripRequest req = new TripRequest("23BCS1010", new GeoPoint(12.97, 77.59), new GeoPoint(12.93, 77.62));
        svc.book(req);

        System.out.println("\n=== Mock Booking (Stretch Goal) ===");

        DistanceService mockDistance = new MockDistanceService();
        AllocationService mockAllocator = new MockAllocationService();
        PaymentService mockPayment = new MockPaymentService();

        TransportBookingService mockSvc = new TransportBookingService(mockDistance, mockAllocator, mockPayment);
        mockSvc.book(req);
    }
}
