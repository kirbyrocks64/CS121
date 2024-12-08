import java.util.*;

public class MoveEvent implements Event {
    public final Train t; public final Station s1, s2;
    public MoveEvent(Train t, Station s1, Station s2) {
        this.t = t; this.s1 = s1; this.s2 = s2;
    }
    public boolean equals(Object o) {
        if (o instanceof MoveEvent e) {
        return t.equals(e.t) && s1.equals(e.s1) && s2.equals(e.s2);
        }
        return false;
    }
    public int hashCode() {
        return Objects.hash(t, s1, s2);
    }
    public String toString() {
        return "Train " + t + " moves from " + s1 + " to " + s2;
    }
    public List<String> toStringList() {
        return List.of(t.toString(), s1.toString(), s2.toString());
    }
    public void replayAndCheck(MBTA mbta) {
        if (mbta.getCurrTrainStation(t) != s1) {
            throw new RuntimeException("Train " + t.toString() + " is not at station " + s1.toString());
        }

        Map<Train, Station> trainLocs = mbta.getTrainCurrStations();
        for (Station s : trainLocs.values()) {
            if (s.equals(s2)) {
                throw new RuntimeException("Another train is currently at station " + s2.toString());
            }
        }

        List<Station> tLine = mbta.getLine(t);
        int currIndex = tLine.indexOf(s1);
        int nextIndex;
        boolean tDirection = mbta.isTrainForwards(t);
        if (tDirection) {
            nextIndex = currIndex + 1;
            if (nextIndex >= tLine.size()) {
                nextIndex -= 2;
                mbta.turnTrainAround(t);
            }
        } else {
            nextIndex = currIndex - 1;
            if (nextIndex < 0) {
                nextIndex += 2;
                mbta.turnTrainAround(t);
            }
        }

        if (tLine.get(nextIndex) != s2) {
            throw new RuntimeException("Train " + t.toString() + " does not have next station " + s2.toString());
        }

        mbta.setCurrTrainStation(t, s2);
    }
}
