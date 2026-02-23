/**
 * Mess add-on pricing.
 */
public class MessAddOnPricing implements PricingComponent {
    @Override
    public Money getMonthlyFee() {
        return new Money(1000.0);
    }
    
    @Override
    public String getDescription() {
        return "Mess Service";
    }
}
