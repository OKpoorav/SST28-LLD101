import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hostel Fee Calculator ===");
        
         BookingRequest req = new BookingRequest(LegacyRoomTypes.DOUBLE, List.of(AddOn.LAUNDRY, AddOn.MESS));
        HostelFeeCalculator calc = new HostelFeeCalculator(new FakeBookingRepo());
        calc.process(req);
        
        System.out.println();
        System.out.println("--- Stretch Goal: Late Fee Component ---");
        
         BookingRequest lateReq = new BookingRequest(LegacyRoomTypes.SINGLE, List.of(AddOn.GYM));
        
        List<PricingComponent> components = PricingComponentFactory.buildComponents(lateReq);
         components.add(new LateFeeComponent(5));
        
         Money monthly = new Money(0.0);
        for (PricingComponent component : components) {
            monthly = monthly.plus(component.getMonthlyFee());
        }
        
        Money deposit = new Money(5000.00);
        FeeCalculationResult result = new FeeCalculationResult(monthly, deposit);
        
        System.out.println("Room: " + LegacyRoomTypes.nameOf(lateReq.roomType) + " | AddOns: " + lateReq.addOns + " | Late: 5 days");
        System.out.println("Monthly: " + result.monthly);
        System.out.println("Deposit: " + result.deposit);
        System.out.println("Breakdown:");
        for (PricingComponent component : components) {
            System.out.println("  - " + component.getDescription() + ": " + component.getMonthlyFee());
        }
        System.out.println("TOTAL DUE NOW: " + result.total);
    }
}
