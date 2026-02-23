/**
 * Deluxe room pricing.
 */
public class DeluxeRoomPricing implements PricingComponent {
    @Override
    public Money getMonthlyFee() {
        return new Money(16000.0);
    }
    
    @Override
    public String getDescription() {
        return "Deluxe Room";
    }
}
