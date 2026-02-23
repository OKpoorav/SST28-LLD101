
public interface DiscountPolicy {
  
    double calculateDiscount(String customerType, double subtotal, int itemCount);
}
