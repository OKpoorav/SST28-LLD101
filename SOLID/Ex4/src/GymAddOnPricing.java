/**
 * Gym add-on pricing.
 */
public class GymAddOnPricing implements PricingComponent {
    @Override
    public Money getMonthlyFee() {
        return new Money(300.0);
    }
    
    @Override
    public String getDescription() {
        return "Gym Access";
    }
}
