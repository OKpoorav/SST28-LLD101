/**
 * Single room pricing.
 */
public class SingleRoomPricing implements PricingComponent {
    @Override
    public Money getMonthlyFee() {
        return new Money(14000.0);
    }
    
    @Override
    public String getDescription() {
        return "Single Room";
    }
}
