import java.util.ArrayList;
import java.util.List;

public class Passenger extends Entity {
    private static final List<Passenger> passengerCache = new ArrayList<>();
    private Passenger(String name) { super(name); }

    public static Passenger make(String name) {
        for (Passenger p : passengerCache) {
            if (p.toString().equals(name)) {
                return p;
            }
        }
        Passenger newPassenger = new Passenger(name);
        passengerCache.add(newPassenger);
        return newPassenger;
    }
}
