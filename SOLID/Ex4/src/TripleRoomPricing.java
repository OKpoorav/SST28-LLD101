/**
 * Triple room pricing.
 */
public class TripleRoomPricing implements PricingComponent {
    @Override
    public Money getMonthlyFee() {
        return new Money(12000.0);
    }
    
    @Override
    public String getDescription() {
        return "Triple Room";
    }
}
