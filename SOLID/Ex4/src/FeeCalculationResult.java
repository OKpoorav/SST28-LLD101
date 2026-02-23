/**
 * Result of fee calculation.
 * Separates data from presentation logic.
 */
public class FeeCalculationResult {
    public final Money monthly;
    public final Money deposit;
    public final Money total;
    
    public FeeCalculationResult(Money monthly, Money deposit) {
        this.monthly = monthly;
        this.deposit = deposit;
        this.total = monthly.plus(deposit);
    }
}
