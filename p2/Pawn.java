import java.util.*;

public class Pawn extends Piece {
    private final Color color;

    public Pawn(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bp"; } 
        return "wp";
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}