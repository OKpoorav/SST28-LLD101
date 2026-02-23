/**
 * Laundry add-on pricing.
 */
public class LaundryAddOnPricing implements PricingComponent {
    @Override
    public Money getMonthlyFee() {
        return new Money(500.0);
    }
    
    @Override
    public String getDescription() {
        return "Laundry Service";
    }
}
