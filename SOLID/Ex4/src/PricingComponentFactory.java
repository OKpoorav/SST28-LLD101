import java.util.*;


public class PricingComponentFactory {
    
  
    public static PricingComponent createRoomPricing(int roomType) {
        return switch (roomType) {
            case LegacyRoomTypes.SINGLE -> new SingleRoomPricing();
            case LegacyRoomTypes.DOUBLE -> new DoubleRoomPricing();
            case LegacyRoomTypes.TRIPLE -> new TripleRoomPricing();
            case LegacyRoomTypes.DELUXE -> new DeluxeRoomPricing();
            default -> new DeluxeRoomPricing(); // default fallback
        };
    }
    
  
    public static PricingComponent createAddOnPricing(AddOn addOn) {
        return switch (addOn) {
            case MESS -> new MessAddOnPricing();
            case LAUNDRY -> new LaundryAddOnPricing();
            case GYM -> new GymAddOnPricing();
        };
    }
 
    public static List<PricingComponent> buildComponents(BookingRequest request) {
        List<PricingComponent> components = new ArrayList<>();
        
        // Add room pricing
        components.add(createRoomPricing(request.roomType));
        
        // Add all add-on pricings
        for (AddOn addOn : request.addOns) {
            components.add(createAddOnPricing(addOn));
        }
        
        return components;
    }
}
