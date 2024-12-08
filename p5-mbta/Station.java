import java.util.ArrayList;
import java.util.List;

public class Station extends Entity {
    private static final List<Station> stationCache = new ArrayList<>();
    private Station(String name) { super(name); }

    public static Station make(String name) {
        for (Station s : stationCache) {
            if (s.toString().equals(name)) {
                return s;
            }
        }
        Station newStation = new Station(name);
        stationCache.add(newStation);
        return newStation;
    }
}
