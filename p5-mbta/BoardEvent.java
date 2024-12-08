import java.util.*;

public class BoardEvent implements Event {
    public final Passenger p; public final Train t; public final Station s;
    public BoardEvent(Passenger p, Train t, Station s) {
        this.p = p; this.t = t; this.s = s;
    }
    public boolean equals(Object o) {
        if (o instanceof BoardEvent e) {
        return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
        }
        return false;
    }
    public int hashCode() {
        return Objects.hash(p, t, s);
    }
    public String toString() {
        return "Passenger " + p + " boards " + t + " at " + s;
    }
    public List<String> toStringList() {
        return List.of(p.toString(), t.toString(), s.toString());
    }
    public void replayAndCheck(MBTA mbta) {
        if (mbta.getCurrPassTrain(p) != null) {
            throw new RuntimeException("Passenger " + p.toString() + " is already boarded");
        } else if (mbta.getCurrPassStation(p) != s) {
            throw new RuntimeException("Passenger " + p.toString() + " is not at station " + s.toString() + " so cannot board");
        } else if (mbta.getCurrTrainStation(t) != s) {
            throw new RuntimeException("Train " + t.toString() + " is not at station " + s.toString() + " so passenger " + p.toString() + " cannot board");
        }
        mbta.setCurrPassTrain(p, t);
    }
}
