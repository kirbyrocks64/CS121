import java.util.List;
import java.util.Map;

public class Config {
    private Map<String, List<String>> lines;
    private Map<String, List<String>> trips;

    // Helper function for retrieving lines map
    public Map<String, List<String>> getLines() {
        return lines;
    }

    // Helper function for retrieving trips map
    public Map<String, List<String>> getTrips() {
        return trips;
    }
}
