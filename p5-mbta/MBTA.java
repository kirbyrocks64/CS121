import java.util.*;
import java.lang.reflect.Field;
import java.io.*;
import com.google.gson.*;
import javax.swing.border.SoftBevelBorder;

public class MBTA {
    // Train fields
    private Map<Train, List<Station>> lines;
    private Map<Train, Station> trainCurrStation;
    private Map<Train, Boolean> trainForwards; // true means forward, false means backward

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
        // Preemptive check of number of trains vs. lines to avoid OOB error later
        if (trainCurrStation.size() != lines.size()) {
            throw new RuntimeException("Number of trains does not equal number of lines");
        }

        for (Map.Entry<Train, List<Station>> line : lines.entrySet()) {
            Train train = line.getKey();
            Station expectedStart = line.getValue().get(0);
            Station actualStart = trainCurrStation.get(train);

            if (!actualStart.equals(expectedStart)) {
                throw new RuntimeException("Train " + train.toString() + " is not at correct starting station");
            }
        }
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

    // Retrieve lines and trips from MBTA object
    public List<Station> getLine(Train train) {
        return lines.get(train);
    }

    public Map<Train, Station> getTrainCurrStations() {
        return trainCurrStation;
    }

    public List<Station> getTrip(Passenger passenger) {
        return trips.get(passenger);
    }

    public Map<Passenger, Station> getPassCurrStations() {
        return passCurrStation;
    }

    // Edit train stations
    public Station getCurrTrainStation(Train t) {
        return trainCurrStation.get(t);
    }

    public synchronized void setCurrTrainStation(Train t, Station s) {
        trainCurrStation.put(t, s);
    }

    // Edit train directions
    public boolean isTrainForwards(Train t) {
        return trainForwards.get(t);
    }

    public synchronized void turnTrainAround(Train t) {
        boolean isForwards = trainForwards.get(t);
        if (isForwards) {
            trainForwards.put(t, false);
        } else {
            trainForwards.put(t, true);
        }
    }

    // Edit passenger stations
    public Station getCurrPassStation(Passenger p) {
        return passCurrStation.get(p);
    }

    public Train getCurrPassTrain(Passenger p) {
        return passBoarded.get(p);
    }

    public synchronized void setCurrPassStation(Passenger p, Station s) {
        passCurrStation.put(p, s);
    }

    public synchronized void setCurrPassTrain(Passenger p, Train t) {
        passBoarded.put(p, t);
    }
}

