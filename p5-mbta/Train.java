import java.util.ArrayList;
import java.util.List;

public class Train extends Entity {
    private static final List<Train> trainCache = new ArrayList<>();
    private Train(String name) { super(name); }

    public static Train make(String name) {
        for (Train t : trainCache) {
            if (t.toString().equals(name)) {
                return t;
            }
        }
        Train newTrain = new Train(name);
        trainCache.add(newTrain);
        return newTrain;
    }
}
