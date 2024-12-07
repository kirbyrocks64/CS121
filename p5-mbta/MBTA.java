import java.util.*;
import java.lang.reflect.Field;
import java.io.*;
import com.google.gson.*;
import javax.swing.border.SoftBevelBorder;

public class MBTA {
    // Train fields
    private Map<Train, List<Station>> lines;
    private Map<Train, Station> trainCurrStation;
    private Map<Train, Boolean> trainForwards;

    // Passenger fields
    private Map<Passenger, List<Station>> trips;
    private Map<Passenger, Station> passCurrStation;
    private Map<Passenger, Train> passBoarded;

    // Creates an initially empty simulation
    public MBTA() {
        lines = new HashMap<>();
        trips = new HashMap<>();
        trainCurrStation = new HashMap<>();
        passCurrStation = new HashMap<>();
        trainForwards = new HashMap<>();
        passBoarded = new HashMap<>();
    }

    // Adds a new transit line with given name and stations
    public void addLine(String name, List<String> stations) {
        Train newTrain = Train.make(name);
        List<Station> newStations = new ArrayList<>();
        for (String s : stations) {
            Station newStation = Station.make(s);
            newStations.add(newStation);
        }
        lines.put(newTrain, newStations);
        trainCurrStation.put(newTrain, newStations.get(0));
        trainForwards.put(newTrain, true);
    }

    // Adds a new planned journey to the simulation
    public void addJourney(String name, List<String> stations) {
        Passenger newPassenger = Passenger.make(name);
        List<Station> newStations = new ArrayList<>();
        for (String s : stations) {
            Station newStation = Station.make(s);
            newStations.add(newStation);
        }
        trips.put(newPassenger, newStations);
        passCurrStation.put(newPassenger, newStations.get(0));
        passBoarded.put(newPassenger, null);
    }

    // Return normally if initial simulation conditions are satisfied, otherwise
    // raises an exception
    public void checkStart() {
    }

    // Return normally if final simulation conditions are satisfied, otherwise
    // raises an exception
    public void checkEnd() {
    }

    // reset to an empty simulation
    public void reset() {
        lines.clear();
        trips.clear();
    }

    // adds simulation configuration from a file
    public void loadConfig(String filename) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            Config config = gson.fromJson(reader, Config.class);

            // Add lines to MBTA object
            if (config.getLines() != null) {
                for (Map.Entry<String, List<String>> entry : config.getLines().entrySet()) {
                    addLine(entry.getKey(), entry.getValue());

                }
            }
            
            // Add trips to MBTA object
            if (config.getTrips() != null) {
                for (Map.Entry<String, List<String>> entry : config.getTrips().entrySet()) {
                    addJourney(entry.getKey(), entry.getValue());
                }
            }
        
        } catch (Exception e) {
            throw new RuntimeException("Error while loading config file");
        }
    }

    // Edit train states
    public Station getCurrTrainStation(Train t) {
        return trainCurrStation.get(t);
    }

    public synchronized void setCurrTrainStation(Train t, Station s) {
        trainCurrStation.put(t, s);
    }

    // Edit passenger states
    public Station getCurrPassStation(Passenger p) {
        return passCurrStation.get(p);
    }

    public synchronized void setCurrPassStation(Passenger p, Station s) {
        passCurrStation.put(p, s);
    }
}

