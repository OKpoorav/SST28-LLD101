
public class StandardTaxPolicy implements TaxPolicy {
    
    @Override
    public double calculateTax(String customerType, double subtotal) {
        double percent = getTaxPercent(customerType);
        return subtotal * (percent / 100.0);
    }
    
    @Override
    public double getTaxPercent(String customerType) {
        if ("student".equalsIgnoreCase(customerType)) return 5.0;
        if ("staff".equalsIgnoreCase(customerType)) return 2.0;
        return 8.0;
    }
}
