import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Sim {

public static void run_sim(MBTA mbta, Log log) {
    Sim sim = new Sim();
    List<Thread> threads = new ArrayList<>();

    for (Train train : mbta.getTrainCurrStations().keySet()) {
        Thread t = new Thread(() -> sim.runTrain());
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
                } finally {
                    nextLock.unlock();
                }
            } finally {
                currLock.unlock();
            }
            break;
        }
    }
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
