import java.util.*;

abstract public class Piece {
    private static final HashMap<Character, PieceFactory> pieceMap = 
        new HashMap<>();
    private static Color color;

    public static void registerPiece(PieceFactory pf) {
	    char pieceSymbol = pf.symbol();
        pieceMap.put(pieceSymbol, pf);
    }

    public static Piece createPiece(String name) {
        char charColor = name.charAt(0);
        if (charColor == 'b') {
            color = Color.BLACK;
        } else {
            color = Color.WHITE;
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
}