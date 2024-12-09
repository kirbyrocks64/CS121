import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Sim {

public static void run_sim(MBTA mbta, Log log) {
    Sim sim = new Sim();
    List<Thread> threads = new ArrayList<>();

    for (Train t : mbta.getTrainCurrStations().keySet()) {
        Thread trainThread = new Thread(() -> sim.runTrain(mbta, t, log));
        threads.add(trainThread);
        trainThread.start();
    }

    for (Passenger p : mbta.getPassCurrStations().keySet()) {
        Thread passThread = new Thread(() -> sim.runPassenger(mbta, p, log));
        threads.add(passThread);
        passThread.start();
    }

    for (Thread thread : threads) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Simulation interrupted with error: ", e);
        }
    }
}

private void runTrain(MBTA mbta, Train t, Log log) {
    List<Station> line = mbta.getLine(t);
    int currStationIndex = 0;

    while (!mbta.allTripsComplete()) {
        boolean trainForwards = mbta.isTrainForwards(t);
        int nextStationIndex;

        if (trainForwards) {
            nextStationIndex = currStationIndex + 1;
            if (nextStationIndex >= line.size()) {
                nextStationIndex -= 2;
                mbta.turnTrainAround(t);
            }
        } else {
            nextStationIndex = currStationIndex - 1;
            if (nextStationIndex < 0) {
                nextStationIndex += 2;
                mbta.turnTrainAround(t);
            }
        }

        Station currStation = line.get(currStationIndex);
        Station nextStation = line.get(nextStationIndex);

        Lock currLock = mbta.getStationLock(currStation);
        Condition currCondition = mbta.getStationCondition(currStation);
        Lock nextLock = mbta.getStationLock(nextStation);
        Condition nextCondition = mbta.getStationCondition(nextStation);
        
        currLock.lock();
        while (true) {
            try {
                nextLock.lock();
                try {
                    while (mbta.stationOccupied(nextStation)) {
                        try {
                            nextCondition.await();
                        } catch (Exception e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    mbta.setCurrTrainStation(t, nextStation);
                    log.train_moves(t, currStation, nextStation);

                    currCondition.signalAll();
                    nextCondition.signalAll();
                } finally {
                    nextLock.unlock();
                }
            } finally {
                currLock.unlock();
            }
            break;
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        currStationIndex = nextStationIndex;
    }
}

private void runPassenger(MBTA mbta, Passenger p, Log log) {
    List<Station> trip = mbta.getTrip(p);
    for (int i = 0; i < trip.size() - 1; i++) {
        Station currStation = trip.get(i);
        Station nextStation = trip.get(i + 1);

        Lock currLock = mbta.getStationLock(currStation);
        Condition currCondition = mbta.getStationCondition(currStation);
        while (true) {
            currLock.lock();
            try {
                Train t = mbta.getTrainAtStation(currStation);
                if (t != null && mbta.getLine(t).contains(nextStation)) {
                    mbta.setCurrPassTrain(p, t);
                    log.passenger_boards(p, t, currStation);
                    break;
                }
            } finally {
                currCondition.signalAll();
                currLock.unlock();
            }
        }

        Lock nextLock = mbta.getStationLock(nextStation);
        Condition nextCondition = mbta.getStationCondition(nextStation);
        while (true) {
            Station holdNewStation;
            nextLock.lock();
            try {
                holdNewStation = mbta.getCurrTrainStation(mbta.getCurrPassTrain(p));
                if (holdNewStation.equals(nextStation)) {
                    log.passenger_deboards(p, mbta.getCurrPassTrain(p), nextStation);
                    mbta.setCurrPassTrain(p, null);
                    mbta.setCurrPassStation(p, nextStation);
                    break;
                }
            } finally {
                nextCondition.signalAll();
                nextLock.unlock();
            }
        }
    }
    mbta.setPassTripComplete(p);
}

public static void main(String[] args) throws Exception {
    if (args.length != 1) {
    System.out.println("usage: ./sim <config file>");
    System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);

    Log log = new Log();

    run_sim(mbta, log);

    String s = new LogJson(log).toJson();
    PrintWriter out = new PrintWriter("log.json");
    out.print(s);
    out.close();

    mbta.reset();
    mbta.loadConfig(args[0]);
    Verify.verify(mbta, log);
}
}
