
public interface TaxPolicy {

    double calculateTax(String customerType, double subtotal);
    

    double getTaxPercent(String customerType);
}
