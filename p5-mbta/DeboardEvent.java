import java.util.*;

public class DeboardEvent implements Event {
    public final Passenger p; public final Train t; public final Station s;
    public DeboardEvent(Passenger p, Train t, Station s) {
        this.p = p; this.t = t; this.s = s;
    }
    public boolean equals(Object o) {
        if (o instanceof DeboardEvent e) {
        return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
        }
        return false;
    }
    public int hashCode() {
        return Objects.hash(p, t, s);
    }
    public String toString() {
        return "Passenger " + p + " deboards " + t + " at " + s;
    }
    public List<String> toStringList() {
        return List.of(p.toString(), t.toString(), s.toString());
    }
    public void replayAndCheck(MBTA mbta) {
        if (mbta.getCurrPassTrain(p) == null) {
            throw new RuntimeException("Passenger " + p.toString() + " is not in a train so cannot deboard");
        } else if (mbta.getCurrTrainStation(t) != s) {
            throw new RuntimeException("Train " + t.toString() + " is not at station " + s.toString() + " so passenger " + p.toString() + " cannot deboard");
        }
        mbta.setCurrPassTrain(p, null);
        mbta.setCurrPassStation(p, s);
    }
}
