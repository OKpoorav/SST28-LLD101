import java.util.*;

public class DeviceRegistry {
    private final List<Object> devices = new ArrayList<>();

    public void add(Object d) { devices.add(d); }

    public <T> T getFirst(Class<T> capabilityType) {
        for (Object d : devices) {
            if (capabilityType.isInstance(d)) return capabilityType.cast(d);
        }
        throw new IllegalStateException("Missing: " + capabilityType.getSimpleName());
    }
}
