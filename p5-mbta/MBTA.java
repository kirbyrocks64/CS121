import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.reflect.Field;
import java.io.*;
import com.google.gson.*;
import javax.swing.border.SoftBevelBorder;

public class MBTA {
    // Sim config
    private Map<Train, List<Station>> lines;
    private Map<Passenger, List<Station>> trips;

    // Train fields
    private Map<Train, Station> trainCurrStation;
    private Map<Train, Boolean> trainForwards; // true means forward, false means backward

    // Passenger fields
    private Map<Passenger, Station> passCurrStation;
    private Map<Passenger, Train> passBoarded;
    private Map<Passenger, Boolean> passTripComplete;

    // Thread fields
    private Map<Train, Lock> trainLocks;
    private Map<Station, Lock> stationLocks;
    private Map<Train, Condition> trainConditions;
    private Map<Station, Condition> stationConditions;



    // Creates an initially empty simulation
    public MBTA() {
        lines = new HashMap<>();
        trips = new HashMap<>();

        trainCurrStation = new HashMap<>();
        trainForwards = new HashMap<>();

        passCurrStation = new HashMap<>();
        passBoarded = new HashMap<>();
        passTripComplete = new HashMap<>();

        trainLocks = new HashMap<>();
        stationLocks = new HashMap<>();
        trainConditions = new HashMap<>();
        stationConditions = new HashMap<>();
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

        trainLocks.put(newTrain, new ReentrantLock());
        for (Station s : newStations) {
            stationLocks.put(s, new ReentrantLock());
        }

        trainConditions.put(newTrain, trainLocks.get(newTrain).newCondition());
        for (Station s : newStations) {
            stationConditions.put(s, stationLocks.get(s).newCondition());
        }
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
        passTripComplete.put(newPassenger, false);
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

        if (passCurrStation.size() != trips.size()) {
            throw new RuntimeException("Number of passengers does not equal number of trips");
        }

        for (Map.Entry<Passenger, List<Station>> trip : trips.entrySet()) {
            Passenger pass = trip.getKey();
            Station expectedStart = trip.getValue().get(0);
            Station actualStart = passCurrStation.get(pass);

            if (!actualStart.equals(expectedStart)) {
                throw new RuntimeException("Passenger " + pass.toString() + " is not at correct starting station");
            }
        }
    }

    // Return normally if final simulation conditions are satisfied, otherwise
    // raises an exception
    public void checkEnd() {
        for (Map.Entry<Passenger, List<Station>> trip : trips.entrySet()) {
            Passenger pass = trip.getKey();
            int journeySize = trip.getValue().size();
            Station expectedEnd = trip.getValue().get(journeySize - 1);
            Station actualEnd = passCurrStation.get(pass);

            if (passBoarded.get(pass) != null || !expectedEnd.equals(actualEnd)) {
                throw new RuntimeException("Passenger " + pass.toString() + " is not at correct end station");
            }
        }
    }

    // reset to an empty simulation
    public void reset() {
        lines.clear();
        trips.clear();

        trainCurrStation.clear();
        trainForwards.clear();

        passCurrStation.clear();
        passBoarded.clear();
        passTripComplete.clear();

        trainLocks.clear();
        stationLocks.clear();
        trainConditions.clear();
        stationConditions.clear();
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

    public synchronized Map<Train, Station> getTrainCurrStations() {
        return trainCurrStation;
    }

    public List<Station> getTrip(Passenger passenger) {
        return trips.get(passenger);
    }

    public synchronized Map<Passenger, Station> getPassCurrStations() {
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

    // Edit passenger trip completion
    public boolean isPassTripComplete(Passenger p) {
        return passTripComplete.get(p);
    }

    public synchronized void setPassTripComplete(Passenger p) {
        passTripComplete.put(p, true);
    }

    public synchronized boolean allTripsComplete() {
        for (boolean complete : passTripComplete.values()) {
            if (!complete) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean stationOccupied(Station s1) {
        for (Station s2 : trainCurrStation.values()) {
            if (s2.equals(s1)) {
                return true;
            }
        }
        return false;
    }

    public synchronized Train getTrainAtStation(Station s) {
        for (Train t : trainCurrStation.keySet()) {
            if (trainCurrStation.get(t).equals(s)) {
                return t;
            }
        }
        return null;
    }

    // Get locks and conditions
    public Lock getTrainLock(Train t) {
        return trainLocks.get(t);
    }

    public Lock getStationLock(Station s) {
        return stationLocks.get(s);
    }

    public Condition getTrainCondition(Train t) {
        return trainConditions.get(t);
    }

    public Condition getStationCondition(Station s) {
        return stationConditions.get(s);
    }
}

