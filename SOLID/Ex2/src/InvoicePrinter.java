public class InvoicePrinter {

    public String printInvoice(BillingResult result) {
        StringBuilder out = new StringBuilder();
        
        // Invoice header
        out.append("Invoice# ").append(result.invoiceId).append("\n");
        
        // Line items
        for (BillingResult.LineItem item : result.lineItems) {
            out.append(String.format("- %s x%d = %.2f\n", 
                                   item.itemName, item.quantity, item.lineTotal));
        }
        
        // Summary
        out.append(String.format("Subtotal: %.2f\n", result.subtotal));
        out.append(String.format("Tax(%.0f%%): %.2f\n", result.taxPercent, result.taxAmount));
        out.append(String.format("Discount: -%.2f\n", result.discountAmount));
        out.append(String.format("TOTAL: %.2f\n", result.total));
        
        String invoice = out.toString();
        System.out.print(invoice);
        
        return invoice;
    }
}
