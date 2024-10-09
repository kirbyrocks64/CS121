import java.util.*;

abstract public class Piece {
    private static final HashMap<Character, PieceFactory> pieceMap = 
        new HashMap<>();
    private static Color color;
    private static char[] cols = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private static char[] rows = {'8', '7', '6', '5', '4', '3', '2', '1'};

    public static void registerPiece(PieceFactory pf) {
	    char pieceSymbol = pf.symbol();
        pieceMap.put(pieceSymbol, pf);
    }

    public static Piece createPiece(String name) {
        char charColor = name.charAt(0);
        if (charColor == 'b') {
            color = Color.BLACK;
        } else if (charColor == 'w') {
            color = Color.WHITE;
        } else {
            throw new IllegalArgumentException("Invalid color");
        }

        char symbol = name.charAt(1);
	    PieceFactory pf = pieceMap.get(symbol);

        return pf.create(color);
    }

    public Color color() {
        return color;
    }

    abstract public String toString();

    abstract public List<String> moves(Board b, String loc);

    public boolean stringInRange(String loc) {
        for (int i = 0; i < cols.length; i++) {
            if (cols[i] == loc.charAt(0)) { 
                for (int j = 0; j < rows.length; j++) {
                    if (rows[j] == loc.charAt(1)) { return true; }
                }
            }
        }
        return false;
    }

    public boolean charInRange(char loc) {
        for (int i = 0; i < cols.length; i++) {
            if (cols[i] == loc) { return true; }
        }
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == loc) { return true; }
        }
        return false;
    }
}