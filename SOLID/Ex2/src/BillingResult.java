import java.util.*;

/**
 * Data class holding billing calculation results.
 * Separates data from presentation/formatting logic.
 */
public class BillingResult {
    public final String invoiceId;
    public final List<LineItem> lineItems;
    public final double subtotal;
    public final double taxPercent;
    public final double taxAmount;
    public final double discountAmount;
    public final double total;
    
    public BillingResult(String invoiceId, List<LineItem> lineItems, 
                        double subtotal, double taxPercent, double taxAmount, 
                        double discountAmount, double total) {
        this.invoiceId = invoiceId;
        this.lineItems = lineItems;
        this.subtotal = subtotal;
        this.taxPercent = taxPercent;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.total = total;
    }
    
    /**
     * Represents a line item in the invoice.
     */
    public static class LineItem {
        public final String itemName;
        public final int quantity;
        public final double lineTotal;
        
        public LineItem(String itemName, int quantity, double lineTotal) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.lineTotal = lineTotal;
        }
    }
}
