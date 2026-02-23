import java.util.*;

public class HostelFeeCalculator {
    private final FakeBookingRepo repo;

    public HostelFeeCalculator(FakeBookingRepo repo) { this.repo = repo; }

     
    public void process(BookingRequest req) {
        // Build pricing components from request (factory handles the mapping)
        List<PricingComponent> components = PricingComponentFactory.buildComponents(req);
        
        // Calculate monthly fee by summing all components
        Money monthly = calculateMonthly(components);
        Money deposit = new Money(5000.00);
        
        // Create result object
        FeeCalculationResult result = new FeeCalculationResult(monthly, deposit);

        // Print receipt
        ReceiptPrinter.print(req, result);

        // Save booking
        String bookingId = "H-" + (7000 + new Random(1).nextInt(1000)); // deterministic-ish
        repo.save(bookingId, req, monthly, deposit);
    }

   
    private Money calculateMonthly(List<PricingComponent> components) {
        Money total = new Money(0.0);
        for (PricingComponent component : components) {
            total = total.plus(component.getMonthlyFee());
        }
        return total;
    }
}
