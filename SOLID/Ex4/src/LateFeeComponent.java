/**
 * Late fee pricing component.
 * Demonstrates OCP: We can add this new fee type without editing HostelFeeCalculator!
 */
public class LateFeeComponent implements PricingComponent {
    private final int daysLate;
    private static final double FEE_PER_DAY = 50.0;
    
    public LateFeeComponent(int daysLate) {
        this.daysLate = daysLate;
    }
    
    @Override
    public Money getMonthlyFee() {
        return new Money(daysLate * FEE_PER_DAY);
    }
    
    @Override
    public String getDescription() {
        return "Late Fee (" + daysLate + " days)";
    }
}
