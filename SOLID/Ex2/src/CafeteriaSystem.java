import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final BillingCalculator calculator;
    private final InvoicePrinter printer;
    private final InvoiceRepository repository;
    private int invoiceSeq = 1000;

    public CafeteriaSystem(BillingCalculator calculator, InvoicePrinter printer, 
                          InvoiceRepository repository) {
        this.calculator = calculator;
        this.printer = printer;
        this.repository = repository;
    }

    public void addToMenu(MenuItem i) { menu.put(i.id, i); }


    public void checkout(String customerType, List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);
        
        // Delegate calculation to BillingCalculator
        BillingResult result = calculator.calculate(invId, customerType, lines, menu);
        
        // Delegate formatting and printing to InvoicePrinter
        String formattedInvoice = printer.printInvoice(result);
        
        // Delegate persistence to InvoiceRepository
        repository.save(invId, formattedInvoice);
        System.out.println("Saved invoice: " + invId + " (lines=" + repository.countLines(invId) + ")");
    }
}
