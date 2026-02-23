/**
 * Double room pricing.
 */
public class DoubleRoomPricing implements PricingComponent {
    @Override
    public Money getMonthlyFee() {
        return new Money(15000.0);
    }
    
    @Override
    public String getDescription() {
        return "Double Room";
    }
}
