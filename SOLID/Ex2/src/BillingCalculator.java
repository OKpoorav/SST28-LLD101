import java.util.*;

/**
 * Handles billing calculations (subtotal, tax, discount, total).
 * Single responsibility: money computations only, no formatting or persistence.
 */
public class BillingCalculator {
    private final TaxPolicy taxPolicy;
    private final DiscountPolicy discountPolicy;
    
    public BillingCalculator(TaxPolicy taxPolicy, DiscountPolicy discountPolicy) {
        this.taxPolicy = taxPolicy;
        this.discountPolicy = discountPolicy;
    }
    
    /**
     * Calculate billing details for an order.
     * Returns structured data (BillingResult) rather than formatted strings.
     */
    public BillingResult calculate(String invoiceId, String customerType, 
                                   List<OrderLine> orderLines, 
                                   Map<String, MenuItem> menu) {
        // Calculate subtotal and build line items
        double subtotal = 0.0;
        List<BillingResult.LineItem> lineItems = new ArrayList<>();
        
        for (OrderLine line : orderLines) {
            MenuItem item = menu.get(line.itemId);
            double lineTotal = item.price * line.qty;
            subtotal += lineTotal;
            lineItems.add(new BillingResult.LineItem(item.name, line.qty, lineTotal));
        }
        
        // Calculate tax
        double taxPercent = taxPolicy.getTaxPercent(customerType);
        double taxAmount = taxPolicy.calculateTax(customerType, subtotal);
        
        // Calculate discount
        double discountAmount = discountPolicy.calculateDiscount(customerType, subtotal, orderLines.size());
        
        // Calculate total
        double total = subtotal + taxAmount - discountAmount;
        
        return new BillingResult(invoiceId, lineItems, subtotal, taxPercent, 
                                taxAmount, discountAmount, total);
    }
}
