import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Cafeteria Billing ===");

        TaxPolicy taxPolicy = new StandardTaxPolicy();
        DiscountPolicy discountPolicy = new StandardDiscountPolicy();
        
        BillingCalculator calculator = new BillingCalculator(taxPolicy, discountPolicy);
        InvoicePrinter printer = new InvoicePrinter();
        InvoiceRepository repository = new FileStore();
        
        CafeteriaSystem sys = new CafeteriaSystem(calculator, printer, repository);
        
        sys.addToMenu(new MenuItem("M1", "Veg Thali", 80.00));
        sys.addToMenu(new MenuItem("C1", "Coffee", 30.00));
        sys.addToMenu(new MenuItem("S1", "Sandwich", 60.00));

        List<OrderLine> order = List.of(
                new OrderLine("M1", 2),
                new OrderLine("C1", 1)
        );

        sys.checkout("student", order);
        
        System.out.println();
        
        List<OrderLine> staffOrder = List.of(
                new OrderLine("M1", 1),
                new OrderLine("C1", 1),
                new OrderLine("S1", 1)
        );
        
        sys.checkout("staff", staffOrder);
    }
}
