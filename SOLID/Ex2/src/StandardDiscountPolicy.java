
public class StandardDiscountPolicy implements DiscountPolicy {
    
    @Override
    public double calculateDiscount(String customerType, double subtotal, int itemCount) {
        if ("student".equalsIgnoreCase(customerType)) {
            if (subtotal >= 180.0) return 10.0;
            return 0.0;
        }
        if ("staff".equalsIgnoreCase(customerType)) {
            if (itemCount >= 3) return 15.0;
            return 5.0;
        }
        return 0.0;
    }
}
