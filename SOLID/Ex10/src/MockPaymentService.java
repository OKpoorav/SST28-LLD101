public class MockPaymentService implements PaymentService {
    public String charge(String studentId, double amount) {
        return "MOCK-TXN-0000";
    }
}
